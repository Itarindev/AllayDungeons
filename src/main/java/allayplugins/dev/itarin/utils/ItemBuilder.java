package allayplugins.dev.itarin.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class ItemBuilder {

    private ItemStack itemStack;
    private ItemMeta itemMeta;

    public ItemBuilder(ItemStack item) {
        itemStack = item;
        itemMeta = item.getItemMeta();
    }

    public ItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    public ItemBuilder(Material material, int quantity) {
        this(new ItemStack(material, quantity));
    }

    public ItemBuilder(Material material, int quantity, int data) {
        this(new ItemStack(material, quantity, (short) data));
    }

    public ItemBuilder(String texture) {
        Material skullMaterial = getSkullMaterial();
        ItemStack skullItem = new ItemStack(skullMaterial, 1);
        if (skullMaterial.toString().contains("PLAYER_HEAD")) {
            skullItem.setDurability((short) 0);
        } else {
            skullItem.setDurability((short) 3);
        }

        if (texture == null || texture.isEmpty()) {
            itemStack = skullItem;
            return;
        }

        if (!texture.startsWith("http://textures.minecraft.net/texture/"))
            texture = "http://textures.minecraft.net/texture/" + texture;

        SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();
        GameProfile profile = new GameProfile(UUID.nameUUIDFromBytes(texture.getBytes()), null);
        String value = Base64.encodeBase64String(String.format("{textures:{SKIN:{url:\"%s\"}}}", texture).getBytes());
        profile.getProperties().put("textures", new Property("textures", value));

        try {
            Field field = skullMeta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(skullMeta, profile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        skullItem.setItemMeta(skullMeta);
        this.itemStack = skullItem;
        this.itemMeta = skullMeta;
    }

    private Material getSkullMaterial() {
        try {
            return Material.valueOf("PLAYER_HEAD");
        } catch (IllegalArgumentException e) {
            return Material.valueOf("SKULL_ITEM");
        }
    }

    public ItemBuilder setQuantity(int quantity) {
        itemStack.setAmount(quantity);
        return this;
    }

    public ItemBuilder setName(String displayName) {
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        itemMeta.setLore(lore.stream().map(l -> ChatColor.translateAlternateColorCodes('&', l)).collect(Collectors.toList()));
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        itemMeta.setLore(Arrays.stream(lore).map(l -> ChatColor.translateAlternateColorCodes('&', l)).collect(Collectors.toList()));
        return this;
    }

    public ItemBuilder addLore(List<String> lore) {
        List<String> list = itemMeta.getLore() == null ? new ArrayList<>() : itemMeta.getLore();
        lore.stream().map(l -> ChatColor.translateAlternateColorCodes('&', l)).forEach(list::add);

        itemMeta.setLore(list);
        return this;
    }

    public ItemBuilder addLore(String... lore) {
        List<String> list = itemMeta.getLore() == null ? new ArrayList<>() : itemMeta.getLore();
        Arrays.stream(lore).map(l -> ChatColor.translateAlternateColorCodes('&', l)).forEach(list::add);

        itemMeta.setLore(list);
        return this;
    }

    public ItemBuilder setGlow(boolean glow) {
        if (glow) {
            itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder addEnchants(List<Enchantment> enchantments) {
        enchantments.forEach(enchantment -> itemMeta.addEnchant(enchantment, 1, true));
        return this;
    }

    public ItemBuilder removeEnchant(Enchantment enchantment) {
        itemMeta.removeEnchant(enchantment);
        return this;
    }

    public ItemBuilder addFlags(ItemFlag... flags) {
        itemMeta.addItemFlags(flags);
        return this;
    }

    public ItemBuilder setFlags() {
        addFlags(ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder removeFlags(ItemFlag... flags) {
        itemMeta.removeItemFlags(flags);
        return this;
    }

    public ItemBuilder setUnbreakable(boolean value) {
        itemMeta.spigot().setUnbreakable(value);
        return this;
    }

    public ItemBuilder setOwner(String owner) {
        SkullMeta skullMeta = (SkullMeta) itemMeta;
        skullMeta.setOwner(owner);

        itemMeta = skullMeta;
        return this;
    }

    public ItemBuilder setDurability(short amount) {
        itemStack.setDurability(amount);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
