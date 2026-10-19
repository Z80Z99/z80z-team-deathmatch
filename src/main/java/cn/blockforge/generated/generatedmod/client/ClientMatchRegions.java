package cn.blockforge.generated.generatedmod.client;

import cn.blockforge.generated.generatedmod.map.MapDefinition;
import cn.blockforge.generated.generatedmod.map.MapRegion;
import cn.blockforge.generated.generatedmod.network.packet.MatchRegionsSyncPacket;

import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * 当前比赛的玩法区域客户端缓存。除原始 MapRegion 外，
 * 同时把每个区域展开成不含 Minecraft 类型的纯值视图（{@link RegionView}），
 * 供第三方模组（如小地图）安全读取——避免调用方直接接触方块坐标类。
 * 服务器只下发允许对局显示的区域：观战出生点、重置区等不会出现。
 */
public final class ClientMatchRegions {
    /** 不下发的类型：出生点（含观战相关的点）、边界与重置区。 */
    private static final Set<String> GAMEPLAY_TYPES = Set.of("bomb", "capture", "hotspot", "objective", "custom", "other");

    /** 一个可绘制区域：名称、颜色与一组长方体（min/max 各三维）。 */
    public record RegionBox(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
    }

    public record RegionView(String name, String type, int color, String appearance,
                             int displayRange, List<RegionBox> boxes) {
    }

    private static List<MapRegion> regions = List.of();
    private static List<RegionView> views = List.of();
    private static int revision;

    private ClientMatchRegions() {
    }

    public static void apply(MatchRegionsSyncPacket packet) {
        regions = packet.regions();
        views = regions.stream().map(ClientMatchRegions::toView).toList();
        revision++;
    }

    public static void clear() {
        regions = List.of();
        views = List.of();
        revision++;
    }

    public static List<MapRegion> regions() {
        return regions;
    }

    /** 纯值视图，第三方模组反射读取用。 */
    public static List<RegionView> views() {
        return views;
    }

    public static int revision() {
        return revision;
    }

    private static RegionView toView(MapRegion region) {
        var boxes = new java.util.ArrayList<RegionBox>();
        appendBox(boxes, region.region());
        for (MapDefinition.Region part : region.region().parts()) {
            appendBox(boxes, part);
        }
        return new RegionView(region.displayName(),
                region.type() == null ? "custom" : region.type().id(),
                region.color(),
                region.appearance() == null ? "always" : region.appearance().id(),
                region.displayRange(),
                List.copyOf(boxes));
    }

    private static void appendBox(List<RegionBox> boxes, MapDefinition.Region box) {
        if (box == null || box.min() == null || box.max() == null) {
            return;
        }
        boxes.add(new RegionBox(
                Math.min(box.min().getX(), box.max().getX()),
                Math.min(box.min().getY(), box.max().getY()),
                Math.min(box.min().getZ(), box.max().getZ()),
                Math.max(box.min().getX(), box.max().getX()),
                Math.max(box.min().getY(), box.max().getY()),
                Math.max(box.min().getZ(), box.max().getZ())));
    }

    /** 服务器侧过滤：只允许下发给客户端的玩法区域。 */
    public static boolean clientVisible(MapRegion region) {
        if (region == null || region.type() == null || !region.visibleInMatch()) {
            return false;
        }
        String typeId = region.type().id().toLowerCase(Locale.ROOT);
        if (!GAMEPLAY_TYPES.contains(typeId)) {
            return false;
        }
        String appearance = region.appearance() == null ? "always" : region.appearance().id();
        return appearance.equals("always") || appearance.equals("nearby");
    }
}
