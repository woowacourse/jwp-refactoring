package kitchenpos.table.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(
            final Long id,
            final Long tableGroupId,
            final int numberOfGuests,
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
        this(null, tableGroupId, numberOfGuests, empty);
    }

    public OrderTable(
            final int numberOfGuests,
            final boolean empty
    ) {
        this(null, null, numberOfGuests, empty);
    }

    public boolean isAbleToGroup() {
        return this.empty && Objects.isNull(this.tableGroupId);
    }

    public void groupByTableGroup(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    public void ungroup() {
        this.tableGroupId = null;
        this.empty = false;
    }

    public void changeEmpty(final boolean empty, final OrderTableValidator orderTableValidator) {
        orderTableValidator.validate(this);
        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests, final TableChangeNumberOfGuestValidator tableValidator) {
        tableValidator.validate(numberOfGuests, this);
        this.numberOfGuests = numberOfGuests;
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
}
