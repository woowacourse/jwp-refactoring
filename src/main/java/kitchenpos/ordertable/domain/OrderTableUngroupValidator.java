package kitchenpos.ordertable.domain;

import java.util.List;
import kitchenpos.common.domain.ValidResult;

public interface OrderTableUngroupValidator {

    ValidResult validate(List<Long> orderTableIds);
}
