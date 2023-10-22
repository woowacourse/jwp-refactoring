package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class TableGroup {

    private static final int MIN_ORDER_TABLE_SIZE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.PERSIST)
    private List<OrderTable> orderTables;

    public TableGroup() {
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = new ArrayList<>();
    }

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public void addOrderTable(List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = orderTables.stream()
            .map(orderTable -> {
                orderTable.grouping(this);
                orderTable.filling();
                return orderTable;
            }).collect(Collectors.toList());
    }

    private void validate(List<OrderTable> orderTables) {
        checkIdentified();
        checkMinOrderTable(orderTables);
        checkEmpty(orderTables);
        checkNotGrouped(orderTables);
    }

    private void checkIdentified() {
        if (id == null) {
            throw new IllegalStateException("TableGroup 의 식별자가 정의되지 않았습니다.");
        }
    }

    private void checkMinOrderTable(List<OrderTable> orderTables) {
        if (this.orderTables.isEmpty() && orderTables.size() < MIN_ORDER_TABLE_SIZE) {
            throw new IllegalArgumentException(String.format("주문 테이블은 최소 %d개 이상입니다.", MIN_ORDER_TABLE_SIZE));
        }
    }

    private void checkEmpty(List<OrderTable> orderTables) {
        orderTables.stream()
            .filter(orderTable -> !orderTable.isEmpty())
            .findAny()
            .ifPresent(orderTable -> {
                throw new IllegalArgumentException("그룹화하기 위해서는 모든 테이블은 빈 테이블이여야 합니다.");
            });
    }

    private void checkNotGrouped(List<OrderTable> orderTables) {
        orderTables.stream()
            .filter(orderTable -> orderTable.getTableGroup() != null)
            .findAny()
            .ifPresent(orderTable -> {
                throw new IllegalArgumentException("주문 테이블이 이미 그룹화 되어 있습니다.");
            });
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
