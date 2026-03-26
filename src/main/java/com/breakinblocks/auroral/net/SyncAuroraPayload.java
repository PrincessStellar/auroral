package com.breakinblocks.auroral.net;

import com.breakinblocks.auroral.Auroral;
import com.breakinblocks.auroral.client.ClientAuroraState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncAuroraPayload(boolean active) implements CustomPacketPayload {
    public static final Type<SyncAuroraPayload> TYPE = new Type<>(Auroral.id("sync_aurora"));

    public static final StreamCodec<FriendlyByteBuf, SyncAuroraPayload> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.BOOL, SyncAuroraPayload::active,
        SyncAuroraPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleOnClient(SyncAuroraPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> ClientAuroraState.setAuroraActive(payload.active));
    }
}
