package david.security.objects;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class RayTracer {

    public static List<Block> rayTraceBlocks(Location start, Vector direction, double maxDistance) {
        List<Block> hits = new ArrayList<>();

        // Calculate the step increment for each axis
        double xStep = direction.getX();
        double yStep = direction.getY();
        double zStep = direction.getZ();

        // Normalize the direction vector
        double length = Math.sqrt(xStep * xStep + yStep * yStep + zStep * zStep);
        xStep /= length;
        yStep /= length;
        zStep /= length;

        // Set the current position and distance traveled
        Location pos = start.clone();
        double distance = 0;

        // Iterate through the blocks between the start and maxDistance
        while (distance < maxDistance) {
            // Calculate the next block to check
            int x = (int) Math.floor(pos.getX());
            int y = (int) Math.floor(pos.getY());
            int z = (int) Math.floor(pos.getZ());
            Block block = Objects.requireNonNull(pos.getWorld()).getBlockAt(x, y, z);

            // Check if the block is solid
            if (block.getType().isSolid()) {
                // Add the block to the list of hits and continue to the next iteration
                hits.add(block);
            }

            // Increment the distance traveled and the current position
            distance += Math.sqrt(xStep * xStep + yStep * yStep + zStep * zStep);
            pos.add(new Vector(xStep, yStep, zStep));
        }

        return hits;
    }

}
