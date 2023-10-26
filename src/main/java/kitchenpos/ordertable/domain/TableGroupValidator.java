package kitchenpos.ordertable.domain;

import java.util.List;

public interface TableGroupValidator {

    void validateCreate(final List<OrderTable> orderTables, final int orderTableSize,
                        final int foundOrderTableSize);

    void validateUnGroup(final List<OrderTable> orderTables);
}
