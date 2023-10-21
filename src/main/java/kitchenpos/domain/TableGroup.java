package kitchenpos.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    public TableGroup() {
        this.createdDate = LocalDateTime.now();
    }

    public TableGroup(Long id) {
        this.id = id;
        this.createdDate = LocalDateTime.now();
    }

    public void addOrderTables(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable savedOrderTable : orderTables) {
            savedOrderTable.changeEmpty(false);
            savedOrderTable.changeTableGroup(this);
        }
        this.orderTables = orderTables;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
