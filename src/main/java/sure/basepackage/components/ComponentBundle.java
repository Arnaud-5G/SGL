package sure.basepackage.components;

import java.util.ArrayList;
import java.util.function.Consumer;

import kotlin.Pair;

public class ComponentBundle {
    private static ComponentBundle bundle;

    protected ComponentBundle(){}

    protected static final ArrayList<Pair<Class, Consumer>> components = new ArrayList<>();

    public static final ComponentBundle getBundle() {
        if (bundle == null) {
            bundle = new ComponentBundle();
        }
        return bundle;
    }

    public final ArrayList<Pair<Class, Consumer>> getComponents() {
        return components;
    }

    protected static final <T> void add(Class<T> componentInterface, Consumer<T[]> consumer) {
        components.add(new Pair<>(componentInterface, consumer));
    }
}
