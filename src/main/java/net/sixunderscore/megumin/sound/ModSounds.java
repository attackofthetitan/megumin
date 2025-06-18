package net.sixunderscore.megumin.sound;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.sixunderscore.megumin.Megumin;

public class ModSounds {
    public static SoundEvent BIG_EXPLOSION = SoundEvent.of(Identifier.of(Megumin.MOD_ID,"big_explosion"));
    public static SoundEvent MAGIC_RING = SoundEvent.of(Identifier.of(Megumin.MOD_ID,"magic_ring"));
    public static SoundEvent MAGIC_AMBIENT = SoundEvent.of(Identifier.of(Megumin.MOD_ID,"magic_ambient"));
    public static SoundEvent MAGIC_CHANT = SoundEvent.of(Identifier.of(Megumin.MOD_ID, "magic_chant"));

    public static void register() {
        Megumin.LOGGER.info("Registering sounds for: " + Megumin.MOD_ID);
        Registry.register(Registries.SOUND_EVENT, Identifier.of(Megumin.MOD_ID, "big_explosion"), BIG_EXPLOSION);
        Registry.register(Registries.SOUND_EVENT, Identifier.of(Megumin.MOD_ID, "magic_ring"), MAGIC_RING);
        Registry.register(Registries.SOUND_EVENT, Identifier.of(Megumin.MOD_ID, "magic_ambient"), MAGIC_AMBIENT);
        Registry.register(Registries.SOUND_EVENT, Identifier.of(Megumin.MOD_ID, "magic_chant"), MAGIC_CHANT);
    }
}
