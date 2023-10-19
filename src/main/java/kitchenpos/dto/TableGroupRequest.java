package kitchenpos.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;

public class TableGroupRequest {

    private List<OrderTableDto> orderTables;

    @JsonCreator
    public TableGroupRequest(List<OrderTableDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableDto> getOrderTables() {
        return orderTables;
    }

    public static class OrderTableDto {

        private Long id;

        @JsonCreator
        public OrderTableDto(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
