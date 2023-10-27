package kitchenpos.order.domain;

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

    @Embedded
    private NumberOfGuests numberOfGuests;

    private boolean empty;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    protected OrderTable() {
    }

    public OrderTable(final Long id, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public static OrderTable forSave(final int numberOfGuests, final boolean empty) {
        return new OrderTable(null, numberOfGuests, empty);
    }

    public void changeEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public void group(final Long tableGroupId) {
        validateEmptyStatus();
        this.tableGroupId = tableGroupId;
    }

    private void validateEmptyStatus() {
        if (empty) {
            throw new IllegalArgumentException("빈 테이블에는 테이블 그룹을 추가할 수 없습니다.");
        }
    }

    public boolean hasTableGroup() {
        return tableGroupId != null;
    }

    public void ungroup() {
        this.tableGroupId = null;
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getValue();
    }

    public boolean isEmpty() {
        return empty;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
