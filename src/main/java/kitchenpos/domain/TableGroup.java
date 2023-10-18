package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;

import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class TableGroup {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup", cascade = REMOVE)
    private List<OrderTable> orderTables;

    public TableGroup(List<OrderTable> orderTables, LocalDateTime localDateTime) {
        this.orderTables = orderTables;
        this.createdDate = localDateTime;
    }

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    protected TableGroup() {

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
