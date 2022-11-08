package kitchenpos.tablegroup.domain;

import java.util.List;
import kitchenpos.order.domain.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class TableGroupMapper {

    public TableGroup mapTable(TableGroup entity, List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup(entity.getId(),
                entity.getCreatedDate(),
                new Tables(orderTables));
        tableGroup.placeTableGroupId();
        return tableGroup;
    }
}
