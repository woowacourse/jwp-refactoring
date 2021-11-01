package kitchenpos.dto.request;

import java.util.List;
import kitchenpos.domain.TableGroup;

public class TableGroupRequest {

    private List<OrderTableOfGroupRequest> orderTables;

    protected TableGroupRequest() {
    }

    public TableGroupRequest(final List<OrderTableOfGroupRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroup toEntity() {
        return new TableGroup();
    }

    public List<OrderTableOfGroupRequest> getOrderTables() {
        return orderTables;
    }

    public static class OrderTableOfGroupRequest {

        private Long id;

        protected OrderTableOfGroupRequest() {
        }

        public OrderTableOfGroupRequest(final Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
