package sure.basepackage.components;

public interface UsesFocus {
    /**
     * This will be called every frame to check if this element should be focused
     * @return boolean
     * @apiNote the result of this function will not indicated if the element truly is focused as this function is essentially only a recommendation to the component handler to check
     */
    public boolean shouldBeFocused();

    /**
     * This will be called every frame to check if this element should not be focused
     * @return boolean
     * @apiNote the result of this function will indicate that this element will not be focused
     */
    public boolean shouldNotBeFocused();

    /**
     * Indicates whether or not this object is focused
     */
    public void isFocused();
}
