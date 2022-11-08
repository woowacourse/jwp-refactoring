package kitchenpos.domain.ordertable;

import java.util.List;

public interface TableValidator {

    void validateUnGroupCondition(List<Long> orderTableIds);

    void validateUnGroupCondition(Long orderTableId);
}
