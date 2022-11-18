package kitchenpos.table.validator;

import java.util.function.Supplier;
import kitchenpos.table.TableGroup;

@FunctionalInterface
public interface TableGroupValidator {

    Supplier<Boolean> validate(final TableGroup tableGroup);
}
