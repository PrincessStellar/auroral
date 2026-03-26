package com.breakinblocks.auroral.net;

import com.breakinblocks.auroral.Auroral;
import com.breakinblocks.auroral.util.AuroraHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class AuroralNetworking {

    public static void register(IEventBus eventBus) {
        eventBus.addListener(AuroralNetworking::registerPayloads);
    }

    private static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(Auroral.MOD_ID);

        registrar.playToClient(
            SyncAuroraPayload.TYPE,
            SyncAuroraPayload.STREAM_CODEC,
            SyncAuroraPayload::handleOnClient
        );
    }

    public static void syncAuroraToAll(ServerLevel level, boolean active) {
        SyncAuroraPayload payload = new SyncAuroraPayload(active);
        for (ServerPlayer player : level.players()) {
            PacketDistributor.sendToPlayer(player, payload);
        }
    }

    public static void syncAuroraToPlayer(ServerPlayer player) {
        if (player.level() instanceof ServerLevel serverLevel) {
            boolean active = AuroraHelper.isAuroraActive(serverLevel);
            PacketDistributor.sendToPlayer(player, new SyncAuroraPayload(active));
        }
    }
}
