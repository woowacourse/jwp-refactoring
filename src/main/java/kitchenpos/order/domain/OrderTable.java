package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.domain.BaseEntity;
import kitchenpos.domain.TableGroup;

@Entity
public class OrderTable extends BaseEntity {

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_order_table_to_table_group"))
    private TableGroup tableGroup;
    @Column(nullable = false)
    private int numberOfGuests;
    @Column(nullable = false)
    private boolean empty;
    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders = new ArrayList<>();

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        validateTableGroupIsNull();
        validateOrderCompletion();

        this.empty = empty;
    }

    private void validateTableGroupIsNull() {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("테이블 그룹이 존재하는 경우 테이블 상태를 수정할 수 없습니다.");
        }
    }

    public void attachTableGroup(final TableGroup tableGroup) {
        validateTableGroupIsNull();
        validateTableGroupIsNotNull(tableGroup);
        this.tableGroup = tableGroup;
        this.empty = false;
        tableGroup.getOrderTables().add(this);
    }

    private void validateTableGroupIsNotNull(final TableGroup tableGroup) {
        if (Objects.isNull(tableGroup)) {
            throw new IllegalArgumentException("테이블 그룹이 존재하지 않는 경우 예외가 발생한다.");
        }
    }

    /*
    테이블을 분리한다.
    아직 결제를 한것이 아니기때문에 empty는 false 상태
     */
    public void detachTableGroup() {
        validateOrderCompletion();

        this.tableGroup = null;
    }

    private void validateOrderCompletion() {
        for (Order order : orders) {
            if (order.getOrderStatus() != OrderStatus.COMPLETION) {
                throw new IllegalArgumentException("주문 완료 상태일때만 테이블 수정 가능합니다.");
            }
        }
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수는 음수일 수 없습니다.");
        }

        if (empty) {
            throw new IllegalArgumentException("주문을 등록할 수 없는 상태에서는 방문한 손님 수를 변경할 수 없습니다.");
        }

        this.numberOfGuests = numberOfGuests;
    }

    public List<Order> getOrders() {
        return orders;
    }

    protected OrderTable() {
    }
}
