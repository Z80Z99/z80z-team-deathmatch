package cn.blockforge.generated.generatedmod.client;

import cn.blockforge.generated.generatedmod.map.MapDefinition;
import cn.blockforge.generated.generatedmod.map.MapEditorView;
import cn.blockforge.generated.generatedmod.map.MapRegion;
import cn.blockforge.generated.generatedmod.map.MapTool;
import cn.blockforge.generated.generatedmod.map.MapBrushMode;
import net.minecraft.core.BlockPos;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MapToolHudTest {
    @Test void completeBombMapReportsAllBuiltInModesReady() {
        MapRegion bomb = MapRegion.custom("site_a", "A 点", MapRegion.Type.BOMB,
                new MapDefinition.Region(new BlockPos(0, 60, 0), new BlockPos(5, 65, 5)));
        List<String> lines = MapToolHud.summaryLines(view(true, true, 2, 2, List.of(bomb)));
        assertTrue(lines.contains("团队竞技：可用"));
        assertTrue(lines.contains("爆破模式：可用"));
        assertTrue(lines.contains("歼灭竞技：可用"));
        assertTrue(lines.stream().anyMatch(line -> line.contains("爆破 1（有效 1）")));
    }

    @Test void incompleteBombMapListsEveryMissingElement() {
        List<String> lines = MapToolHud.summaryLines(view(true, false, 1, 0, List.of()));
        assertTrue(lines.contains("团队竞技：缺少重置区域、B队复活点"));
        assertTrue(lines.contains("爆破模式：缺少重置区域、B队复活点、有效爆破区"));
    }

    private static MapEditorView view(boolean bounds, boolean reset, int teamA, int teamB,
                                      List<MapRegion> regions) {
        MapEditorView.RegionData present = new MapEditorView.RegionData(true, 0, 0, 0, 10, 10, 10);
        MapEditorView.RegionData absent = MapEditorView.RegionData.empty();
        return new MapEditorView(true, "test", "测试地图", "minecraft:overworld",
                bounds ? present : absent, reset ? present : absent, absent, absent,
                teamA, teamB, 1, "ready", true, true, false, false,
                List.of("test"), List.of("test"), "", 0, "", false, 0, 0,
                regions, MapTool.BOUNDS, MapBrushMode.REGION, "bounds", 64);
    }
}
