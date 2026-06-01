package sure.objects;

import sure.Game;

public class GameObject {
    public GameObject() {
        Game.use(this);
    }

    public void delete() {
        Game.remove(this);
    }
}
