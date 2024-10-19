package coolrex.bookofdragons.registry;

import coolrex.bookofdragons.BookOfDragons;
import net.minecraft.client.KeyMapping;

import java.util.ArrayList;
import java.util.List;

public class BODKeyBindings {
    public static final List<KeyMapping> LIST = new ArrayList<>();
    public static final KeyMapping DRAGON_DESCEND = register("dragonDescend", 91);

    private static KeyMapping register(String name, int key) {
        KeyMapping keyBinding = new KeyMapping("key." + name, key, "key.categories." + BookOfDragons.MOD_ID);
        LIST.add(keyBinding);
        return keyBinding;
    }
}
