package kitchenpos.domain.table;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {

    private static final int MIN_NUMBER_OF_GUESTS = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int numberOfGuests;
    @Column(name = "table_group_id")
    private Long tableGroupId;
    private boolean empty;

    public OrderTable(final int numberOfGuests) {
        validateGuests(numberOfGuests);

        this.numberOfGuests = numberOfGuests;
        this.empty = numberOfGuests == MIN_NUMBER_OF_GUESTS;
    }

    protected OrderTable() {
    }

    private void validateGuests(final int numberOfGuests) {
        if (numberOfGuests < MIN_NUMBER_OF_GUESTS) {
            throw new IllegalArgumentException(MIN_NUMBER_OF_GUESTS + "이상의 손님 수를 입력해 주세요.");
        }
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        validateIsEmpty();
        this.numberOfGuests = numberOfGuests;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    private void validateIsEmpty() {
        if (empty) {
            throw new IllegalArgumentException("주문 테이블이 비어있습니다.");
        }
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void validateGroupable() {
        if (!empty || Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException("그룹화된 테이블이 존재합니다.");
        }
    }

    public void ungroup() {
        this.empty = true;
        this.tableGroupId = null;
    }

    public void updateEmpty(final boolean empty) {
        validateClearable();

        this.empty = empty;
    }

    private void validateClearable() {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException();
        }
    }
}
