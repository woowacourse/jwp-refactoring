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

    public void changeEmpty(boolean empty) {
        validate(Objects.nonNull(this.tableGroup) && empty, "TableGroup이 해지되지 않았습니다.");
        this.empty = empty;
    }

    private void validate(boolean condition, String exceptionMessage) {
        if (condition) {
            throw new IllegalArgumentException(exceptionMessage);
        }
    }

    public void unGroup() {
        if (tableGroup.contains(this)) {
            tableGroup.removeTable(this);
        }
        this.tableGroup = null;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void setTableGroup(final TableGroup tableGroup) {
        if (Objects.nonNull(tableGroup)) {
            validate(Objects.nonNull(this.tableGroup), "TableGroup이 이미 존재합니다.");
            tableGroup.addTable(this);
        }
        if (Objects.isNull(tableGroup)) {
            validate(Objects.isNull(this.tableGroup), "TableGroup이 이미 존재하지 않습니다.");
            removeTableOfTableGroup();
        }
        this.tableGroup = tableGroup;
    }

    private void removeTableOfTableGroup() {
        if (this.tableGroup.hasTable(this)) {
            this.tableGroup.removeTable(this);
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
        validate(empty, "Table이 현재 비어있기 때문에 numberOfGuest를 설정할 수 없습니다.");
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public boolean hasTableGroup() {
        return Objects.nonNull(tableGroup);
    }

    public boolean isEmpty() {
        return empty;
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
