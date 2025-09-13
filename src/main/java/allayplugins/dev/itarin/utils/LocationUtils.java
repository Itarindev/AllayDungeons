package allayplugins.dev.itarin.utils;

import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationUtils {

    public static String serialize(Location location) {
        val world = location.getWorld().getName();

        val x = location.getX();
        val y = location.getY();
        val z = location.getZ();

        val yaw = location.getYaw();
        val pitch = location.getPitch();

        return world + ";" + x + ";" + y + ";" + z + ";" + yaw + ";" + pitch;
    }

    public static Location deserialize(String location) {
        if (location == null) return null;

        String[] split = location.split(";");
        World world = Bukkit.getWorld(split[0]);

        double x = Double.parseDouble(split[1]);
        double y = Double.parseDouble(split[2]);
        double z = Double.parseDouble(split[3]);
        float yaw = Float.parseFloat(split[4]);
        float pitch = Float.parseFloat(split[5]);

        return new Location(world, x, y, z, yaw, pitch);
    }

}