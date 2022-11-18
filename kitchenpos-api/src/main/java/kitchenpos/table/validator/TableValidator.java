package kitchenpos.table.validator;

import java.util.function.Supplier;

@FunctionalInterface
public interface TableValidator {

    Supplier<Boolean> validate(final Long orderTableId);
}
