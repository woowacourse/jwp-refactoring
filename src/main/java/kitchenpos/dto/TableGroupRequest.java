package kitchenpos.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TableGroupRequest {

    private List<OrderTableIdRequest> orderTables;

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }
}
