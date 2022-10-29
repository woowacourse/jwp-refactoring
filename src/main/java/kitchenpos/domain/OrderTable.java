package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    @Column(name = "number_of_guests", nullable = false)
    private int numberOfGuests;

    @Column(name = "enmpty", nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void updateEmpty(boolean empty) {
        checkTableGroupIsNull();
        this.empty = empty;
    }

    public void updateNumberOfGuests(int numberOfGuests) {
        checkEmpty();
        checkNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void checkNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    public void checkCanGroup() {
        checkTableGroupIsNull();
        checkNotEmpty();
    }

    private void checkTableGroupIsNull() {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    public void checkEmpty() {
        if (empty) {
            throw new IllegalArgumentException();
        }
    }

    private void checkNotEmpty() {
        if (!empty) {
            throw new IllegalArgumentException();
        }
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

    public void setTableGroupId(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
}
