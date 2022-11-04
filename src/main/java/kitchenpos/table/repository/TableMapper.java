package kitchenpos.table.repository;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.Tables;
import org.springframework.stereotype.Component;

@Component
public class TableMapper {

    public TableGroup mapTable(TableGroup entity, List<OrderTable> orderTables) {
        entity.placeOrderTables(new Tables(orderTables));
        entity.placeTableGroupId();
        return entity;
    }
}
