package cn.blockforge.generated.generatedmod.client;

import cn.blockforge.generated.generatedmod.map.MapRegion;
import net.minecraft.world.phys.Vec3;

/** Select a real component, never the empty space between composite boxes. */
public final class RegionNamePlacement {
    private RegionNamePlacement() { }

    public static Vec3 position(MapRegion region, double x, double headY, double z) {
        Vec3 result = null;
        double nearest = Double.POSITIVE_INFINITY;
        for (var box : region.region().boxes()) {
            double nearX = clamp(x, box.min().getX(), box.max().getX() + 1D);
            double nearZ = clamp(z, box.min().getZ(), box.max().getZ() + 1D);
            double nearY = region.infiniteHeight() ? headY
                    : clamp(headY, box.min().getY(), box.max().getY() + 1D);
            double distance = new Vec3(nearX, nearY, nearZ).distanceToSqr(new Vec3(x, headY, z));
            if (distance < nearest) {
                nearest = distance;
                result = new Vec3((box.min().getX() + box.max().getX() + 1D) / 2D,
                        region.infiniteHeight() ? headY + 1D
                                : clamp(headY + 1D, box.min().getY() + 0.05D, box.max().getY() + 0.95D),
                        (box.min().getZ() + box.max().getZ() + 1D) / 2D);
            }
        }
        return region.appearance() == MapRegion.Appearance.NEARBY
                && nearest > (double) region.displayRange() * region.displayRange() ? null : result;
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
