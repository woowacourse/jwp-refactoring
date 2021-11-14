package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import kitchenpos.exception.InvalidOrderTableException;
import kitchenpos.exception.NumberOfGuestsNegativeException;
import kitchenpos.exception.OrderTableEmptyException;
import kitchenpos.exception.OrderTableNotEmptyException;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @NotNull
    private int numberOfGuests;

    @NotNull
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        validateNegative(numberOfGuests);
    }

    private void validateNegative(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new InvalidOrderTableException(String.format("음수 %s는 손님 수가 될 수 없습니다.", numberOfGuests));
        }
    }

    public void groupBy(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void ungroup() {
        this.tableGroup = null;
        this.empty = true;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new NumberOfGuestsNegativeException(String.format("음수 %s는 손님 수로 사용할 수 없습니다.", numberOfGuests));
        }

        if (isEmpty()) {
            throw new OrderTableEmptyException(String.format("%s ID OrderTable이 비어있는 상태입니다.", id));
        }

        this.numberOfGuests = numberOfGuests;
    }

    public void changeEmpty(boolean empty) {
        if (Objects.nonNull(tableGroup)) {
            throw new OrderTableNotEmptyException(
                String.format("TableGroup %s을 가지고 있어 상태 변경이 불가능 합니다.", tableGroup.getId())
            );
        }

        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    // TODO: 곧 제거
    public void setId(final Long id) {
        this.id = id;
    }

    public Long getTableGroupId() {
        if (Objects.isNull(tableGroup)) {
            return null;
        }

        return tableGroup.getId();
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    public boolean isGrouped() {
        return Objects.nonNull(tableGroup);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderTable that = (OrderTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
