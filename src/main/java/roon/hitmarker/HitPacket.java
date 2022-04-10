package roon.hitmarker;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public class HitPacket {

    private boolean kill;

    public HitPacket(boolean kill) {
      this.kill = kill;
    }
  
    public static HitPacket encode(HitPacket message, FriendlyByteBuf buffer) {
      buffer.writeBoolean(message.kill);
      return message;
    }

    public static HitPacket decode(FriendlyByteBuf buffer) {
        return new HitPacket(buffer.readBoolean());
    }
  
    // public void handle(HitPacket message, Supplier<NetworkEvent.Context> ctx) {
    //   ctx.get().setPacketHandled(true);
    //   HitmarkerClient.receiveHit(kill);
    // }

    public static void handle(HitPacket message, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() ->
        {
          fromMessage(message);
        }
      );
      ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public static void fromMessage(HitPacket message)
    {
      HitMarkerClient.receiveHit(message.kill);
    }
    
}
