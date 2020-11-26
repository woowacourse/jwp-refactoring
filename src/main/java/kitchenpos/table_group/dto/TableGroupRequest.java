package kitchenpos.table_group.dto;

import java.util.List;
import kitchenpos.order_table.dto.OrderTableIdRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TableGroupRequest {

    private List<OrderTableIdRequest> orderTables;
}
