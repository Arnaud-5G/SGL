package sure.basepackage.objects;

import sure.basepackage.Game;

public class GameObject {
    public GameObject() {
        Game.use(this);
    }

    public void delete() {
        Game.remove(this);
    }
}
