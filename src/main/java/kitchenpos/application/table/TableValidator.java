package kitchenpos.application.table;

import java.util.List;

public interface TableValidator {

    void checkOrderTableStatus(final Long orderTableId);

    void checkOrderStatus(final List<Long> orderTableIds);
}
