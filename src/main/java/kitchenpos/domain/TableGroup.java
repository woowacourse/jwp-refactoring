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

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL)
    private List<OrderTable> orderTables = new ArrayList<>();

    public TableGroup() {
    }

    public TableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.createdDate = createdDate;
        validateOrderTables(orderTables);
        setOrderTables(orderTables);
    }

    public void ungroup() {
        for (OrderTable orderTable : orderTables) {
            orderTable.unGroup();
        }
        orderTables.clear();
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

    private void setOrderTables(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            orderTable.updateTableGroup(this);
        }
        this.orderTables.addAll(orderTables);
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        if (orderTables.size() < 2) {
            throw new IllegalArgumentException("테이블 그룹의 개수는 2개 이상이어야 합니다.");
        }
    }

}
