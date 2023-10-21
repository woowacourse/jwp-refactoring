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

    private static final int NUMBER_OF_GUESTS_MIN = 0;

    @OneToMany(mappedBy = "orderTable")
    private final List<Order> orders = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    @Column(nullable = false)
    private int numberOfGuests;
    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final Long id,
                      final TableGroup tableGroup,
                      final int numberOfGuests,
                      final boolean empty) {
        validateNumberOfGuests(numberOfGuests);
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(final int numberOfGuests,
                      final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public void group(final TableGroup tableGroup) {
        validateAbleToGroup();
        changeEmpty(false);
        this.tableGroup = tableGroup;
    }

    private void validateAbleToGroup() {
        if (isNotEmpty()) {
            throw new IllegalArgumentException("이미 주문 상태인 테이블을 단체로 지정할 수 없습니다.");
        }
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("이미 단체에 속한 테이블을 단체로 지정할 수 없습니다.");
        }
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < NUMBER_OF_GUESTS_MIN) {
            throw new IllegalArgumentException("테이블 당 인원 수는 최소 " + NUMBER_OF_GUESTS_MIN + "명입니다.");
        }
    }

    public void unGroup() {
        this.tableGroup = null;
        changeEmpty(false);
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        if (isEmpty()) {
            throw new IllegalArgumentException("빈 테이블의 인원 수를 변경할 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void changeEmpty(final boolean empty) {
        validateAbleToChangeEmpty();
        this.empty = empty;
    }

    private void validateAbleToChangeEmpty() {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("단체로 지정된 테이블을 비울 수 없습니다.");
        }
        final boolean hasAnyOrderInProgress = orders.stream()
                .anyMatch(Order::isInProgress);
        if (hasAnyOrderInProgress) {
            throw new IllegalArgumentException("조리 또는 식사 중인 테이블을 비울 수 없습니다.");
        }
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isNotEmpty() {
        return !empty;
    }

    public void addOrder(final Order order) {
        orders.add(order);
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }
}
