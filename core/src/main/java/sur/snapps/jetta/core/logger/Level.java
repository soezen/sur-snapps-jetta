package sur.snapps.jetta.core.logger;

/**
 * User: SUR
 * Date: 26/08/14
 * Time: 20:19
 */
public enum Level {

    /**
     * No logging.
     */
    NONE(0),
    /**
     * only warning messages.
     */
    WARNING(1),
    /**
     * general info and warning messages.
     */
    INFO(2),
    /**
     * debug info, general info and warning messages.
     */
    DEBUG(3),
    /**
     * all information.
     */
    ALL(4);

    private int factor;

    private Level(int factor) {
        this.factor = factor;
    }


    public boolean shouldLogMessageOfLevel(Level level) {
        return factor >= level.factor;
    }
}
