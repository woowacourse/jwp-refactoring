package kitchenpos.ordertable.persistence;

import java.util.List;
import kitchenpos.ordertable.persistence.dto.OrderTableDataDto;
import kitchenpos.support.BasicDataAccessor;

public interface OrderTableDataAccessor extends BasicDataAccessor<OrderTableDataDto> {

    List<OrderTableDataDto> findAllByIdIn(List<Long> ids);

    List<OrderTableDataDto> findAllByTableGroupId(Long tableGroupId);
}
