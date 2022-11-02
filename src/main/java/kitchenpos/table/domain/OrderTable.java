package kitchenpos.table.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import kitchenpos.order.domain.Order;
import kitchenpos.exception.EmptyOrderTableException;
import kitchenpos.exception.InvalidNumberOfGuestsException;
import kitchenpos.exception.InvalidOrderStatusException;
import kitchenpos.exception.TableGroupNullException;

@Entity
@Table(name = "order_table")
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "table_group_id")
    private Long tableGroupId;
    private int numberOfGuests;
    @OneToOne(mappedBy = "orderTable")
    private Order order;
    private boolean empty;

    protected OrderTable() {
    }

    private OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final Order order,
                      final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.order = order;
        this.empty = empty;
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, null, empty);
    }

    public boolean isNotCompleted() {
        return order != null && order.isNotComplete();
    }

    public void releaseGroup() {
        this.tableGroupId = null;
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
        if (numberOfGuests < 0) {
            throw new InvalidNumberOfGuestsException();
        }
        if (this.isEmpty()) {
            throw new EmptyOrderTableException();
        }
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    public void setEmpty(final boolean empty) {
        if (Objects.nonNull(this.getTableGroupId())) {
            throw new TableGroupNullException();
        }
        if (order != null && order.isNotComplete()) {
           throw new InvalidOrderStatusException();
        }
        this.empty = empty;
    }
}
