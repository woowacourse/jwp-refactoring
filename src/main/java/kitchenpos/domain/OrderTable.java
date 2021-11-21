package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.exception.CannotChangeOrderTableEmpty;
import kitchenpos.exception.CannotChangeOrderTableGuest;

@Entity
public class OrderTable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    private int numberOfGuests;

    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(Long id) {
        this.id = id;
    }

    public OrderTable(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public OrderTable(boolean empty) {
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public boolean canCreateTableGroup() {
        return this.isEmpty() && Objects.isNull(tableGroup);
    }

    public void includeInTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void excludeFromTableGroup() {
        this.tableGroup = null;
        this.empty = true;
    }

    public void changeEmptyStatus(Boolean empty) {
        if (Objects.nonNull(this.tableGroup)) {
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
        if (Objects.isNull(this.tableGroup)) {
            return null;
        }

        return this.tableGroup.getId();
    }
}
