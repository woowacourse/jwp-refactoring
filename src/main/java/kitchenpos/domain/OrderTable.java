package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
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
    @ManyToOne
    @JoinColumn(name = "fk_order_table_to_table_group")
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

    public Long getId() {
        return id;
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
        this.tableGroup = tableGroup;
        this.empty = false;
        tableGroup.getOrderTables().add(this);
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
                throw new IllegalArgumentException("주문 완료 상태일때만 테이블 분리가 가능합니다.");
            }
        }
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (empty) {
            throw new IllegalArgumentException("주문을 등록할 수 없는 상태에서는 방문한 손님 수를 변경할 수 없습니다.");
        }

        this.numberOfGuests = numberOfGuests;
    }

    protected OrderTable() {
    }
}
