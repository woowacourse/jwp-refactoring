package kitchenpos.fixture;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.ui.dto.OrderTableDto;
import kitchenpos.tablegroup.ui.dto.TableGroupRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("NonAsciiCharacters")
public class TableGroupFixture {

    public static TableGroup 단체_지정_엔티티_생성(final List<OrderTable> orderTables) {
        final TableGroup tableGroup = TableGroup.of(LocalDateTime.now(), orderTables);
        orderTables.forEach(orderTable -> orderTable.updateTableGroup(tableGroup));

        return tableGroup;
    }

    public static TableGroupRequest 단체_지정_요청_dto_생성(final List<OrderTable> orderTables) {
        final List<OrderTableDto> orderTableDtos = orderTables.stream()
                                                       .map(orderTable -> new OrderTableDto(orderTable.getId()))
                                                       .collect(Collectors.toList());

        return new TableGroupRequest(orderTableDtos);
    }
}
