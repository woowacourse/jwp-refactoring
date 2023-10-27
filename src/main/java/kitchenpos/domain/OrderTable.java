package kitchenpos.domain;

import kitchenpos.common.BaseDate;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class OrderTable extends BaseDate {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders = new ArrayList<>();

    public OrderTable(final Long id, final Long tableGroupId, final Integer numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    protected OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final Boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public void changeEmpty(final Boolean empty) {
        if (tableGroupId != null && empty) {
            throw new IllegalArgumentException("[ERROR] 테이블 그룹이 존재하는 경우에 혼자 테이블을 비울 수 없습니다.");
        }
        if (orders.stream().anyMatch(OrderTable::canUngroupOrChangeEmpty)) {
            throw new IllegalArgumentException("[ERROR] 조리중이거나, 식사중인 테이블을 비울 수 없습니다.");
        }
        if (empty) {
            empty();
            return;
        }
        notEmpty();
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("[ERROR] 손님의 숫자는 항상 0보다 커야 합니다.");
        }
        if (isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 빈 테이블의 손님 수는 변경할 수 없습니다.");
        }

        this.numberOfGuests = numberOfGuests;
    }

    public void ungroup() {
        if (orders.stream().anyMatch(OrderTable::canUngroupOrChangeEmpty)) {
            throw new IllegalArgumentException("[ERROR] 조리중이거나, 식사중인 테이블은 그룹을 해제할 수 없습니다.");
        }
        this.tableGroupId = null;
        notEmpty();
    }

    private static boolean canUngroupOrChangeEmpty(final Order it) {
        return it.getOrderStatus() == OrderStatus.COOKING || it.getOrderStatus() == OrderStatus.MEAL;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void notEmpty() {
        this.empty = false;
    }

    public void empty() {
        this.empty = true;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
}
