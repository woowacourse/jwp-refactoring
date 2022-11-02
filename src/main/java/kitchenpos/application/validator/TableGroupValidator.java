package kitchenpos.application.validator;

import java.util.function.Supplier;
import kitchenpos.domain.TableGroup;

@FunctionalInterface
public interface TableGroupValidator {

    Supplier<Boolean> validate(final TableGroup tableGroup);
}
