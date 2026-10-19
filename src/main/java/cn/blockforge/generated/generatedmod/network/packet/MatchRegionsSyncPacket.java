package cn.blockforge.generated.generatedmod.network.packet;

import cn.blockforge.generated.generatedmod.client.network.ClientPacketHandler;
import cn.blockforge.generated.generatedmod.map.MapRegion;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

/** 比赛玩法区域下发：只含当前地图对玩家可见的爆破/占领/热点/目标等区域。 */
public final class MatchRegionsSyncPacket {
    private static final int MAX_REGIONS = 64;
    private final List<MapRegion> regions;

    public MatchRegionsSyncPacket(List<MapRegion> regions) {
        this.regions = regions == null ? List.of() : regions;
    }

    public MatchRegionsSyncPacket(FriendlyByteBuf buffer) {
        int count = buffer.readVarInt();
        if (count < 0 || count > MAX_REGIONS) {
            throw new IllegalArgumentException("Invalid match region count");
        }
        var read = new java.util.ArrayList<MapRegion>(count);
        for (int i = 0; i < count; i++) {
            read.add(MapRegionPacket.readRegion(buffer));
        }
        regions = List.copyOf(read);
    }

    public void encode(FriendlyByteBuf buffer) {
        int count = Math.min(MAX_REGIONS, regions.size());
        buffer.writeVarInt(count);
        for (int i = 0; i < count; i++) {
            MapRegionPacket.writeRegion(buffer, regions.get(i));
        }
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientPacketHandler.handle(this));
        context.setPacketHandled(true);
    }

    public List<MapRegion> regions() {
        return regions;
    }
}
