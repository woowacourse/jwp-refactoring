package kitchenpos.domain.order;

import kitchenpos.config.BaseEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.util.List;
import java.util.Objects;

@AttributeOverride(name = "id", column = @Column(name = "order_table_id"))
@Entity
public class OrderTable extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "FK_ORDER_TABLE_TABLE_GROUP"))
    private TableGroup tableGroup;

    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public void tableGrouping(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void tableUngrouping(List<Order> ordersByOrderTable) {
        for (Order order : ordersByOrderTable) {
            if (order.isNotComplete()) {
                throw new IllegalArgumentException();
            }
        }
        this.tableGroup = null;
        this.empty = false;
    }

    public void ValidateGrouping() {
        if (!isEmpty() || Objects.nonNull(getTableGroup())) {
            throw new IllegalArgumentException();
        }
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(final boolean empty) {
        this.empty = empty;
    }
}
