package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    protected TableGroup() {
    }

    public TableGroup(final LocalDateTime createdDate) {
        this(null, createdDate);
    }

    public TableGroup(final Long id, final LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public void bind(final List<OrderTable> orderTables) {
        if (orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }

        for (OrderTable orderTable : orderTables) {
            orderTable.joinGroup(id);
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

}
