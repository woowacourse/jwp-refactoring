package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.springframework.data.annotation.CreatedDate;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.PERSIST)
    private List<OrderTable> orderTables = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdDate;

    protected TableGroup() {
    }

    public static TableGroup from(final List<OrderTable> orderTables) {
        final TableGroup tableGroup = new TableGroup();
        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroup(tableGroup);
            tableGroup.orderTables.add(orderTable);
        }
        return tableGroup;
    }

    public void addOrderTable(final OrderTable orderTable) {
        orderTable.setTableGroup(this);
        this.orderTables.add(orderTable);
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
        return orderTables;
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
}
