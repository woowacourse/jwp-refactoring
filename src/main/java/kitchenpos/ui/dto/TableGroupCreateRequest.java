package kitchenpos.ui.dto;

import java.util.List;

public class TableGroupCreateRequest {

    private List<OrderTableIdDto> orderTables;

    TableGroupCreateRequest() {

    }

    public TableGroupCreateRequest(final List<OrderTableIdDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableIdDto> getOrderTables() {
        return orderTables;
    }

    public static class OrderTableIdDto {

        private Long id;

        OrderTableIdDto() {

        }

        public OrderTableIdDto(final Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
