package kitchenpos.factory;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@Component
public class TableGroupFactory {
    public TableGroup create(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setCreatedDate(createdDate);
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }

    public TableGroup create(List<OrderTable> orderTables) {
        return create(null, null, orderTables);
    }

    public TableGroup create(Long id, List<OrderTable> orderTables) {
        return create(id, null, orderTables);
    }
}
