package kitchenpos.domain.table;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.domain.base.BaseIdEntity;
import kitchenpos.domain.tablegroup.TableGroup;

@Entity
@javax.persistence.Table(name = "order_table")
public class Table extends BaseIdEntity {

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column
    private boolean empty;

    protected Table() {
    }

    private Table(Long id, TableGroup tableGroup, NumberOfGuests numberOfGuests, boolean empty) {
        super(id);
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static Table of(Long id, int numberOfGuests, boolean empty) {
        return new Table(id, null, new NumberOfGuests(numberOfGuests), empty);
    }

    public static Table entityOf(int numberOfGuests, boolean empty) {
        return of(null, numberOfGuests, empty);
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void setTableGroup(final TableGroup tableGroup) {
        if (Objects.nonNull(tableGroup) && Objects.nonNull(this.tableGroup)) {
            throw new IllegalArgumentException("TableGroup이 이미 존재합니다.");
        }
        if (Objects.isNull(tableGroup) && Objects.isNull(this.tableGroup)) {
            throw new IllegalArgumentException("TableGroup이 이미 존재하지 않습니다.");
        }
        this.tableGroup = tableGroup;
        if (Objects.nonNull(tableGroup)) {
            tableGroup.addTables(this);
        }
    }

    public Long getTableGroupId() {
        if (Objects.isNull(tableGroup)) {
            return null;
        }
        return this.tableGroup.getId();
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateTableNotEmpty();
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    private void validateTableNotEmpty() {
        if (empty) {
            throw new IllegalArgumentException("Table이 현재 비어있기 때문에 numberOfGuest를 설정할 수 없습니다.");
        }
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void changeEmpty(final boolean empty) {
        if (Objects.nonNull(this.tableGroup)) {
            throw new IllegalArgumentException("TableGroup이 해지되지 않았습니다.");
        }

        setEmpty(empty);
    }

    @Override
    public String toString() {
        return "OrderTable{" +
            "id=" + getId() +
            ", numberOfGuests=" + numberOfGuests +
            ", empty=" + empty +
            '}';
    }
}
