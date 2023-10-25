package kitchenpos.domain.tablegroup;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class TableGroup {
    @Id
    private final Long id;
    private final LocalDateTime createdDate;

    private TableGroup(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public static TableGroupBuilder builder() {
        return new TableGroupBuilder();
    }

    public static final class TableGroupBuilder {
        private Long id;
        private LocalDateTime createdDate;

        private TableGroupBuilder() {
        }

        public TableGroupBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TableGroupBuilder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public TableGroup build() {
            return new TableGroup(id, createdDate);
        }
    }
}
