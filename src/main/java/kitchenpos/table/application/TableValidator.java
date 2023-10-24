package kitchenpos.table.application;

import java.util.List;

public interface TableValidator {

    void validateIsTableCompleteMeal(Long orderTableId);

    void validateIsTableGroupCompleteMeal(List<Long> orderTableIds);
}
