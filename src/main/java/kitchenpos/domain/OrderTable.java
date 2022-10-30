package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 매장에서 주문이 발생하는 영역
 * emptyTable: 주문을 등록할 수 없는 OrderTable
 * numberOfGuests: 방문한 손님 수. 필수는 아니며 Order 는 0명으로 등록할 수 있다.
 */
@Entity
@Table(name = "order_table")
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    @Column(name = "number_of_guests")
    private int numberOfGuests;

    @Column(name = "empty")
    private boolean empty;

    @OneToMany(mappedBy = "orderTableId")
    List<Order> orders = new ArrayList<>();

    protected OrderTable() {
    }

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        validateNumberOfGuests(numberOfGuests);
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    public void addOrder(final Order order) {
        orders.add(order);
    }

    public void group(final Long tableGroupId) {
        changeEmpty(false);
        this.tableGroupId = tableGroupId;
    }

    public void ungroup() {
        this.tableGroupId = null;
        changeEmpty(false);
    }

    public void validateCanBeGrouped() {
        if (!empty || tableGroupId != null) {
            throw new IllegalArgumentException("table can not be grouped");
        }
    }

    public void changeEmpty(final boolean empty) {
        validateCanChangeEmpty();
        if (tableGroupId != null) {
            throw new IllegalArgumentException("could not change empty, table is now grouped");
        }
        this.empty = empty;
    }

    private void validateCanChangeEmpty() {
        for (final Order order : orders) {
            validateOrderCompletion(order);
        }
    }

    private void validateOrderCompletion(final Order order) {
        if (order.isCompletion()) {
            throw new IllegalArgumentException("can not change to empty, status is not completion");
        }
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        if (empty) {
            throw new IllegalArgumentException();
        }
        this.numberOfGuests = numberOfGuests;
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

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final OrderTable that = (OrderTable)o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
