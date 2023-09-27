package me.juancarloscp52.bedrockify.common.item;

import net.minecraft.item.Items;

public final class StackableCakes {
    public static void register() {
        ItemUtility.setMaxStackSize(Items.CAKE, 64);
    }
}
