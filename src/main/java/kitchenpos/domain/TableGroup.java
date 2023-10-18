package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
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

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;


    protected TableGroup() {
    }

    private TableGroup(final Long id,
                       final LocalDateTime createdDate,
                       final List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroup of(final LocalDateTime createdDate,
                                final List<OrderTable> orderTables) {
        return new TableGroup(null, createdDate, orderTables);
    }

    public static TableGroup of(final LocalDateTime createdDate) {
        return new TableGroup(null, createdDate, new ArrayList<>());
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

    public void updateOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
}
