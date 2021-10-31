package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import kitchenpos.domain.validator.TableValidator;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    private OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void updateEmpty(TableValidator tableValidator, Boolean empty) {
        tableValidator.validateUpdateEmpty(this);
        this.empty = empty;
    }

    public void updateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        if (empty) {
            throw new IllegalArgumentException();
        }
        this.numberOfGuests = numberOfGuests;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }
}
