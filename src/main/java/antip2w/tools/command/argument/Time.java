package antip2w.tools.command.argument;

public record Time(TimeUnit unit, int amount) {

    public Time to(TimeUnit unit) {
        return new Time(unit, this.unit.to(this.amount, unit));
    }

    public enum TimeUnit {
        MILLISECONDS("ms", 1),
        TICKS("t", 50),
        SECONDS("s", 1000);

        final String suffix;
        final int multiplier;

        TimeUnit(String suffix, int milliMultiplier) {
            this.suffix = suffix;
            this.multiplier = milliMultiplier;
        }

        public int to(int time, TimeUnit unit) {
            return time * multiplier / unit.multiplier;
        }

        public TimeUnit getSmaller(TimeUnit other) {
            return other.ordinal() < this.ordinal() ? other : this;
        }

        public TimeUnit getLonger(TimeUnit other) {
            return other.ordinal() > this.ordinal() ? other : this;
        }

        public static TimeUnit fromString(String string) throws InvalidTimeUnitException {
            if (string.isEmpty()) return SECONDS;

            for (TimeUnit unit : TimeUnit.values()) {

                if (string.equalsIgnoreCase(unit.suffix)) {
                    return unit;
                }

            }

            throw new InvalidTimeUnitException(string);
        }
    }

    public static class InvalidTimeUnitException extends Exception {

        public InvalidTimeUnitException(String string) {
            super(string);
        }

    }

}
