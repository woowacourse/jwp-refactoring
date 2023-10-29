package kitchenpos.domain;

import java.util.List;

public interface OrderTableValidator {

    void validateChangeEmpty(Long orderTableId, TableGroup tableGroup);
    void validateUngroup(List<Long> orderTableIds);

}
