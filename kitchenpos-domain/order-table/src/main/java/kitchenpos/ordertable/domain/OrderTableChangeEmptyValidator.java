package kitchenpos.ordertable.domain;

import kitchenpos.common.domain.ValidResult;

public interface OrderTableChangeEmptyValidator {

    ValidResult validate(Long orderTableId);
}
