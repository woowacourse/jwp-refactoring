package kitchenpos.application.dto.response;

import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CreateTableGroupResponse {
    private final Long id;
    private final LocalDateTime createdDate;
    private final List<OrderTableResponse> orderTables;

    private CreateTableGroupResponse(Long id, LocalDateTime createdDate, List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static CreateTableGroupResponse from(TableGroup tableGroup) {
        return builder()
                .id(tableGroup.getId())
                .createdDate(tableGroup.getCreatedDate())
                .orderTables(tableGroup.getOrderTables().stream()
                        .map(OrderTableResponse::from)
                        .collect(Collectors.toList())
                )
                .build();
    }

    public static CreateTableGroupResponseBuilder builder() {
        return new CreateTableGroupResponseBuilder();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }

    public static final class CreateTableGroupResponseBuilder {
        private Long id;
        private LocalDateTime createdDate;
        private List<OrderTableResponse> orderTables = new ArrayList<>();

        private CreateTableGroupResponseBuilder() {
        }

        public static CreateTableGroupResponseBuilder aCreateTableGroupResponse() {
            return new CreateTableGroupResponseBuilder();
        }

        public CreateTableGroupResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CreateTableGroupResponseBuilder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public CreateTableGroupResponseBuilder orderTables(List<OrderTableResponse> orderTables) {
            this.orderTables = orderTables;
            return this;
        }

        public CreateTableGroupResponse build() {
            return new CreateTableGroupResponse(id, createdDate, orderTables);
        }
    }
}
