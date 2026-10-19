package cn.blockforge.generated.generatedmod.client;

/** Built-in style presets use existing live HUD data and remain fully editable. */
public enum HudPreset {
    COMPETITIVE("综合竞技（推荐）", "完整展示阶段公告、比分、目标、个人、队伍、经济、生命护甲、武器弹药与 C4。"),
    COUNTER_STRIKE("反恐精英 CS2", "顶部紧凑比分、分队人数、金色状态元素。"),
    CROSSFIRE("穿越火线", "宽幅顶部比分，青色强调，醒目的击杀播报。"),
    CALL_OF_DUTY("使命召唤20 MWIII", "左上分队比分与进度、轻量战术信息。");

    private final String label;
    private final String description;

    HudPreset(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public String label() { return label; }
    public String description() { return description; }

    public void apply(ClientHudLayout.Draft draft, HudContext context) {
        for (var e : java.util.List.copyOf(draft.customElements(context))) {
            if (!e.placement().example().isBlank()) draft.removeCustomElement(context, e.id());
        }
        for (var component : HudContext.BuiltIn.values()) draft.of(context).setBuiltInEnabled(component, false);
        int white = 0xFFF0F2F3, accent = this == COMPETITIVE ? 0xFF78D6A5
                : this == COUNTER_STRIKE ? 0xFFF0CE74
                : this == CROSSFIRE ? 0xFF88DDF1 : 0xFFD5E3A5;
        if (!context.isMatch()) {
            var b = new HudAssemblies.Builder(draft, context, label, 50, 0);
            b.opacity = 75;
            b.block("横幅底板", 0, 48, 360, 72, 0xFF181C20);
            b.block("横幅强调线", 0, 13, 360, 2, accent);
            if (context == HudContext.MATCHING) {
                b.line("匹配状态", "{matching_state}", 0, 29, 340, accent);
                b.condition = "queued";
                b.line("队列", "队列 {queue}/{queue_need}  序位 {position}", 0, 48, 340, white);
                b.line("时间", "已等待 {wait}", 0, 67, 340, white);
                b.condition = "forming";
                b.line("倒计时", "开赛倒计时 {ready} 秒", 0, 48, 340, white);
                b.line("入场提示", "准备进入比赛", 0, 67, 340, white);
            } else {
                b.line("房间", "{room} · {room_mode}", 0, 29, 340, accent);
                b.line("地图", "地图 {map} · {state}", 0, 48, 340, white);
                b.line("成员", "人数 {members}/{room_max} · 房主 {owner}", 0, 67, 340, white);
            }
            return;
        }
        switch (this) {
            case COMPETITIVE -> competitive(draft, context, white, accent);
            case COUNTER_STRIKE -> counterStrike(draft, context, white, accent);
            case CROSSFIRE -> crossfire(draft, context, white, accent);
            case CALL_OF_DUTY -> callOfDuty(draft, context, white, accent);
        }
        addMatchReadouts(draft, context, label, accent, white, 0xFFFF6570);
    }

    private void competitive(ClientHudLayout.Draft draft, HudContext context, int white, int accent) {
        boolean kills = context == HudContext.TEAM_DEATHMATCH;
        String score = kills ? "score_" : "wins_";
        var top = builder(draft, context, 50, 0, 78);
        top.block("顶栏底板", 0, 29, 330, 50, 0xFF10151A);
        teamWing(top, "a", -105, 29, score, 0xFFFF6570, white, kills);
        teamWing(top, "b", 105, 29, score, 0xFF62B6FF, white, kills);
        top.block("计时底板", 0, 29, 76, 50, 0xFF20272D);
        top.text("计时", "{time}", 0, 21, 68, white, 120);
        top.line("回合", "R {round}", 0, 40, 58, accent);
        top.line("规则", kills ? "{mode}  目标 {target}" : "{mode}  先胜 {rounds_to_win}",
                0, 57, 190, 0xFFC8D1D5);
        optionalTeams(top, score, white, kills, 72);
        var vitals = builder(draft, context, 0, 100, 70);
        vitals.block("状态底板", 102, -46, 196, 58, 0xFF10151A);
        vitals.text("生命", "{health}", 35, -55, 52, white, 165);
        vitals.text("护甲", "{armor}", 92, -55, 42, 0xFF62B6FF, 120);
        vitals.progress("生命条", "health_percent", 102, -34, 180, accent);
        weaponPanel(draft, context, 100, 100, white, accent, 72, -105, -48, 202);
        personalPanel(draft, context, 0, 77, white, accent, 64, 140);
        killFeed(draft, context, white, 0xFFFFD477, 100, 36, 74);
    }

    private void counterStrike(ClientHudLayout.Draft draft, HudContext context, int white, int accent) {
        boolean kills = context == HudContext.TEAM_DEATHMATCH;
        String score = kills ? "score_" : "wins_";
        var top = builder(draft, context, 50, 0, 66);
        top.block("紧凑顶栏", 0, 24, 274, 40, 0xFF15191D);
        top.block("A队色块", -93, 24, 86, 40, 0xFF5A332B);
        top.block("B队色块", 93, 24, 86, 40, 0xFF29445B);
        top.text("A队比分", "{" + score + "a}", -68, 23, 34, white, 170);
        top.text("B队比分", "{" + score + "b}", 68, 23, 34, white, 170);
        top.text("A队人数", "{team_a_size}", -112, 23, 24, 0xFFFFB25F, 95);
        top.text("B队人数", "{team_b_size}", 112, 23, 24, 0xFF7AC7FF, 95);
        top.text("计时", "{time}", 0, 19, 72, white, 115);
        top.line("局数", "第 {round} 回合", 0, 34, 72, accent);
        top.line("规则", kills ? "目标 {target}" : "先胜 {rounds_to_win}", 0, 48, 94, white);
        optionalTeams(top, score, white, kills, 62);
        var lower = builder(draft, context, 0, 100, 58);
        lower.text("生命值", "+ {health}", 40, -37, 72, white, 145);
        lower.text("护甲值", "盾 {armor}", 112, -37, 64, white, 105);
        lower.progress("生命条", "health_percent", 88, -23, 148, 0xFFFFB25F);
        weaponPanel(draft, context, 100, 100, white, accent, 58, -102, -38, 196);
        personalPanel(draft, context, 0, 84, white, accent, 48, 132);
        killFeed(draft, context, white, accent, 100, 28, 62);
    }

    private void crossfire(ClientHudLayout.Draft draft, HudContext context, int white, int accent) {
        boolean kills = context == HudContext.TEAM_DEATHMATCH;
        String score = kills ? "score_" : "wins_";
        var top = builder(draft, context, 50, 0, 88);
        top.block("宽幅顶栏", 0, 28, 430, 48, 0xFF111820);
        top.block("A队面板", -145, 28, 138, 48, 0xFF5A242B);
        top.block("B队面板", 145, 28, 138, 48, 0xFF183E5C);
        top.text("A队", "A队", -185, 20, 40, 0xFFFF6B73, 95);
        top.text("A队比分", "{" + score + "a}", -132, 27, 52, white, 190);
        top.text("B队比分", "{" + score + "b}", 132, 27, 52, white, 190);
        top.text("B队", "B队", 185, 20, 40, 0xFF62C4FF, 95);
        top.line("A队人数", "{team_a_size} 人", -181, 38, 48, white);
        top.line("B队人数", "{team_b_size} 人", 181, 38, 48, white);
        top.text("模式", "{mode}", 0, 13, 130, accent, 90);
        top.text("计时", "{time}", 0, 29, 82, white, 140);
        top.line("目标", kills ? "目标 {target}" : "先胜 {rounds_to_win}", 0, 44, 100, accent);
        optionalTeams(top, score, white, kills, 72);
        var vitals = builder(draft, context, 0, 100, 82);
        vitals.block("生命底板", 104, -45, 202, 58, 0xFF111820);
        vitals.text("生命", "HP {health}", 58, -53, 82, white, 145);
        vitals.text("护甲", "AC {armor}", 139, -53, 70, 0xFF71D7F1, 110);
        vitals.progress("生命条", "health_percent", 104, -32, 184, 0xFF5FE0B0);
        weaponPanel(draft, context, 100, 100, white, accent, 82, -112, -46, 218);
        personalPanel(draft, context, 0, 79, white, accent, 72, 150);
        killFeed(draft, context, white, 0xFFFFD66B, 100, 39, 90);
    }

    private void callOfDuty(ClientHudLayout.Draft draft, HudContext context, int white, int accent) {
        boolean kills = context == HudContext.TEAM_DEATHMATCH;
        String progress = kills ? "_progress" : "_wins_progress";
        var objective = builder(draft, context, 0, 4, 52);
        objective.text("模式", "{mode}", 112, 8, 202, white, 92);
        objective.line("回合计时", "{time}  |  回合 {round}", 112, 25, 202, 0xFFC8D1D5);
        for (int i = 0; i < 4; i++) {
            String key = "abcd".substring(i, i + 1);
            objective.condition = i < 2 ? "" : "team_" + key;
            int y = 48 + i * 25;
            int color = teamColor(i);
            objective.block("队伍标记" + key, 20, y, 5, 18, color);
            objective.text("队名" + key, key.toUpperCase(), 37, y, 20, color, 95);
            objective.text("比分" + key, "{" + (kills ? "score_" : "wins_") + key + "}", 64, y, 30, white, 135);
            objective.progress("进度" + key, "team_" + key + progress, 154, y, 142, color);
        }
        objective.condition = "";
        objective.line("胜利目标", kills ? "得分上限 {target}" : "先胜 {rounds_to_win} 回合", 112, 153, 202, accent);
        var vitals = builder(draft, context, 0, 100, 45);
        vitals.text("生命值", "{health}", 43, -42, 58, white, 170);
        vitals.text("护甲值", "{armor}", 93, -42, 40, 0xFFBBD97D, 105);
        vitals.progress("生命条", "health_percent", 76, -25, 126, accent);
        weaponPanel(draft, context, 100, 100, white, accent, 44, -104, -40, 198);
        personalPanel(draft, context, 100, 78, white, accent, 42, -128);
        killFeed(draft, context, white, accent, 100, 31, 48);
    }

    private HudAssemblies.Builder builder(ClientHudLayout.Draft draft, HudContext context,
                                           int x, int y, int opacity) {
        var builder = new HudAssemblies.Builder(draft, context, label, x, y);
        builder.opacity = opacity;
        return builder;
    }

    private static void teamWing(HudAssemblies.Builder b, String key, int x, int y,
                                 String score, int color, int white, boolean kills) {
        b.block(key.toUpperCase() + "队底板", x, y, 124, 50, 0xFF1B2228);
        b.block(key.toUpperCase() + "队色条", x, y - 24, 124, 3, color);
        b.text(key.toUpperCase() + "队名", key.toUpperCase() + "队", x - 37, y - 8, 40, color, 88);
        b.text(key.toUpperCase() + "队比分", "{" + score + key + "}", x + 21, y - 1, 42, white, 180);
        b.line(key.toUpperCase() + "队人数", "{team_" + key + "_size} 人", x - 28, y + 13, 54, white);
        b.progress(key.toUpperCase() + "队进度", "team_" + key + (kills ? "_progress" : "_wins_progress"), x + 25, y + 15, 44, color);
    }

    private static void optionalTeams(HudAssemblies.Builder b, String score, int white,
                                      boolean kills, int y) {
        for (int i = 2; i < 4; i++) {
            String key = i == 2 ? "c" : "d";
            int x = i == 2 ? -76 : 76;
            int color = teamColor(i);
            b.condition = "team_" + key;
            b.block(key.toUpperCase() + "队附加底板", x, y, 138, 22, 0xFF182027);
            b.text(key.toUpperCase() + "队附加比分", key.toUpperCase() + " {" + score + key + "}", x - 22, y, 64, color, 92);
            b.line(key.toUpperCase() + "队附加人数", "{team_" + key + "_size} 人", x + 41, y, 48, white);
            b.progress(key.toUpperCase() + "队附加进度", "team_" + key + (kills ? "_progress" : "_wins_progress"), x, y + 10, 122, color);
        }
        b.condition = "";
    }

    private void weaponPanel(ClientHudLayout.Draft draft, HudContext context, int anchorX, int anchorY,
                             int white, int accent, int opacity, int x, int y, int width) {
        var weapon = builder(draft, context, anchorX, anchorY, opacity);
        weapon.block("武器底板", x, y, width, 55, 0xFF10151A);
        weapon.text("武器名称", "{held_weapon}", x, y - 12, width - 16, 0xFFC8D1D5, 92);
        weapon.text("弹药", "{held_ammo}", x + width / 4, y + 7, width / 2, white, 155);
        weapon.text("资金", "${my_match_money}", x - width / 4, y + 7, width / 2 - 12, accent, 100);
        weapon.progress("武器耐久", "held_durability", x, y + 25, width - 16, accent);
    }

    private void personalPanel(ClientHudLayout.Draft draft, HudContext context, int anchorX, int anchorY,
                               int white, int accent, int opacity, int x) {
        var personal = builder(draft, context, anchorX, anchorY, opacity);
        personal.block("个人统计底板", x, 0, 250, 44, 0xFF10151A);
        personal.line("击杀阵亡", "K {my_kills}  D {my_deaths}  KD {my_kd}", x, -10, 234, white);
        personal.line("伤害统计", "伤害 {my_damage} / 承伤 {my_damage_taken}", x, 7, 234, 0xFFC8D1D5);
        personal.line("队伍击杀", "{team}  队伍击杀 {match_kills_sum}", x, 20, 234, accent);
    }

    private void killFeed(ClientHudLayout.Draft draft, HudContext context, int white, int accent,
                          int anchorX, int anchorY, int opacity) {
        var feed = builder(draft, context, anchorX, anchorY, opacity);
        feed.condition = "feed";
        feed.block("击杀播报底板", -116, 0, 224, 22, 0xFF11161B);
        feed.block("击杀播报标记", -227, 0, 3, 22, accent);
        feed.line("击杀播报", "{killer}  >  {victim}", -116, 0, 208, white);
    }

    private static int teamColor(int index) {
        return switch (index) {
            case 0 -> 0xFFFF6570;
            case 1 -> 0xFF62B6FF;
            case 2 -> 0xFF69D391;
            default -> 0xFFFFCC66;
        };
    }

    private static void addMatchReadouts(ClientHudLayout.Draft draft, HudContext context,
                                         String example, int accent, int white, int red) {
        for (var event : cn.blockforge.generated.generatedmod.match.MatchHudEventType.values()) {
            HudAssemblies.addEventComponent(draft, context, event);
        }
    }

    public void apply(ClientHudLayout.Mutable v, HudContext context) {
        for (HudContext.BuiltIn component : HudContext.BuiltIn.values()) {
            v.setBuiltInEnabled(component, true);
        }
        v.scoreVisible = v.textVisible = v.feedVisible = context.isMatch();
        v.bannerVisible = !context.isMatch();
        v.scoreXPercent = 50; v.scoreYPercent = 2;
        v.textXPercent = 50; v.textYPercent = 78; v.textWidth = 220;
        v.feedXPercent = 100; v.feedYPercent = 42;
        v.bannerXPercent = 50; v.bannerYPercent = 12;
        v.scoreHeaderTemplate = "{mode} · {phase}";
        v.scoreTeamATemplate = "A队 {score_a}";
        v.scoreTeamBTemplate = "B队 {score_b}";
        v.scoreTimerTemplate = "{time}";
        v.scoreDetailsTemplate = "回合 {round}    目标 {target}";
        v.textTemplate = "{hint}";
        v.bannerLineOneTemplate = context == HudContext.ROOM ? "{room_line1}" : "{matching_line1}";
        v.bannerLineTwoTemplate = context == HudContext.ROOM ? "{room_line2}" : "{matching_line2}";
        switch (this) {
            case COMPETITIVE -> {
                v.scoreWidth = 340; v.scoreScalePercent = 92; v.scoreOpacityPercent = 78;
                v.textScalePercent = 92; v.textOpacityPercent = 45;
                v.feedScalePercent = 92; v.feedOpacityPercent = 72;
                v.bannerScalePercent = 92; v.bannerOpacityPercent = 78;
                v.scoreColor = v.bannerColor = 0x78D6A5;
                v.textColor = 0xE8ECEF; v.feedColor = 0xF0CE74;
                v.feedTemplate = "{killer} 击杀 {victim}";
            }
            case COUNTER_STRIKE -> {
                v.scoreWidth = 280; v.scoreScalePercent = 85; v.scoreOpacityPercent = 65;
                v.textXPercent = 0; v.textScalePercent = 85; v.textOpacityPercent = 35;
                v.feedScalePercent = 85; v.feedOpacityPercent = 65;
                v.bannerScalePercent = 85; v.bannerOpacityPercent = 65;
                v.scoreColor = v.textColor = v.bannerColor = 0xE5C56C;
                v.feedColor = 0xE8E8E8;
                v.feedTemplate = "{killer} > {victim}";
            }
            case CROSSFIRE -> {
                v.scoreWidth = 400; v.scoreScalePercent = 100; v.scoreOpacityPercent = 90;
                v.textScalePercent = 100; v.textOpacityPercent = 75;
                v.feedScalePercent = 100; v.feedOpacityPercent = 85;
                v.bannerScalePercent = 100; v.bannerOpacityPercent = 90;
                v.scoreColor = v.bannerColor = 0x67D4ED;
                v.textColor = 0xF0F0F0; v.feedColor = 0xF0CE70;
                v.feedTemplate = "{killer} 击杀 {victim}";
            }
            case CALL_OF_DUTY -> {
                v.scoreXPercent = 0; v.scoreWidth = 240;
                v.scoreScalePercent = 85; v.scoreOpacityPercent = 45;
                v.textXPercent = 0; v.textScalePercent = 85; v.textOpacityPercent = 25;
                v.feedScalePercent = 85; v.feedOpacityPercent = 40;
                v.bannerXPercent = 0; v.bannerScalePercent = 85; v.bannerOpacityPercent = 45;
                v.scoreColor = v.feedColor = 0xF1F3F2;
                v.textColor = v.bannerColor = 0xA9CF80;
                v.feedTemplate = "{killer} / {victim}";
            }
        }
    }
}
