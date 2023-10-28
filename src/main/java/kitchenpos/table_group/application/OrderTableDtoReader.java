package kitchenpos.table_group.application;

import java.util.List;
import kitchenpos.table_group.application.dto.OrderTableDtoInTableGroup;

public interface OrderTableDtoReader {

    List<OrderTableDtoInTableGroup> readTablesByTableGroupId(final Long tableGrouopId);
}
