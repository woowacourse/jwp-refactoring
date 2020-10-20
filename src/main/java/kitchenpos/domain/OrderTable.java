package kitchenpos.domain;

import javax.persistence.*;

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
        this.numberOfGuests = this.numberOfGuests.compare(numberOfGuests);
    }

    public int getNumberOfGuestsCount() {
        return numberOfGuests.getNumberOfGuests();
    }

    public boolean isEmptyTable() {
        return this.empty.isEmpty();
    }

    public void groupIn(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void ungroup() {
        this.tableGroup = null;
        this.empty = Empty.of(false);
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

    public void setId(final Long id) {
        this.id = id;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(NumberOfGuests numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Empty getEmpty() {
        return empty;
    }

    public void setEmpty(Empty empty) {
        this.empty = empty;
    }
}
