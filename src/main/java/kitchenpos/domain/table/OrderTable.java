package kitchenpos.domain.table;

import static kitchenpos.exception.TableException.NotCompletionTableCannotChangeEmptyException;
import static kitchenpos.exception.TableException.TableGroupedTableCannotChangeEmptyException;
import static kitchenpos.exception.TableGroupException.CannotUngroupException;

import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.Orders;
import kitchenpos.domain.table_group.TableGroup;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;
    @Embedded
    private GuestStatus guestStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup;
    @Embedded
    private final Orders orders;

    protected OrderTable() {
        this.id = null;
        this.tableGroup = null;
        this.guestStatus = GuestStatus.EMPTY_GUEST_STATUS;
        this.orders = Orders.EMPTY_ORDERS();
    }

    public OrderTable(final GuestStatus guestStatus) {
        this.id = null;
        this.tableGroup = null;
        this.guestStatus = guestStatus;
        this.orders = Orders.EMPTY_ORDERS();
    }

    public void group(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void unGroup() {
        validateUnGroup();
        this.tableGroup = null;
    }

    private void validateUnGroup() {
        if (orders.hasStatusOf(OrderStatus.COOKING) || orders.hasStatusOf(OrderStatus.MEAL)) {
            throw new CannotUngroupException();
        }
    }

    public void order(final Order order) {
        orders.add(order);
    }

    public void changeEmpty(final boolean isEmpty) {
        if (orders.isEmpty() || orders.isAllStatusOf(OrderStatus.COMPLETION)) {
            this.guestStatus = this.guestStatus.changeEmpty(isEmpty);
            return;
        }

        if (Objects.nonNull(tableGroup)) {
            throw new TableGroupedTableCannotChangeEmptyException();
        }

        throw new NotCompletionTableCannotChangeEmptyException();
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

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public GuestStatus getGuestStatus() {
        return guestStatus;
    }
}
