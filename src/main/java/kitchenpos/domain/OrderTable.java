package kitchenpos.domain;

import static java.util.Objects.isNull;

import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private TableGroup tableGroup;
    @Column(nullable = false)
    private int numberOfGuests;
    @Column(nullable = false)
    private boolean empty; // empty = true -> 주문 등록 불가
    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders;

    protected OrderTable() {
    }

    public OrderTable(final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public Long getTableGroupId() {
        if(isNull(tableGroup)) {
            return null;
        }
        return tableGroup.getId();
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isTableGroupEmpty() {
        return isNull(tableGroup);
    }

    public void updateEmptyIfNoConstraints(final boolean empty) {
        validateTableGroupEmpty();
        validateOrderStatus();
        this.empty = empty;
    }

    private void validateTableGroupEmpty() {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("단체 지정이 되어있습니다.");
        }
    }

    private void validateOrderStatus() {
        if(!isOrderCompleted()) {
            throw new IllegalArgumentException("계산이 완료되지 않아 테이블의 상태를 바꿀 수 없습니다.");
        }
    }

    private boolean isOrderCompleted() {
        for (Order order : orders) {
            if (!order.isCompleted()) {
                return false;
            }
        }
        return true;
    }

    public void updateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("올바르지 않은 숫자입니다.");
        }
        if (empty) {
            throw new IllegalArgumentException("빈 테이블은 게스트 수를 변경할 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void groupBy(final TableGroup tableGroup) {
        this.empty = false;
        this.tableGroup = tableGroup;
    }

    public void ungroupBy(final TableGroup tableGroup) {
        validateRightTableGroup(tableGroup);
        validateUngroupAvailable();
        this.empty = false;
        this.tableGroup = null;
    }

    private void validateRightTableGroup(final TableGroup tableGroup) {
        if(!this.tableGroup.equals(tableGroup)) {
            throw new IllegalArgumentException("해당 단체에 지정되어 있지 않아 단체 지정 해제가 불가능합니다.");
        }
    }

    private void validateUngroupAvailable() {
        if(!isOrderCompleted()) {
            throw new IllegalArgumentException("계산 완료되지 않은 테이블이 남아있어 단체 지정 해제가 불가능합니다.");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderTable that = (OrderTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
