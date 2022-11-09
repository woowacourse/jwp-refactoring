package kitchenpos.table.application;

import java.util.List;

public interface OrderTableValidator {

    void validate(Long orderTableId);

    void validate(List<Long> orderTableIds);
}
