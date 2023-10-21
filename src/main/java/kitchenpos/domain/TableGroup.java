package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
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

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.PERSIST)
    private List<OrderTable> orderTables = new ArrayList<>();

    public TableGroup() {
    }

    public TableGroup(LocalDateTime createdDate) {
        this(null, createdDate);
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public static TableGroup from() {
        return new TableGroup(
                LocalDateTime.now()
        );
    }

    public void addAllOrderTables(List<OrderTable> orderTables) {
        orderTables.forEach(orderTable -> orderTable.registerTableGroup(this));
        this.orderTables.addAll(orderTables);
    }

    public void removeAllOrderTables() {
        orderTables.forEach(OrderTable::breakupTableGroup);
        orderTables.clear();
        this.orderTables = new ArrayList<>();
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
