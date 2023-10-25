package kitchenpos.domain;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "table_group_id")
    private OrderTableGroup orderTableGroup;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(OrderTableGroup orderTableGroup, int numberOfGuests) {
        this.orderTableGroup = orderTableGroup;
        this.numberOfGuests = numberOfGuests;
    }

    public OrderTable(OrderTableGroup orderTableGroup, int numberOfGuests, boolean empty) {
        this.orderTableGroup = orderTableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void updateEmpty(boolean isEmpty) {
        validateTableGroupIsNotNull();
        this.empty = isEmpty;
    }

    public Long getId() {
        return id;
    }

    public OrderTableGroup getTableGroup() {
        return orderTableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void validateTableGroupIsNotNull() {
        if (Objects.nonNull(orderTableGroup)) {
            throw new IllegalArgumentException("TableGroup이 Null일 수 없습니다.");
        }
    }

    public void validateNumberOfGuests() {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님의 수가 음수일 수 없습니다.");
        }
    }

    public void validateIsEmpty() {
        if (isEmpty()) {
            throw new IllegalArgumentException("OrderTable이 비어있지 않아야 합니다.");
        }
    }

    public void updateNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void updateTableGroup(final OrderTableGroup orderTableGroup) {
        this.orderTableGroup = orderTableGroup;
    }

}
