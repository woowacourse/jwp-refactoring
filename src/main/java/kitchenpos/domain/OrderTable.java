package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final Long id, final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
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
        if (Objects.nonNull(tableGroup)) {
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

    public void groupTableBy(final TableGroup tableGroup) {
        validateGroupable();
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    private void validateGroupable() {
        if (!empty | Objects.nonNull(tableGroup)) {
            throw new CanNotGroupException();
        }
    }

    public void ungroup() {
        this.tableGroup = null;
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
}
