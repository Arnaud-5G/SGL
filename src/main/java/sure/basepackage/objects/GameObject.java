package sure.basepackage.objects;

import sure.basepackage.Window;

public class GameObject {
    public GameObject() {
        Window.get().getRunningGame().use(this);
    }

    public void delete() {
        Window.get().getRunningGame().remove(this);
    }
}
