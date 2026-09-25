package sure.basepackage.components;

import java.util.ArrayList;
import java.util.function.Consumer;

import kotlin.Pair;

public abstract class ComponentBundle {
    protected final ArrayList<Pair<Class, Consumer>> components = new ArrayList<>();

    /**
     * Will retrieve every component from the bundle
     * @return
     */
    public final ArrayList<Pair<Class, Consumer>> getComponents() {
        return components;
    }

    /**
     * Will add the component to the bundle
     * @param <T>
     * @param componentInterface - type <T>
     * @param consumer - type <T>
     */
    protected final <T> void add(Class<T> componentInterface, Consumer<T[]> consumer) {
        components.add(new Pair<>(componentInterface, consumer));
    }
}
