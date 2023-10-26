package kitchenpos.persistence.specific;

import java.util.List;
import kitchenpos.persistence.BasicDataAccessor;
import kitchenpos.persistence.dto.OrderDataDto;

public interface OrderDataAccessor extends BasicDataAccessor<OrderDataDto> {

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses);
}
