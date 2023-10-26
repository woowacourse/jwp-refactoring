package kitchenpos.domain;

import static javax.persistence.GenerationType.IDENTITY;
import static kitchenpos.exception.ExceptionType.EMPTY_ORDER_TABLE;
import static kitchenpos.exception.ExceptionType.NUMBER_OF_GUESTS;
import static kitchenpos.exception.ExceptionType.TABLE_GROUP_CANNOT_CHANGE_STATUS;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.exception.CustomException;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeEmpty(boolean empty) {
        if (tableGroup != null) {
            throw new CustomException(TABLE_GROUP_CANNOT_CHANGE_STATUS);
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new CustomException(NUMBER_OF_GUESTS);
        }
        if (isEmpty()) {
            throw new CustomException(EMPTY_ORDER_TABLE);
        }

        this.numberOfGuests = numberOfGuests;
    }

    public void ungroup() {
        tableGroup = null;
        empty = false;
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

    public void setTableGroup(TableGroup tableGroup) {
        this.empty = false;
        this.tableGroup = tableGroup;
    }

    public static class Builder {

        private Long id;
        private TableGroup tableGroup;
        private int numberOfGuests;
        private boolean empty;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setTableGroup(TableGroup tableGroup) {
            this.tableGroup = tableGroup;
            return this;
        }

        public Builder setNumberOfGuests(int numberOfGuests) {
            this.numberOfGuests = numberOfGuests;
            return this;
        }

        public Builder setEmpty(boolean empty) {
            this.empty = empty;
            return this;
        }

        public OrderTable build() {
            return new OrderTable(id, tableGroup, numberOfGuests, empty);
        }
    }
}
