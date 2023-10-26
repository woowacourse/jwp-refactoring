package kitchenpos.table.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long tableGroupId;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    private OrderTable(
            final Long id,
            final Long tableGroupId,
            final NumberOfGuests numberOfGuests,
            final boolean empty
    ) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(
            final Long tableGroupId,
            final int numberOfGuests,
            final boolean empty
    ) {
        this(null, tableGroupId, new NumberOfGuests(numberOfGuests), empty);
    }

    public void updateTableGroup(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    public void updateNumberOfGuests(final int numberOfGuests) {
        validateEmptyForUpdateNumberOfGuests();
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    private void validateEmptyForUpdateNumberOfGuests() {
        if (this.empty) {
            throw new IllegalArgumentException("테이블은 비어있을 수 없습니다.");
        }
    }

    public void updateEmpty(final boolean empty) {
        validateTableGroup();
        this.empty = empty;
    }

    private void validateTableGroup() {
        if (Objects.nonNull(this.tableGroupId)) {
            throw new IllegalArgumentException("이미 속해있는 table group이 있습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
