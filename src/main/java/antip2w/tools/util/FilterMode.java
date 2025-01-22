package antip2w.tools.util;

import net.minecraft.util.StringIdentifiable;

import java.util.Collection;
import java.util.function.BiFunction;

public enum FilterMode implements StringIdentifiable {

    ALLOW_ALL("Allow all", true, (collection, element) -> true),
    ALLOW_ONLY("Allow only", false, Collection::contains),
    DISALLOW_ONLY("Disallow only", false, (collection, element) -> !collection.contains(element)),
    DISALLOW_ALL("Disallow all", true, (collection, element) -> false);

    private final String label;
    public final boolean returnsConstant;
    private final BiFunction<Collection<?>, Object, Boolean> tester;

    FilterMode(String label, boolean returnsConstant, BiFunction<Collection<?>, Object, Boolean> tester) {
        this.label = label;
        this.returnsConstant = returnsConstant;
        this.tester = tester;
    }

    @Override
    public String asString() {
        return label;
    }

    public boolean test(Collection<?> collection, Object element) {
        return tester.apply(collection, element);
    }

}
