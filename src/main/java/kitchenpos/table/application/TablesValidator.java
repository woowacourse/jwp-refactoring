package kitchenpos.table.application;

import kitchenpos.table.application.dto.request.CreateTableGroupRequest;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class TablesValidator {

    public void validate(List<OrderTable> orderTables, CreateTableGroupRequest createTableGroupRequest) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }

        if (orderTables.size() != createTableGroupRequest.getOrderTables().size()) {
            throw new IllegalArgumentException();
        }

        for (OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || orderTable.existsTableGroup()) {
                throw new IllegalArgumentException();
            }
        }
    }
}
