package sure.basepackage.renderers;

import java.util.ArrayList;
import java.util.function.Supplier;

import kotlin.Pair;
import sure.basepackage.renderers.Sprites.SpriteSheet;

public class Animator extends Texture {
    public static final Animation DEFAULT_ANIMATION = new Animation(new int[] {0});
    protected SpriteSheet spriteSheet;
    protected ArrayList<Pair<Supplier<Boolean>, Animation>> animations = new ArrayList<Pair<Supplier<Boolean>, Animation>>();
    protected Animation currentAnimation;

    public Animator(SpriteSheet spriteSheet) {
        super(spriteSheet.get(0).getTextureID(), spriteSheet.get(0).getUvcoords());
        this.spriteSheet = spriteSheet;
        currentAnimation = DEFAULT_ANIMATION;
        currentAnimation.start();
    }
    
    /**
     * Adds the animation in an ordered list that will select the first animation whose condition is true
     * @param condition
     * @param animation
     */
    public void addAnimation(Supplier<Boolean> condition, Animation animation) {
        this.animations.add(new Pair<Supplier<Boolean>,Animation>(condition, animation));
    }

    public Animation getCurrentAnimation() {
        for(Pair<Supplier<Boolean>, Animation> animation : animations) {
            if (animation.getFirst().get()) {
                return animation.getSecond();
            }
        }

        return DEFAULT_ANIMATION;
    }

    @Override
    public float[][] getUvcoords() {
        Animation tempAnim = getCurrentAnimation();
        if (tempAnim.equals(currentAnimation)) {
            return spriteSheet.get(currentAnimation.getCurrentIndex()).getUvcoords();
        } else {
            currentAnimation.stop();
            tempAnim.start();
            currentAnimation = tempAnim;
        }

        return spriteSheet.get(currentAnimation.getCurrentIndex()).getUvcoords();
    }
}
