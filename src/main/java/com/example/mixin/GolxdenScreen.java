package com.suschunkfinder;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.ChunkPos;

import java.util.List;

public class GolxdenScreen extends Screen {

    private final List<ChunkPos> susChunks;
    private int scrollOffset = 0;
    private static final int ROW_HEIGHT = 14;
    private static final int VISIBLE_ROWS = 12;

    public GolxdenScreen(List<ChunkPos> susChunks) {
        super(Text.literal("Golxden Client"));
        this.susChunks = susChunks;
    }

    @Override
    protected void init() {
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Close"), btn -> this.close())
                .dimensions(this.width / 2 - 50, this.height - 35, 100, 20)
                .build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("▲"), btn -> {
            if (scrollOffset > 0) scrollOffset--;
        }).dimensions(this.width / 2 + 115, this.height / 2 - 20, 20, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("▼"), btn -> {
            if (scrollOffset < Math.max(0, susChunks.size() - VISIBLE_ROWS)) scrollOffset++;
        }).dimensions(this.width / 2 + 115, this.height / 2 + 5, 20, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fillGradient(0, 0, this.width, this.height, 0xCC0A0A0A, 0xCC1A1A1A);

        int panelW = 260;
        int panelH = 230;
        int panelX = this.width / 2 - panelW / 2;
        int panelY = this.height / 2 - panelH / 2;

        context.fill(panelX, panelY, panelX + panelW, panelY + panelH, 0xEE111111);
        context.fill(panelX, panelY, panelX + panelW, panelY + 2, 0xFFFFAA00);
        context.fill(panelX, panelY + panelH - 2, panelX + panelW, panelY + panelH, 0xFFFFAA00);
        context.fill(panelX, panelY, panelX + 2, panelY + panelH, 0xFFFFAA00);
        context.fill(panelX + panelW - 2, panelY, panelX + panelW, panelY + panelH, 0xFFFFAA00);

        String title = "✦ GOLXDEN CLIENT ✦";
        int titleWidth = this.textRenderer.getWidth(title);
        context.drawText(this.textRenderer, title,
                this.width / 2 - titleWidth / 2 + 1, panelY + 10 + 1, 0xFF996600, false);
        context.drawText(this.textRenderer, title,
                this.width / 2 - titleWidth / 2, panelY + 10, 0xFFFFCC33, false);

        String sub = "— Sus Chunk Scanner —";
        int subWidth = this.textRenderer.getWidth(sub);
        context.drawText(this.textRenderer, sub,
                this.width / 2 - subWidth / 2, panelY + 24, 0xFFAA8800, false);

        context.fill(panelX + 10, panelY + 35, panelX + panelW - 10, panelY + 36, 0xFF444400);

        if (susChunks.isEmpty()) {
            String noResult = "No suspicious chunks found!";
            int nrW = this.textRenderer.getWidth(noResult);
            context.drawText(this.textRenderer, noResult,
                    this.width / 2 - nrW / 2, panelY + 50, 0xFF55FF55, false);
        } else {
            String header = "Found " + susChunks.size() + " sus chunk(s):";
            context.drawText(this.textRenderer, header, panelX + 14, panelY + 40, 0xFFFFAA00, false);

            int listStartY = panelY + 54;
            int end = Math.min(scrollOffset + VISIBLE_ROWS, susChunks.size());

            for (int i = scrollOffset; i < end; i++) {
                ChunkPos pos = susChunks.get(i);
                int rowY = listStartY + (i - scrollOffset) * ROW_HEIGHT;

                if ((i % 2) == 0) {
                    context.fill(panelX + 8, rowY - 1,
                            panelX + panelW - 8, rowY + ROW_HEIGHT - 2, 0x22FFAA00);
                }

                context.drawText(this.textRenderer, "◆",
                        panelX + 12, rowY, 0xFFFFAA00, false);

                String line = String.format("Chunk [%d, %d]  →  Block (%d, %d)",
                        pos.x, pos.z, pos.x * 16, pos.z * 16);
                context.drawText(this.textRenderer, line,
                        panelX + 24, rowY, 0xFFEEEEEE, false);
            }

            if (susChunks.size() > VISIBLE_ROWS) {
                String scrollHint = (scrollOffset + VISIBLE_ROWS) + " / " + susChunks.size();
                context.drawText(this.textRenderer, scrollHint,
                        panelX + panelW - 10 - this.textRenderer.getWidth(scrollHint),
                        panelY + panelH - 45, 0xFF887733, false);
            }
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (verticalAmount < 0) {
            if (scrollOffset < Math.max(0, susChunks.size() - VISIBLE_ROWS)) scrollOffset++;
        } else {
            if (scrollOffset > 0) scrollOffset--;
        }
        return true;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}