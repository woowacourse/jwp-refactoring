package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderOrderTables;

    public TableGroup() {
        this(null);
    }

    public TableGroup(List<OrderTable> orderOrderTables) {
        this.createdDate = LocalDateTime.now();
        this.orderOrderTables = orderOrderTables;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderOrderTables;
    }

    public void setOrderTables(final List<OrderTable> orderOrderTables) {
        this.orderOrderTables = orderOrderTables;
    }
}
