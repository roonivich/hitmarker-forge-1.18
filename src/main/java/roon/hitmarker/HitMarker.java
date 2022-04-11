package roon.hitmarker;

import java.util.function.BiConsumer;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod(HitMarker.MODID)
public class HitMarker {

    public static final String MODID = "hitmarker";
    public static final SoundEvent HIT_SOUND_EVENT = new SoundEvent(new ResourceLocation(MODID, "hit"));

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(HitMarker.MODID, "main"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );
    private int index = 0;

    public HitMarker() {
        
        //Packets
		Register();
        HitMarkerConfig.Register();
        
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        if (FMLEnvironment.dist.isClient()) {
            bus.addListener(HitMarkerClient::clientSetup);
        }
        MinecraftForge.EVENT_BUS.addListener(this::hit);
        MinecraftForge.EVENT_BUS.addListener(this::death);
    }

    private void hit(LivingDamageEvent e) {
        sendToPlayer(false, e.getSource());
    }

    private void death(LivingDeathEvent e) {
        sendToPlayer(true, e.getSource());
    }

    private void Register() {
        INSTANCE.registerMessage(index++, HitPacket.class, HitPacket::encode, HitPacket::decode, HitPacket::handle);
    }

    private void sendToPlayer(boolean kill, DamageSource attacker) {
        if (attacker.getDirectEntity() instanceof Player) {
            Player player = (Player)attacker.getDirectEntity();
            INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new HitPacket(kill));
        }
    }
}
