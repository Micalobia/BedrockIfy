package me.juancarloscp52.bedrockify.common.item;

import me.juancarloscp52.bedrockify.mixin.common.core.accessors.ItemAccessor;
import net.minecraft.item.Item;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ItemUtility {
    public static int setMaxStackSize(Item item, int size) {
        if (!(item instanceof ItemAccessor accessor)) return item.getMaxCount();
        return setTemplate(size, item::getMaxCount, accessor::setMaxCount);
    }

    private static <T> T setTemplate(T value, Supplier<T> supplier, Consumer<T> consumer) {
        T old = supplier.get();
        consumer.accept(value);
        return old;
    }
}
