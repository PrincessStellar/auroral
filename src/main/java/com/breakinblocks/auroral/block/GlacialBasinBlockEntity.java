package com.breakinblocks.auroral.block;

import com.breakinblocks.auroral.config.AuroralConfig;
import com.breakinblocks.auroral.registry.ModBlockEntities;
import com.breakinblocks.auroral.util.AuroraHelper;
import com.breakinblocks.auroral.util.BiomeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class GlacialBasinBlockEntity extends BlockEntity {
    private int fillTickCounter = 0;

    public GlacialBasinBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GLACIAL_BASIN.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, GlacialBasinBlockEntity blockEntity) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        if (!AuroraHelper.isAuroraActive(serverLevel)) {
            return;
        }

        if (!BiomeHelper.isColdBiome(level, pos)) {
            return;
        }

        int maxAura = AuroralConfig.SERVER.basinMaxAura.get();
        int currentAura = state.getValue(GlacialBasinBlock.AURA_LEVEL);

        if (currentAura >= maxAura) {
            return;
        }

        blockEntity.fillTickCounter++;

        int fillRate = AuroralConfig.SERVER.basinFillRate.get();
        if (blockEntity.fillTickCounter >= fillRate) {
            blockEntity.fillTickCounter = 0;

            int newLevel = Math.min(currentAura + 1, maxAura);
            level.setBlock(pos, state.setValue(GlacialBasinBlock.AURA_LEVEL, newLevel), 3);
            blockEntity.setChanged();
        }
    }

    public int getAuraLevel() {
        BlockState state = getBlockState();
        if (state.hasProperty(GlacialBasinBlock.AURA_LEVEL)) {
            return state.getValue(GlacialBasinBlock.AURA_LEVEL);
        }
        return 0;
    }

    public void setAuraLevel(int newLevel) {
        if (level != null) {
            BlockState state = getBlockState();
            level.setBlock(worldPosition, state.setValue(GlacialBasinBlock.AURA_LEVEL, newLevel), 3);
            setChanged();
        }
    }

    public boolean hasAura() {
        return getAuraLevel() > 0;
    }

    public void consumeAura() {
        int currentLevel = getAuraLevel();
        if (currentLevel > 0) {
            setAuraLevel(currentLevel - 1);
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("FillCounter", fillTickCounter);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        fillTickCounter = input.getIntOr("FillCounter", 0);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
