package net.grodax.f1mod.sound;

import net.grodax.f1mod.F1Mod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, F1Mod.MOD_ID);

    public static final RegistryObject<SoundEvent> ENGINE_SOUND = SOUND_EVENTS.register("engine_sound", () ->
            SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(F1Mod.MOD_ID, "engine_sound")));

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
