package sure.basepackage.components;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.function.Consumer;

import kotlin.Pair;

import sure.basepackage.Window;

public class HandleComponents {
    protected ArrayList<Pair<Class, Consumer>> components = new ArrayList<>();

    /**
     * Will execute all added components should only be called by the internal game logic
     */
    public void executeComponents() {
        for (Pair<Class, Consumer> component : components) {
            executeComponent(component);
        }
    }

    protected <T> void executeComponent(Pair<Class, Consumer> component) {
        // casts the component parts to the correct class
        Class<T> type = (Class<T>) component.getFirst();
        Consumer<T[]> consumer = (Consumer<T[]>) component.getSecond();

        // obtains a list of raw objects that have the correct type
        Object[] rawObjects = Window.get().getRunningGame().getGameObjects(type).toArray();

        // casts the array elements to the correct type
        T[] typedArray = (T[]) Array.newInstance(type, rawObjects.length);
        System.arraycopy(rawObjects, 0, typedArray, 0, rawObjects.length);

        // execute the component
        consumer.accept(typedArray);
    }

    /**
     * This method is used to add component scripts to interfaces.
     * @apiNote This process is not reversible at runtime.
     * @param componentInterface - an interface
     * @param consumer - the script to be run every frame on the selected objects
     */
    public final <T> void addComponent(Class<T> componentInterface, Consumer<T[]> consumer) {
        components.add(new Pair<>(componentInterface, consumer));
    }

    /**
     * This method is used to add component scripts to interfaces.
     * @apiNote This process is not reversible at runtime.
     * @param bundle - a bundle of components
     */
    public final void addComponent(ComponentBundle bundle) {
        for (Pair<Class, Consumer> component : bundle.getComponents()) {
            components.add(component);
        }
    }
}
