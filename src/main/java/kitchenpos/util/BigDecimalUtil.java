package kitchenpos.util;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Supplier;

public class BigDecimalUtil {

    public static ValidatingValue valueForCompare(final BigDecimal target) {
        return new ValidatingValue(target);
    }

    public static BigDecimal sum(final List<BigDecimal> bigDecimals) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final BigDecimal bigDecimal : bigDecimals) {
            sum = sum.add(bigDecimal);
        }
        return sum;
    }

    public static class ValidatingValue {

        private BigDecimal value;

        private ValidatingValue(final BigDecimal value) {
            this.value = value;
        }

        public void throwIfNegative(final Supplier<RuntimeException> supplier) {
            throwIfLessThan(BigDecimal.ZERO, supplier);
        }

        public void throwIfLessThan(final BigDecimal target, final Supplier<RuntimeException> supplier) {
            if (this.value.compareTo(target) < 0) {
                throw supplier.get();
            }
        }

        public void throwIfBiggerThan(final BigDecimal target, final Supplier<RuntimeException> supplier) {
            if (this.value.compareTo(target) > 0) {
                throw supplier.get();
            }
        }
    }
}
