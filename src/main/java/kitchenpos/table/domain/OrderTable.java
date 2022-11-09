package kitchenpos.table.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.exception.AlreadyGroupedException;
import kitchenpos.exception.CanNotGroupException;
import kitchenpos.exception.NumberOfGuestsSizeException;
import kitchenpos.exception.TableEmptyException;

@Table(name = "order_table")
@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void validateOrderable() {
        if (empty) {
            throw new TableEmptyException();
        }
    }

    public void updateEmpty(final boolean empty) {
        validateNotGrouping();
        this.empty = empty;
    }

    private void validateNotGrouping() {
        if (Objects.nonNull(tableGroupId)) {
            throw new AlreadyGroupedException();
        }
    }

    public void updateNumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        validateOrderable();
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new NumberOfGuestsSizeException();
        }
    }

    public void groupTableBy(final Long tableGroupId) {
        validateGroupable();
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    private void validateGroupable() {
        if (!empty | Objects.nonNull(tableGroupId)) {
            throw new CanNotGroupException();
        }
    }

    public void ungroup() {
        this.tableGroupId = null;
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
