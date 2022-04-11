package roon.hitmarker;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class HitMarkerClient {

    public static int remainingTicks = 0;
    public static boolean kill = false;

    public static void clientSetup(FMLClientSetupEvent e) {
        MinecraftForge.EVENT_BUS.addListener(HitMarkerClient::tick);
        MinecraftForge.EVENT_BUS.addListener(HitMarkerClient::renderCrosshair);
    }

    private static void tick(TickEvent.ClientTickEvent e) {
        if (remainingTicks > 0) {
            remainingTicks--;
        }
    }

    private static final ResourceLocation HIT_TEXTURE = new ResourceLocation(HitMarker.MODID, "textures/hit.png");

    private static void renderCrosshair(RenderGameOverlayEvent.Post e) {
        // System.out.print(e.getType());
        
        if (e.getType() == RenderGameOverlayEvent.ElementType.LAYER) {
            if (HitMarkerClient.remainingTicks > 0) {
                
                int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
                int scaledHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

                RenderSystem.setShader(GameRenderer::getPositionTexShader);

                if (kill) {
                    RenderSystem.setShaderColor(1.0f, 0.0f, 0.0f, 1.0f); 
                }
                else {
                    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f); 
                }
                
                RenderSystem.setShaderTexture(0, HIT_TEXTURE);
                Gui.blit(e.getMatrixStack(), (scaledWidth - 11) / 2, (scaledHeight - 11) / 2, 0.0F, 0.0F, 11, 11, 11, 11);

            }
        }
    }

    public static void receiveHit(boolean kill) {
        if(HitMarkerConfig.ClientConfig.hitMarkerEnabled.get()) {
            Minecraft.getInstance().player.playSound(HitMarker.HIT_SOUND_EVENT, HitMarkerConfig.ClientConfig.hitSoundVolume.get(), 1.0f);
            remainingTicks = HitMarkerConfig.ClientConfig.hitMarkerDuration.get();
            HitMarkerClient.kill = kill;
        }
    }

}