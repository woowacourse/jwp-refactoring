package kitchenpos.domain.table;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.exception.CustomError;
import kitchenpos.exception.DomainLogicException;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    @Embedded
    private TableStatus status;

    protected OrderTable() {
    }

    public OrderTable(final Long id) {
        this(id, null, null);
    }

    public OrderTable(final TableStatus status) {
        this(null, null, status);
    }

    public OrderTable(final Long tableGroupId, final TableStatus status) {
        this(null, tableGroupId, status);
    }

    public OrderTable(final Long id, final Long tableGroupId, final TableStatus status) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.status = status;
    }

    public void changeEmpty(final boolean empty, final OrderTableValidator validator) {
        validateUngrouped();
        validator.validateAllOrderCompleted(this.id);
        status.changeEmpty(empty);
    }

    private void validateUngrouped() {
        if (isGrouped()) {
            throw new DomainLogicException(CustomError.TABLE_ALREADY_GROUPED_ERROR);
        }
    }

    public void changeGuestNumber(final int number) {
        status.changeGuestNumber(number);
    }

    public void changeTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void ungroup() {
        this.tableGroupId = null;
        this.status.changeEmpty(false);
    }

    public void validateNotEmpty() {
        if (status.isEmpty()) {
            throw new DomainLogicException(CustomError.ORDER_TABLE_EMPTY_ERROR);
        }
    }

    public boolean isGrouped() {
        return this.tableGroupId != null;
    }

    public boolean isEmpty() {
        return status.isEmpty();
    }

    public Long getId() {
        return this.id;
    }

    public int getGuestNumber() {
        return this.status.getNumberOfGuests();
    }

    public Long getTableGroupId() {
        return this.tableGroupId;
    }
}
