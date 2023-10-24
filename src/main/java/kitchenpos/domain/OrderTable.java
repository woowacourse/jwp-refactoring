package kitchenpos.domain;

import kitchenpos.common.BaseDate;

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

    public OrderTable() {
    }

    public void ungroup() {
        if (orders.stream().anyMatch(OrderTable::canUngroup)) {
            throw new IllegalArgumentException("[ERROR] 조리중이거나, 식사중인 테이블은 그룹을 해제할 수 없습니다.");
        }
        this.tableGroupId = null;
        notEmpty();
    }

    private static boolean canUngroup(final Order it) {
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

    public void setId(final Long id) {
        this.id = id;
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
