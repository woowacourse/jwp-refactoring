package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class TableGroup {

    @GeneratedValue
    @Id
    private Long id;
    private LocalDateTime createdDate;

    public TableGroup() {
        this.createdDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void mapping(List<OrderTable> orderTables) {
        if (orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
        orderTables.forEach(it -> it.group(id));
    }
}
