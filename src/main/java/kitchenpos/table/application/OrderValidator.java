package kitchenpos.table.application;

import java.util.List;

public interface OrderValidator {

    void validateCompletion(final List<Long> orderTableIds);
}
