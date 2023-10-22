package kitchenpos.domain.ordertable;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.order.Order;
import kitchenpos.exception.CannotChangeEmptyException;
import kitchenpos.exception.InvalidGuestNumberException;
import kitchenpos.exception.InvalidUnGroupException;

@Entity
public class OrderTable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @OneToMany(mappedBy = "orderTable")
    private List<Order> order;


    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column(name = "empty", nullable = false)
    private Boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final Integer numberOfGuests, final Boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(final Long id, final TableGroup tableGroup, final Integer numberOfGuests, final Boolean empty) {
        validateCanBeEmpty(numberOfGuests, empty);
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
        this.order = new ArrayList<>();
    }

    private void validateCanBeEmpty(final Integer numberOfGuests, final Boolean empty) {
        if (numberOfGuests > 0 && empty) {
            throw new InvalidGuestNumberException("손님 수가 1명 이상이면 빈 테이블이 될 수 없습니다.");
        }
    }

    public void changeNumberOfGuests(final Integer numberOfGuests) {
        validateNotEmpty();
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    private void validateNotEmpty() {
        if (empty) {
            throw new InvalidGuestNumberException("빈 테이블에는 손님 수를 변경할 수 없습니다.");
        }
    }

    public void changeEmpty(final Boolean empty) {
        if (hasTableGroup()) {
            throw new CannotChangeEmptyException("그룹 지정된 테이블은 비어있는지 여부를 변경할 수 없습니다.");
        }

        if (!order.isEmpty() && order.stream()
                                     .anyMatch(Order::isCookingOrMeal)) {
            throw new CannotChangeEmptyException("조리 또는 식사중인 테이블은 비어있는지 여부를 변경할 수 없습니다.");
        }
        validateCanBeEmpty(numberOfGuests.getValue(), empty);
        this.empty = empty;
    }

    public void addOrder(final Order order) {
        this.order.add(order);
    }

    public boolean hasTableGroup() {
        return tableGroup != null;
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void group(final TableGroup tableGroup) {
        changeEmpty(false);
        this.tableGroup = tableGroup;
    }

    public void unGroup() {
        if (tableGroup == null) {
            throw new InvalidUnGroupException("그룹 지정되지 않은 테이블은 그룹을 해제할 수 없습니다.");
        }
        if (order.stream()
                 .anyMatch(Order::isCookingOrMeal)) {
            throw new InvalidUnGroupException("조리 또는 식사중인 테이블은 그룹을 해제할 수 없습니다.");
        }
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests.getValue();
    }

    public Boolean isEmpty() {
        return empty;
    }
}
