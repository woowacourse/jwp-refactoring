package kitchenpos.domain;

import java.util.List;

public interface TableValidator {

    void validateUnGroupCondition(List<Long> orderTableIds);
}
