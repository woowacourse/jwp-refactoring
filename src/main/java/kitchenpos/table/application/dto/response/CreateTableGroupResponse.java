package kitchenpos.table.application.dto.response;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CreateTableGroupResponse {

    private Long id;
    private LocalDateTime createdAt;
    private List<OrderTableResponse> orderTables;

    protected CreateTableGroupResponse() {
    }

    public CreateTableGroupResponse(Long id, LocalDateTime createdAt, List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdAt = createdAt;
        this.orderTables = orderTables;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }

    public static CreateTableGroupResponse of(TableGroup tableGroup, List<OrderTable> orderTables) {
        List<OrderTableResponse> orderTablesReponse = orderTables.stream()
                .map(orderTable -> new OrderTableResponse(
                        orderTable.getId(),
                        orderTable.getNumberOfGuests(),
                        orderTable.isEmpty()
                ))
                .collect(Collectors.toList());

        return new CreateTableGroupResponse(
                tableGroup.getId(),
                tableGroup.getCreatedDate(),
                orderTablesReponse
        );
    }

    static class OrderTableResponse {
        private Long id;
        private int numberOfGuests;
        private boolean empty;

        protected OrderTableResponse() {
        }

        public OrderTableResponse(Long id, int numberOfGuests, boolean empty) {
            this.id = id;
            this.numberOfGuests = numberOfGuests;
            this.empty = empty;
        }

        public Long getId() {
            return id;
        }

        public int getNumberOfGuests() {
            return numberOfGuests;
        }

        public boolean isEmpty() {
            return empty;
        }
    }
}
