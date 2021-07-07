package com.curtisnewbie.module.ioc.config;

/**
 * Type's log that may be muted
 * <p>
 * default method is provided, but it doesn't guarantee such operation will have any effect, user may invoke {@link
 * #canMuteLog()} to see if log muting is supported.
 * </p>
 * <p>
 * If a class implements this interface, the implementation class should either override all the methods or not override
 * any method at all, such that these methods' behaviours are consistent.
 * </p>
 *
 * @author yongjie.zhuang
 */
public interface LogMutable {

    /**
     * Check if current type support muting its log
     */
    default boolean canMuteLog() {
        return false;
    }

    /**
     * Mute the log
     */
    default void muteLog() {
        // by default it does nothing
    }

    /**
     * Check whether the log is actually muted
     */
    default boolean isLogMuted() {
        return false;
    }
}
