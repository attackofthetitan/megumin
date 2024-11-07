package net.sixunderscore.megumin.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.sixunderscore.megumin.Megumin;
import net.sixunderscore.megumin.item.custom.MeguminStaffItem;

import java.util.function.Function;

public class ModItems {
    public static final Item MEGUMIN_STAFF_ITEM = registerItem("megumin_staff", MeguminStaffItem::new, new Item.Settings().maxCount(1).maxDamage(20));
    public static final Item STAFF_BALL = registerItem("staff_ball", Item::new, new Item.Settings());

    public static Item registerItem(String path, Function<Item.Settings, Item> factory, Item.Settings settings) {
        return Items.register(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Megumin.MOD_ID, path)), factory, settings);
    }

    public static void load() {
        Megumin.LOGGER.info("Loading items for mod: " + Megumin.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(content -> content.add(MEGUMIN_STAFF_ITEM));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content -> content.add(STAFF_BALL));
    }
}
