package net.grodax.f1mod.client;

import net.grodax.f1mod.F1Mod;
import net.grodax.f1mod.entity.custom.F1CarEntity;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = F1Mod.MOD_ID, value = Dist.CLIENT)
public class ClientInputHandler {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null && mc.player.getVehicle() instanceof F1CarEntity car) {
                // Check if the key was just pressed
                while (KeyBindings.TOGGLE_DRS.consumeClick()) {
                    // Toggle the state
                    car.toggleDRS();

                    // IMPORTANT: You will eventually need a Packet here
                    // to tell the server the key was pressed!
                }
            }
        }
    }
}
