package me.zatozalez.wirelessredstone.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;

public class Compat {

    /**
     * Splits Bukkit.getBukkitVersion() on “-” (to strip off any “-R0.1-SNAPSHOT” suffix),
     * then parses the first three numeric components (major.minor.patch).
     *
     * Examples:
     *   "1.20.3-R0.1-SNAPSHOT"  → [1, 20, 3]
     *   "1.20.5"                → [1, 20, 5]
     *   "1.21.0-R0.1"           → [1, 21, 0]
     */
    private static int[] getVersionInts() {
        // e.g. "1.20.4-R0.1-SNAPSHOT" → split on “-” → take “1.20.4”
        String raw = Bukkit.getBukkitVersion().split("-")[0];
        String[] parts = raw.split("\\.");
        int[] v = new int[]{0, 0, 0};

        for (int i = 0; i < parts.length && i < 3; i++) {
            try {
                v[i] = Integer.parseInt(parts[i]);
            } catch (NumberFormatException ex) {
                // (in case BukkitVersion is something weird like “v1.20.x”,
                //  we’ll just leave it at 0 and fall back gracefully)
                v[i] = 0;
            }
        }

        return v;
    }

    /**
     * Returns true if version v (as [major,minor,patch])
     * is >= the target (maj.min.patch).
     *
     * Compare order:
     *   1) major,  2) minor,  3) patch.
     */
    private static boolean isAtLeast(int[] v, int maj, int min, int pat) {
        if (v[0] != maj) {
            return v[0] > maj;
        }
        if (v[1] != min) {
            return v[1] > min;
        }
        return v[2] >= pat;
    }

    /**
     *   - If server version < 1.20.5  ⇒ return Particle.REDSTONE
     *   - Otherwise                ⇒ return Particle.DUST
     */
    public static Particle getDust() {
        int[] ver = getVersionInts();
        if (isAtLeast(ver, 1, 20, 5)) {
            return Particle.DUST;
        } else {
            return Particle.valueOf("REDSTONE");
        }
    }

    /**
     *   - If server version ≤ 1.20.3 ⇒ return Material.GRASS
     *   - If server version ≥ 1.20.4 ⇒ return Material.SHORT_GRASS
     */
    public static Material getGrass() {
        int[] ver = getVersionInts();
        if (isAtLeast(ver, 1, 20, 4)) {
            return Material.SHORT_GRASS;
        } else {
            return Material.valueOf("GRASS");
        }
    }
}
