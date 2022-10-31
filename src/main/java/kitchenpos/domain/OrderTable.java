package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.exception.InvalidOrderTableException;

@Entity
@Table(name = "order_table")
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id", length = 20)
    private Long tableGroupId;

    @Column(name = "number_of_guests", length = 11, nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this(null, tableGroupId, numberOfGuests, empty);
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, numberOfGuests, empty);
    }

    protected OrderTable() {
    }

    public boolean isUsing() {
        return !empty || Objects.nonNull(tableGroupId);
    }

    public void ungroup() {
        tableGroupId = null;
        empty = false;
    }

    public void changeEmpty(final boolean empty) {
        validateAffiliatedTable();
        this.empty = empty;
    }

    private void validateAffiliatedTable() {
        if (Objects.nonNull(tableGroupId)) {
            throw new InvalidOrderTableException("이미 테이블 그룹에 속해있습니다.");
        }
    }

    public void changeNumberOfGuests(final int changingNumber) {
        validateChangingSize(changingNumber);
        validateEmpty();
        numberOfGuests = changingNumber;
    }

    private void validateChangingSize(final int changingNumber) {
        if (changingNumber < 0) {
            throw new InvalidOrderTableException("테이블 인원은 음수일 수 없습니다.");
        }
    }

    private void validateEmpty() {
        if (empty) {
            throw new InvalidOrderTableException("테이블이 비어있을 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderTable orderTable)) {
            return false;
        }
        return Objects.equals(id, orderTable.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
