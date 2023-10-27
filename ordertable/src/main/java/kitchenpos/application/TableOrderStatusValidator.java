package kitchenpos.application;

import java.util.List;

public interface TableOrderStatusValidator {

    void validateIsTableCompleteMeal(Long orderTableId);

    void validateIsTableGroupCompleteMeal(List<Long> orderTableIds);
}
