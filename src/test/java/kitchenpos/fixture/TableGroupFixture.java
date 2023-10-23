package kitchenpos.fixture;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupRequest.OrderTableIdRequest;

public class TableGroupFixture {

    public static TableGroup 단체_지정() {
        return new TableGroup(LocalDateTime.now());
    }

    public static TableGroup 단체_지정(List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        tableGroup.changeOrderTables(orderTables);
        return tableGroup;
    }

    public static TableGroupRequest 단체_지정_요청(OrderTable... ids) {
        return Arrays.stream(ids)
                .map(OrderTable::getId)
                .map(OrderTableIdRequest::new)
                .collect(collectingAndThen(toList(), TableGroupRequest::new));
    }
}
