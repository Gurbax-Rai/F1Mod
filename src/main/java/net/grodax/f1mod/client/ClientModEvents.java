package net.grodax.f1mod.client;

import net.grodax.f1mod.entity.ModEntities;
import net.grodax.f1mod.entity.client.F1CarRenderer;
import net.grodax.f1mod.entity.client.model.F1CarModel;

import net.grodax.f1mod.entity.custom.F1CarEntity;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.grodax.f1mod.client.KeyBindings.TOGGLE_DRS;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.F1_CAR_ENTITY.get(), F1CarRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(F1CarModel.LAYER_LOCATION, F1CarModel::createBodyLayer);
    }
}