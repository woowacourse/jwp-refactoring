package kitchenpos.domain.ordertable;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.domain.Order;
import kitchenpos.exception.CannotChangeEmptyException;

@Entity
public class OrderTable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private Long tableGroupId;

    @OneToMany
    private List<Order> order;

    @Embedded
    private TableStatus tableStatus;

    protected OrderTable() {
    }

    public OrderTable(final Integer numberOfGuests, final Boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(final Long id, final Long tableGroupId, final Integer numberOfGuests, final Boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.tableStatus = new TableStatus(numberOfGuests, empty);
        this.order = new ArrayList<>();
    }

    public void changeNumberOfGuests(final Integer numberOfGuests) {
        tableStatus.changeNumberOfGuests(numberOfGuests);
    }

    public void changeEmpty(final Boolean empty) {
        if (tableGroupId != null) {
            throw new CannotChangeEmptyException("그룹 지정된 테이블은 비어있는지 여부를 변경할 수 없습니다.");
        }

        if (!order.isEmpty() && order.stream()
                                     .anyMatch(Order::isCookingOrMeal)) {
            throw new CannotChangeEmptyException("조리 또는 식사중인 테이블은 비어있는지 여부를 변경할 수 없습니다.");
        }

        tableStatus.changeEmpty(empty);
    }

    public void addOrder(final Order order) {
        this.order.add(order);
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
        return tableStatus.getNumberOfGuests();
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        tableStatus.changeNumberOfGuests(numberOfGuests);
    }

    public boolean isEmpty() {
        return tableStatus.getEmpty();
    }

    public void setEmpty(final boolean empty) {
        tableStatus.changeEmpty(empty);
    }
}
