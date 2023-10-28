package kitchenpos.ordertable.domain;

import static javax.persistence.GenerationType.IDENTITY;
import static kitchenpos.exception.ExceptionType.EMPTY_ORDER_TABLE;
import static kitchenpos.exception.ExceptionType.NUMBER_OF_GUESTS;
import static kitchenpos.exception.ExceptionType.TABLE_GROUP_CANNOT_CHANGE_STATUS;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import kitchenpos.exception.CustomException;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column(name = "table_group_id", insertable = false, updatable = false)
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeEmpty(boolean empty) {
        if (tableGroupId != null) {
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

    public void group(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        empty = false;
    }

    public void ungroup() {
        this.tableGroupId = null;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public static class Builder {

        private Long id;
        private Long tableGroupId;
        private int numberOfGuests;
        private boolean empty;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setTableGroupId(Long tableGroupId) {
            this.tableGroupId = tableGroupId;
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
            return new OrderTable(id, tableGroupId, numberOfGuests, empty);
        }
    }
}
