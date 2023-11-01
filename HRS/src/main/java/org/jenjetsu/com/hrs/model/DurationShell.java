package org.jenjetsu.com.hrs.model;

import java.time.Duration;

public class DurationShell {

    public static DurationShell ZERO = new DurationShell(Duration.ZERO);
    public static DurationShell INFINITY = new DurationShell(Duration.ofMillis(Long.MAX_VALUE));

    private Duration duration;

    private DurationShell(Duration duration) {
        this.duration = duration;
    }

    public Long getSeconds() {
        return this.duration.toSeconds();
    }

    public Long getMinutes() {
        return this.duration.toMinutes();
    }

    public Long getMinutesCeil() {
        return (long) Math.ceil(this.duration.getSeconds() / 60.0);
    }

    public void changeDuration(Duration duration) {
        this.duration = duration;
    }

    public void minusSeconds(Long seconds) {
        this.duration = this.duration.minusSeconds(seconds);
    }

    public void minuseMinutes(Long minutes) {
        this.duration = this.duration.minusMinutes(minutes);
    }

    public void clearToZero() {
        this.duration = Duration.ZERO;
    }

    public static DurationShell ofSeconds(Long seconds) {
        return new DurationShell(Duration.ofSeconds(seconds));
    }

    public static DurationShell ofMinutes(Long minutes) {
        return new DurationShell(Duration.ofMinutes(minutes));
    }

    public static DurationShell ofHours(Long hours) {
        return new DurationShell(Duration.ofHours(hours));
    }

    public static DurationShell ofDays(Long days) {
        return new DurationShell(Duration.ofDays(days));
    }
}
