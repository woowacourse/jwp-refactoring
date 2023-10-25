package kitchenpos.dto.response;

import kitchenpos.domain.tablegroup.TableGroup;

import java.time.LocalDateTime;

public class CreateTableGroupResponse {
    private final Long id;
    private final LocalDateTime createdDate;

    private CreateTableGroupResponse(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public static CreateTableGroupResponse from(TableGroup tableGroup) {
        return builder()
                .id(tableGroup.getId())
                .createdDate(tableGroup.getCreatedDate())
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

    public static final class CreateTableGroupResponseBuilder {
        private Long id;
        private LocalDateTime createdDate;

        private CreateTableGroupResponseBuilder() {
        }

        public CreateTableGroupResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CreateTableGroupResponseBuilder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public CreateTableGroupResponse build() {
            return new CreateTableGroupResponse(id, createdDate);
        }
    }
}
