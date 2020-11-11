package kitchenpos.domain.order;

import kitchenpos.domain.BaseEntity;

import javax.persistence.*;
import java.util.Objects;

@AttributeOverride(name = "id", column = @Column(name = "id"))
@Table(name = "order_table")
@Entity
public class OrderTable extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Embedded
    private Empty empty;

    public OrderTable() {
    }

    public OrderTable(Long id) {
        this.id = id;
    }

    public OrderTable(TableGroup tableGroup, NumberOfGuests numberOfGuests, Empty empty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(Long id, TableGroup tableGroup, NumberOfGuests numberOfGuests, Empty empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeEmpty(final boolean empty) {
        this.empty = this.empty.compare(empty);
    }

    public void changeNumberOfGuests(final NumberOfGuests numberOfGuests) {
        if (isEmptyTable()) {
            throw new IllegalArgumentException();
        }
        this.numberOfGuests = this.numberOfGuests.compare(numberOfGuests);
    }

    public boolean isEmptyTable() {
        return this.empty.isEmpty();
    }

    public int getNumberOfGuestsCount() {
        return numberOfGuests.getNumberOfGuests();
    }

    public void groupIn(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void ungroup() {
        this.tableGroup = null;
        this.empty = Empty.of(false);
    }

    public boolean isGroupTable() {
        return Objects.nonNull(tableGroup);
    }

    public Long getContainTableGroupId() {
        return tableGroup.getId();
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void setTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public Long getId() {
        return id;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public Empty getEmpty() {
        return empty;
    }
}
