package roon.hitmarker;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class HitMarkerConfig {
	
    public static void Register() {
        registerClientConfigs();
    }

	private static void registerClientConfigs() {
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
        ClientConfig.registerConfig(CLIENT_BUILDER);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_BUILDER.build());
    }

    protected static class ClientConfig
	{
		public static ForgeConfigSpec.ConfigValue<Boolean> hitMarkerEnabled;
        public static ForgeConfigSpec.ConfigValue<Integer> hitMarkerDuration;
        public static ForgeConfigSpec.ConfigValue<Float> hitSoundVolume;
		
		public static void registerConfig(final ForgeConfigSpec.Builder builder)
		{
			builder.push("general");
			
			hitMarkerEnabled = builder
					.comment("Whether the hit marker is enabled")
					.define("hitMarkerEnabled", true);
            hitMarkerDuration = builder
                    .comment("How many ticks the marker lasts")
                    .define("hitMarkerDuration", 20);
            hitSoundVolume = builder
					.comment("How loud the volume of the hit is, set to 0 to turn off")
					.define("hitSoundVolume", 0.5f);
			
			builder.pop();
		}
		
	}
	
}
