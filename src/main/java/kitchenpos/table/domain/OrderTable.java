package kitchenpos.table.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.table.exception.CannotChangeOrderTableEmpty;
import kitchenpos.table.exception.CannotChangeOrderTableGuest;

@Entity
public class OrderTable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    private int numberOfGuests;

    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public boolean canCreateTableGroup() {
        return this.isEmpty() && Objects.isNull(tableGroupId);
    }

    public void includeInTableGroup(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    public void excludeFromTableGroup() {
        this.tableGroupId = null;
        this.empty = true;
    }

    public void changeEmptyStatus(Boolean empty) {
        if (Objects.nonNull(this.tableGroupId)) {
            throw new CannotChangeOrderTableEmpty();
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new CannotChangeOrderTableGuest();
        }

        if (this.empty) {
            throw new CannotChangeOrderTableGuest();
        }

        this.numberOfGuests = numberOfGuests;
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public Long getTableGroupId() {
        if (Objects.isNull(this.tableGroupId)) {
            return null;
        }

        return this.tableGroupId;
    }
}
