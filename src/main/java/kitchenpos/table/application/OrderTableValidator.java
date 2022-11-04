package kitchenpos.table.application;

import java.util.List;

public interface OrderTableValidator {

    void validateCompletionStatus(Long orderTableId);

    void validateAllCompletionStatus(List<Long> orderTableIds);
}
