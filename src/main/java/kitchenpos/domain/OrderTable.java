package kitchenpos.domain;

import static kitchenpos.application.exception.ExceptionType.INVALID_CHANGE_NUMBER_OF_GUEST;
import static kitchenpos.application.exception.ExceptionType.INVALID_PROCEEDING_TABLE_GROUP_EXCEPTION;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.application.exception.CustomIllegalArgumentException;

@Entity
@Table(name = "order_table")
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    @Column
    private int numberOfGuests;
    @Column
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        this(null, tableGroup, numberOfGuests, empty);
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(final Long id, final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        validNumberOfGuests(numberOfGuests);
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void validTableGroupCondition() {
        if (Objects.nonNull(tableGroup)) {
            throw new CustomIllegalArgumentException(INVALID_PROCEEDING_TABLE_GROUP_EXCEPTION);
        }
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new CustomIllegalArgumentException(INVALID_CHANGE_NUMBER_OF_GUEST);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getTableGroupId() {
        return tableGroup.getId();
    }


    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void clearTable() {
        validTableGroupCondition();
        this.empty = true;
    }

    private void clearTableGroup() {
        this.tableGroup = null;
    }

    public void clear() {
        clearTableGroup();
        clearTable();
    }
}
