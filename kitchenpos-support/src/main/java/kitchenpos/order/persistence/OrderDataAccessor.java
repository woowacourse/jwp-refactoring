package kitchenpos.order.persistence;

import java.util.List;
import kitchenpos.order.persistence.dto.OrderDataDto;
import kitchenpos.support.BasicDataAccessor;

public interface OrderDataAccessor extends BasicDataAccessor<OrderDataDto> {

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses);
}
