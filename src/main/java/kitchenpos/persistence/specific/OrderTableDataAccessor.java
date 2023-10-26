package kitchenpos.persistence.specific;

import java.util.List;
import kitchenpos.persistence.BasicDataAccessor;
import kitchenpos.persistence.dto.OrderTableDataDto;

public interface OrderTableDataAccessor extends BasicDataAccessor<OrderTableDataDto> {

    List<OrderTableDataDto> findAllByIdIn(List<Long> ids);

    List<OrderTableDataDto> findAllByTableGroupId(Long tableGroupId);
}
