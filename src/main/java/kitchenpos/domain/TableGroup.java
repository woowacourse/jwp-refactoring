package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.Arrays;
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
    @OneToMany(mappedBy = "tableGroupId")
    private List<OrderTable> orderTables;

    protected TableGroup() {
        
    }

    public TableGroup(OrderTable[] orderTables) {
        this(null, null, Arrays.asList(orderTables));
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this(id, createdDate, null);
    }

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public void updateCreatedDate() {
        createdDate = LocalDateTime.now();
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

    public void updateOrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
}
