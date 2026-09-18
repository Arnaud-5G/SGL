package sure.basepackage.components;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.function.Consumer;

import kotlin.Pair;

import sure.basepackage.Window;

public abstract class HandleComponents {
    private ArrayList<Pair<Class, Consumer>> components = new ArrayList<>();

    public abstract void initializeComponents();

    public void executeComponents() {
        handleComponents();
    }

    
    private void handleComponents() {
        for (Pair<Class, Consumer> component : components) {
            executeComponent(component);
        }
    }

    private <T> void executeComponent(Pair<Class, Consumer> component) {
        Class<T> type = (Class<T>) component.getFirst();
        Consumer<T[]> consumer = (Consumer<T[]>) component.getSecond();

        Object[] rawObjects = Window.get().getRunningGame().getGameObjects(type).toArray();

        T[] typedArray = (T[]) Array.newInstance(type, rawObjects.length);
        System.arraycopy(rawObjects, 0, typedArray, 0, rawObjects.length);

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

}
