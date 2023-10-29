package domain;


import exception.TableException.TableGroupedTableCannotChangeEmptyException;
import exception.TableGroupException.HasAlreadyGroupedTableException;
import exception.TableGroupException.HasEmptyTableException;
import java.util.Objects;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import support.AggregateReference;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;
    @Embedded
    private GuestStatus guestStatus;
    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "table_group_id"))
    private AggregateReference<TableGroup> tableGroup;

    protected OrderTable() {
        this.id = null;
        this.tableGroup = null;
        this.guestStatus = GuestStatus.EMPTY_GUEST_STATUS;
    }

    public OrderTable(final GuestStatus guestStatus) {
        this.id = null;
        this.tableGroup = null;
        this.guestStatus = guestStatus;
    }

    public void group(final AggregateReference<TableGroup> tableGroup) {
        if (!guestStatus.isEmpty()) {
            throw new HasEmptyTableException();
        }
        if (isGrouped()) {
            throw new HasAlreadyGroupedTableException();
        }
        this.tableGroup = tableGroup;
    }

    public void unGroup(final TableGroupValidator validator) {
        validator.validateUnGroup(id);
        this.tableGroup = null;
    }

    public void changeEmpty(final boolean isEmpty, final OrderTableValidator orderTableValidator) {
        if (Objects.nonNull(tableGroup)) {
            throw new TableGroupedTableCannotChangeEmptyException();
        }
        orderTableValidator.validateChangeEmpty(this);
        this.guestStatus = this.guestStatus.changeEmpty(isEmpty);
    }

    public void changeNumberOfGuest(final int numberOfGuest) {
        this.guestStatus = this.guestStatus.changeNumberOfGuest(numberOfGuest);
    }

    public boolean isGrouped() {
        return tableGroup != null;
    }

    public boolean isEmpty() {
        return guestStatus.isEmpty();
    }

    public Long getId() {
        return id;
    }

    public AggregateReference<TableGroup> getTableGroup() {
        return tableGroup;
    }

    public GuestStatus getGuestStatus() {
        return guestStatus;
    }
}
