package kitchenpos.ordertable;

import static javax.persistence.GenerationType.IDENTITY;

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

    @Column
    private Long tableGroupId;

    @Column
    private int numberOfGuests;

    @Column(name = "empty")
    private boolean isEmpty;

    public OrderTable(Long tableGroupId, int numberOfGuests, boolean isEmpty) {
        this.tableGroupId = tableGroupId;
        this.isEmpty = isEmpty;
        this.numberOfGuests = numberOfGuests;
    }

    public OrderTable() {
        this(null, 0, false);
    }

    public OrderTable(int numberOfGuests, boolean isEmpty) {
        this(null, numberOfGuests, isEmpty);
    }

    public OrderTable(boolean isEmpty) {
        this(null, 0, isEmpty);
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

    public void setNumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        validateIsEmpty();
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateIsEmpty() {
        if (this.isEmpty == true) {
            throw new IllegalArgumentException();
        }
    }

    public void setTableGroupId(Long tableGroupId) {
        if (this.tableGroupId != null || this.isEmpty == false) {
            throw new IllegalArgumentException();
        }
        this.tableGroupId = tableGroupId;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void ungroup() {
        this.tableGroupId = null;
        this.isEmpty = false;
    }

    public void changeEmpty(boolean isEmpty) {
        if (isEmpty == true && tableGroupId != null) {
            throw new IllegalArgumentException();
        }
        this.isEmpty = isEmpty;
    }
}
