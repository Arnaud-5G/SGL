package sure.basepackage.components;

import java.util.ArrayList;
import java.util.function.Consumer;

import kotlin.Pair;

public abstract class ComponentBundle {
    protected final ArrayList<Pair<Class, Consumer>> components = new ArrayList<>();

    public final ArrayList<Pair<Class, Consumer>> getComponents() {
        return components;
    }

    protected final <T> void add(Class<T> componentInterface, Consumer<T[]> consumer) {
        components.add(new Pair<>(componentInterface, consumer));
    }
}
