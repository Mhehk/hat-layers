package dev.mihek.chunkhat;

import net.minecraft.client.gui.DrawContext;

/** Small, resource-pack-independent asterisk, drawn only by the TAB head hook. */
public final class TabHatMarker {
    // Five-pixel asterisk, rendered at half scale with a one-pixel dark outline.
    private static final int[] ROWS = {0b00100, 0b10101, 0b01110, 0b10101, 0b00100};

    private TabHatMarker() {}

    public static void draw(DrawContext context, int headX, int headY, int headSize, int headColor) {
        int alpha = headColor & 0xFF000000;
        if (alpha == 0 || headSize < 4) {
            return;
        }
        var matrices = context.getMatrices();
        matrices.pushMatrix();
        try {
            // Keep the complete 3.5 x 3.5 marker inside the top-right of the head.
            // This leaves the name and adjacent TAB rows untouched at every GUI scale.
            matrices.translate(headX + headSize - 3.0f, headY + 1.0f);
            matrices.scale(0.5f, 0.5f);
            context.fill(1, -1, 4, 6, alpha);
            context.fill(-1, 0, 6, 5, alpha);
            int white = alpha | 0x00FFFFFF;
            for (int row = 0; row < ROWS.length; row++) {
                int column = 0;
                while (column < 5) {
                    if ((ROWS[row] & (1 << column)) == 0) {
                        column++;
                        continue;
                    }
                    int start = column++;
                    while (column < 5 && (ROWS[row] & (1 << column)) != 0) {
                        column++;
                    }
                    context.fill(start, row, column, row + 1, white);
                }
            }
        } finally {
            matrices.popMatrix();
        }
    }
}
