package kitchenpos.domain.group;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.domain.order.OrderTables;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "table_group")
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    protected TableGroup() {
    }

    public TableGroup(final Long id, final LocalDateTime createdDate, final OrderTables orderTables) {
        orderTables.validateGroup();
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables.arrangeGroup(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }

    public static class Builder {

        private Long id;
        private LocalDateTime createdDate;
        private OrderTables orderTables;

        public Builder id(final Long id) {
            this.id = id;
            return this;
        }

        public Builder createdDate(final LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder orderTables(final OrderTables orderTables) {
            this.orderTables = orderTables;
            return this;
        }

        public TableGroup build() {
            return new TableGroup(id, createdDate, orderTables);
        }
    }
}
