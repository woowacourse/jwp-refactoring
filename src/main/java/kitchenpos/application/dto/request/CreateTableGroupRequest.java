package kitchenpos.application.dto.request;

import java.time.LocalDateTime;
import java.util.List;

public class CreateTableGroupRequest {
    private Long id;
    private LocalDateTime createdDate;
    private List<Long> orderTableIds;

    private CreateTableGroupRequest() {
    }

    private CreateTableGroupRequest(Long id, LocalDateTime createdDate, List<Long> orderTableIds) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTableIds = orderTableIds;
    }

    public static CreateTableGroupRequestBuilder builder() {
        return new CreateTableGroupRequestBuilder();
    }

    public static final class CreateTableGroupRequestBuilder {
        private Long id;
        private LocalDateTime createdDate;
        private List<Long> orderTableIds;

        private CreateTableGroupRequestBuilder() {
        }

        public CreateTableGroupRequestBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CreateTableGroupRequestBuilder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public CreateTableGroupRequestBuilder orderTables(List<Long> orderTableIds) {
            this.orderTableIds = orderTableIds;
            return this;
        }

        public CreateTableGroupRequest build() {
            return new CreateTableGroupRequest(id, createdDate, orderTableIds);
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
