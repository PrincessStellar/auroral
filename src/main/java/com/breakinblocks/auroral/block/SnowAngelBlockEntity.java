package com.breakinblocks.auroral.block;

import com.breakinblocks.auroral.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
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

public class SnowAngelBlockEntity extends BlockEntity {

    public static final int FADE_TIME = 6000; // 5 minutes

    private int age = 0;
    private boolean permanent = false;

    public SnowAngelBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SNOW_ANGEL.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, SnowAngelBlockEntity blockEntity) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        if (blockEntity.permanent) {
            return;
        }

        blockEntity.age++;

        if (blockEntity.age >= FADE_TIME) {
            serverLevel.sendParticles(ParticleTypes.SNOWFLAKE,
                pos.getX() + 0.5, pos.getY() + 0.25, pos.getZ() + 0.5,
                10, 0.5, 0.1, 0.5, 0.02);

            level.removeBlock(pos, false);
        }
    }

    public void makePermanent() {
        this.permanent = true;
        this.setChanged();
    }

    public boolean isPermanent() {
        return permanent;
    }

    public int getAge() {
        return age;
    }

    public float getFadeProgress() {
        if (permanent) return 0.0f;
        return (float) age / FADE_TIME;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("Age", age);
        output.putBoolean("Permanent", permanent);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        age = input.getIntOr("Age", 0);
        permanent = input.getBooleanOr("Permanent", false);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
