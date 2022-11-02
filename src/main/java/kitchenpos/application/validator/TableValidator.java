package kitchenpos.application.validator;

import java.util.function.Supplier;
import kitchenpos.domain.OrderTable;

@FunctionalInterface
public interface TableValidator {

    Supplier<Boolean> validate(final OrderTable ordertable);
}
