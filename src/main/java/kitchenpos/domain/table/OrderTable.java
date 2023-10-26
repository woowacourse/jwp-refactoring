package kitchenpos.domain.table;

import kitchenpos.exception.KitchenposException;

import javax.persistence.*;
import java.util.Objects;

import static kitchenpos.exception.ExceptionInformation.EMPTY_TABLE_UPDATE_GUEST;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private TableGroup tableGroup;

    @Embedded
    private Guest guest;

    private boolean empty;

    protected OrderTable() {

    }

    public OrderTable(final TableGroup tableGroup, final Guest guest, final boolean empty) {
        this.tableGroup = tableGroup;
        this.guest = guest;
        this.empty = empty;
    }

    public static OrderTable create(final int numberOfGuests) {
        return new OrderTable(null, Guest.create(numberOfGuests), false);
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        if (Objects.isNull(tableGroup)) {
            return null;
        }
        return tableGroup.getId();
    }

    public boolean isEmpty() {
        return empty;
    }

    public int getNumberOfGuests() {
        return guest.getNumberOfGuests();
    }

    public void updateOrderStatus(final boolean empty) {
        this.empty = empty;
    }

    public void updateGuest(final int numberOfGuests) {
        if (isEmpty()) {
            throw new KitchenposException(EMPTY_TABLE_UPDATE_GUEST);
        }
        guest = Guest.create(numberOfGuests);
    }

    public void updateTableGroupId(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void deleteTableGroupId() {
        this.tableGroup = null;
    }

    public boolean isGrouped() {
        return Objects.nonNull(tableGroup);
    }

    public boolean unableGrouping() {
        return !isEmpty() || isGrouped();
    }
}
