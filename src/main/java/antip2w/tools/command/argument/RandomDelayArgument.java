package antip2w.tools.command.argument;

import antip2w.tools.util.Util;

public class RandomDelayArgument {

    private Time value;
    private final int minimum;
    private final int maximum;
    private final Time.TimeUnit unit;

    public RandomDelayArgument(Time max) {
        this(new Time(max.unit(), 0), max);
    }

    public RandomDelayArgument(Time min, Time max) {
        this.unit = max.unit().getSmaller(min.unit());
        min = min.to(this.unit);
        max = max.to(this.unit);
        this.minimum = Math.min(min.amount(), max.amount());
        this.maximum = Math.max(min.amount(), max.amount());
        generateNewValue(this.unit);
    }

    public Time generateNewValue(Time.TimeUnit unit) {
        this.value = new Time(unit, Util.RANDOM.nextInt(this.unit.to(minimum, unit), this.unit.to(maximum, unit)));
        return this.value;
    }

    public Time getValue() {
        return this.value;
    }

}
