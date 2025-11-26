package sure.utils;

public class Memory {
    private Memory() {}

    /**
     * @return the amount of memory used by this program in bytes
     */
    public static long inUseMemory() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }
}
