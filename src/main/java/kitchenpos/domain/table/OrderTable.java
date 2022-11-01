package kitchenpos.domain.table;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.domain.common.NumberOfGuests;
import kitchenpos.exception.badrequest.EmptyTableCannotChangeNumberOfGuestsException;
import kitchenpos.exception.badrequest.GroupedTableCannotChangeEmptyException;

@Entity
@Table(name = "order_table")
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    @Embedded
    private NumberOfGuests numberOfGuests;
    @Column(name = "empty", nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, new NumberOfGuests(numberOfGuests), empty);
    }

    public OrderTable(final Long id, final TableGroup tableGroup, final NumberOfGuests numberOfGuests,
                      final boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeEmpty(final boolean empty) {
        validateNotGrouped();
        this.empty = empty;
    }

    private void validateNotGrouped() {
        if (tableGroup != null) {
            throw new GroupedTableCannotChangeEmptyException();
        }
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateNotEmpty();
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    private void validateNotEmpty() {
        if (empty) {
            throw new EmptyTableCannotChangeNumberOfGuestsException();
        }
    }

    public boolean isGrouped() {
        return this.tableGroup != null;
    }

    public void designateTableGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void ungroup() {
        this.tableGroup = null;
        this.empty = false;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getValue();
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderTable)) {
            return false;
        }
        OrderTable that = (OrderTable) o;
        return Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
