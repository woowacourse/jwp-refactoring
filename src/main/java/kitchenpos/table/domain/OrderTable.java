package kitchenpos.table.domain;

import kitchenpos.global.exception.KitchenposException;

import javax.persistence.*;
import java.util.Objects;

import static kitchenpos.global.exception.ExceptionInformation.EMPTY_TABLE_UPDATE_GUEST;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tableGroupId;

    @Embedded
    private Guest guest;

    private boolean empty;

    protected OrderTable() {

    }

    public OrderTable(final Long tableGroupId, final Guest guest, final boolean empty) {
        this.tableGroupId = tableGroupId;
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
        return tableGroupId;
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

    public void updateTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void deleteTableGroupId() {
        this.tableGroupId = null;
    }

    public boolean isGrouped() {
        return Objects.nonNull(tableGroupId);
    }

    public boolean unableGrouping() {
        return !isEmpty() || isGrouped();
    }
}
