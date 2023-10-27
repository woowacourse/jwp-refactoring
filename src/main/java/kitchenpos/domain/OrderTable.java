package kitchenpos.domain;

import kitchenpos.application.exception.InvalidChangeOrderTableNumberOfGuests;
import kitchenpos.domain.exception.InvalidUpdateNumberOfGuestsException;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    private int numberOfGuests;

    private boolean empty;

    protected OrderTable() {}

    private OrderTable(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(final int numberOfGuests, final boolean empty) {
        validateNumberOfGuests(numberOfGuests);

        return new OrderTable(numberOfGuests, empty);
    }

    private static void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new InvalidUpdateNumberOfGuestsException("방문한 손님 수는 음수가 될 수 없습니다.");
        }
    }

    public void ungroup() {
        this.tableGroup = null;
        this.empty = false;
    }

    public void updateTableGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void updateNumberOfGuests(final int numberOfGuests) {
        validateOrderTableIsEmpty();
        validateNumberOfGuests(numberOfGuests);

        this.numberOfGuests = numberOfGuests;
    }

    private void validateOrderTableIsEmpty() {
        if (isEmpty()) {
            throw new InvalidChangeOrderTableNumberOfGuests("주문 테이블이 빈 상태라면 사용자 수를 변경할 수 없습니다.");
        }
    }

    public void updateEmpty(final boolean empty) {
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderTable that = (OrderTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderTable{" +
               "id=" + id +
               ", numberOfGuests=" + numberOfGuests +
               ", empty=" + empty +
               '}';
    }
}
