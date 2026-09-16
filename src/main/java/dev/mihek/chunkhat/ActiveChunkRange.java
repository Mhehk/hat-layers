package dev.mihek.chunkhat;

/** Horizontal chunk range: height and line of sight deliberately do not matter. */
public final class ActiveChunkRange {
    private ActiveChunkRange() {}

    public static boolean contains(int centerX, int centerZ, int targetX, int targetZ, int viewDistance) {
        return viewDistance >= 0
                && Math.abs((long) targetX - centerX) <= viewDistance
                && Math.abs((long) targetZ - centerZ) <= viewDistance;
    }
}
