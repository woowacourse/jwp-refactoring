package kitchenpos.order.domain;

import static javax.persistence.CascadeType.PERSIST;

import java.time.LocalDateTime;
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

    @OneToMany(mappedBy = "tableGroup", cascade = PERSIST)
    private List<OrderTable> orderTables;

    public TableGroup() {
        this(null, LocalDateTime.now(), null);
    }

    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public void mapTables(List<OrderTable> tables) {
        this.orderTables = tables;
        for (OrderTable table : tables) {
            table.mapTableGroup(this);
        }
    }

    public void changeStatusNotEmpty() {
        for (OrderTable orderTable : orderTables) {
            orderTable.changeStatusNotEmpty();
        }
    }

    public void initCurrentDateTime() {
        createdDate = LocalDateTime.now();
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

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    @Override
    public String toString() {
        return "TableGroup{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                '}';
    }
}
