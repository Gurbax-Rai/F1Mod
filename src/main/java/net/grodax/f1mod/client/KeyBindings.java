package net.grodax.f1mod.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class KeyBindings {

    public static final KeyMapping TOGGLE_DRS = new KeyMapping(
            "key.f1mod.toggle_drs",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_F, // default key
            "key.categories.f1mod"
    );

    // Register the key mapping with Forge
    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(TOGGLE_DRS);
    }
}

