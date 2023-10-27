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

    public OrderTable(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void updateEmpty(boolean isEmpty) {
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
