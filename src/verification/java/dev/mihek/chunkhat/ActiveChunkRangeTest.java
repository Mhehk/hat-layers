package dev.mihek.chunkhat;

/** Standalone regression checks, run by Gradle check/build without a test framework. */
public final class ActiveChunkRangeTest {
    public static void main(String[] args) {
        expect(true, 0, 0, 0, 0, 0);
        expect(false, 0, 0, 1, 0, 0);
        for (int distance : new int[] {2, 4, 8, 12, 16, 24, 32}) {
            expect(true, 0, 0, distance, distance, distance);
            expect(true, -20, -30, -20 - distance, -30 - distance, distance);
            expect(false, 0, 0, distance + 1, 0, distance);
            expect(false, 0, 0, 0, -distance - 1, distance);
        }
        // A setting change takes effect without stale state or a fixed 12-chunk limit.
        expect(false, 0, 0, 15, 0, 8);
        expect(true, 0, 0, 15, 0, 16);
        expect(false, 0, 0, 15, 0, 4);
        expect(false, Integer.MIN_VALUE, 0, Integer.MAX_VALUE, 0, 32);
        expect(false, 0, 0, 0, 0, -1);
        System.out.println("35 chunk range checks passed.");
    }

    private static void expect(boolean expected, int cx, int cz, int x, int z, int distance) {
        if (ActiveChunkRange.contains(cx, cz, x, z, distance) != expected) {
            throw new AssertionError("Unexpected chunk range result: " + cx + "," + cz
                    + " -> " + x + "," + z + " at distance " + distance);
        }
    }
}
