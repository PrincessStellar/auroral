package com.breakinblocks.auroral.block;

import com.breakinblocks.auroral.registry.ModBlockEntities;
import com.breakinblocks.auroral.registry.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HearthwoodLogBlockEntity extends BlockEntity {

    public static final int MAX_BURN_TIME = 168000; // 7 days
    public static final double EFFECT_RADIUS = 16.0;
    private static final int EFFECT_INTERVAL = 40; // 2 seconds
    private static final int EFFECT_DURATION = 60; // 3 seconds

    private int burnTimeRemaining = MAX_BURN_TIME;
    private int effectTimer = 0;

    public HearthwoodLogBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.HEARTHWOOD_LOG.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, HearthwoodLogBlockEntity blockEntity) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        if (!state.getValue(HearthwoodLogBlock.LIT)) {
            return;
        }

        blockEntity.burnTimeRemaining--;

        if (blockEntity.burnTimeRemaining <= 0) {
            level.setBlock(pos, state.setValue(HearthwoodLogBlock.LIT, false), 3);
            blockEntity.setChanged();
            return;
        }

        blockEntity.effectTimer++;
        if (blockEntity.effectTimer >= EFFECT_INTERVAL) {
            blockEntity.effectTimer = 0;
            blockEntity.applyEffectsToNearbyPlayers(serverLevel, pos);
        }

        if (blockEntity.burnTimeRemaining % 1200 == 0) {
            blockEntity.setChanged();
        }
    }

    private void applyEffectsToNearbyPlayers(ServerLevel level, BlockPos pos) {
        AABB effectBox = new AABB(pos).inflate(EFFECT_RADIUS);
        List<Player> nearbyPlayers = level.getEntitiesOfClass(Player.class, effectBox);

        for (Player player : nearbyPlayers) {
            player.addEffect(new MobEffectInstance(
                ModEffects.FROSTBITE_IMMUNITY,
                EFFECT_DURATION,
                0,
                true,
                false,
                true
            ));
        }
    }

    public int getBurnTimeRemaining() {
        return burnTimeRemaining;
    }

    public float getBurnProgress() {
        return (float) burnTimeRemaining / MAX_BURN_TIME;
    }

    public boolean isLit() {
        return burnTimeRemaining > 0 && getBlockState().getValue(HearthwoodLogBlock.LIT);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("BurnTime", burnTimeRemaining);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        burnTimeRemaining = input.getIntOr("BurnTime", MAX_BURN_TIME);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
