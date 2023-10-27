package kitchenpos.domain.order;

import kitchenpos.common.BaseDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class OrderTable extends BaseDate {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable(final Long id, final Long tableGroupId, final Integer numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    protected OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final Boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public void changeEmpty(final Boolean empty, final OrderTableValidator orderTableValidator) {
        orderTableValidator.validateChangeEmpty(this);
        if (empty) {
            empty();
            return;
        }
        notEmpty();
    }

    public void changeNumberOfGuests(final int numberOfGuests, final OrderTableValidator orderTableValidator) {
        orderTableValidator.validateChangeNumberOfGuests(this);
        this.numberOfGuests = numberOfGuests;
    }

    public void ungroup(final TableGroupValidator tableGroupValidator) {
        tableGroupValidator.validate(this);
        this.tableGroupId = null;
        notEmpty();
    }

    public boolean isEmpty() {
        return empty;
    }

    public void notEmpty() {
        this.empty = false;
    }

    public void empty() {
        this.empty = true;
    }

    public Long getId() {
        return id;
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

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
}
