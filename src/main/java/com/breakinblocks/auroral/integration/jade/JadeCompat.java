package com.breakinblocks.auroral.integration.jade;

import com.breakinblocks.auroral.Auroral;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import snownee.jade.Jade;

/**
 * Workaround for jade 26.0.5 through 26.0.8: Jade defines
 * {@code Jade.registerGameRules()} but never calls it, so its
 * {@code MAX_POSITION_DEVIATION} stays null and every tooltip request NPEs
 * server-side. We invoke it during the GAME_RULE {@link RegisterEvent} on its
 * behalf. Jade 26.0.9+ registers the game rule itself, so we skip the
 * workaround there to avoid a duplicate-key registry crash.
 */
public final class JadeCompat {

    private static final ArtifactVersion JADE_FIXED_VERSION = new DefaultArtifactVersion("26.0.9");

    private JadeCompat() {}

    public static void register(IEventBus eventBus) {
        if (jadeRegistersGameRulesItself()) {
            Auroral.LOGGER.debug("Jade compat: Jade registers game rules itself, skipping workaround");
            return;
        }
        eventBus.addListener(JadeCompat::onRegister);
    }

    private static boolean jadeRegistersGameRulesItself() {
        return ModList.get().getModContainerById("jade")
            .map(c -> c.getModInfo().getVersion().compareTo(JADE_FIXED_VERSION) >= 0)
            .orElse(false);
    }

    private static void onRegister(RegisterEvent event) {
        if (event.getRegistryKey() != Registries.GAME_RULE) return;
        try {
            Jade.registerGameRules();
        } catch (Throwable t) {
            Auroral.LOGGER.warn("Jade compat: registerGameRules() failed (likely already registered): {}", t.toString());
        }
    }
}
