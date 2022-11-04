package kitchenpos.domain.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.domain.common.NumberOfGuests;
import kitchenpos.exception.badrequest.CookingOrMealOrderTableCannotChangeEmptyException;
import kitchenpos.exception.badrequest.CookingOrMealOrderTableCannotUngroupedException;
import kitchenpos.exception.badrequest.EmptyTableCannotChangeNumberOfGuestsException;
import kitchenpos.exception.badrequest.GroupedTableCannotChangeEmptyException;
import org.hibernate.annotations.BatchSize;

@Entity
@Table(name = "order_table")
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    @Embedded
    private NumberOfGuests numberOfGuests;
    @Column(name = "empty", nullable = false)
    private boolean empty;
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "orderTable", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<OrderStatusRecord> orderStatusRecords = new ArrayList<>();

    protected OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, new NumberOfGuests(numberOfGuests), empty);
    }

    public OrderTable(final Long id, final TableGroup tableGroup, final NumberOfGuests numberOfGuests,
                      final boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeEmpty(final boolean empty) {
        validateNotGrouped();
        validateCookingOrMealOrderNotExistsWhenChangeEmpty();
        this.empty = empty;
    }

    private void validateCookingOrMealOrderNotExistsWhenChangeEmpty() {
        if (orderStatusRecords.stream().anyMatch(OrderStatusRecord::isNotCompleted)) {
            throw new CookingOrMealOrderTableCannotChangeEmptyException();
        }
    }

    private void validateNotGrouped() {
        if (tableGroup != null) {
            throw new GroupedTableCannotChangeEmptyException();
        }
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateNotEmpty();
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    private void validateNotEmpty() {
        if (empty) {
            throw new EmptyTableCannotChangeNumberOfGuestsException();
        }
    }

    public boolean isGrouped() {
        return this.tableGroup != null;
    }

    public void designateTableGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void ungroup() {
        validateCookingOrMealOrderNotExistsWhenUngroup();
        this.tableGroup = null;
        this.empty = false;
    }

    private void validateCookingOrMealOrderNotExistsWhenUngroup() {
        if (orderStatusRecords.stream().anyMatch(OrderStatusRecord::isNotCompleted)) {
            throw new CookingOrMealOrderTableCannotUngroupedException();
        }
    }

    public void add(final OrderStatusRecord orderStatusRecord) {
        orderStatusRecords.add(orderStatusRecord);
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getValue();
    }

    public boolean isEmpty() {
        return empty;
    }

    public List<OrderStatusRecord> getOrderStatusRecords() {
        return orderStatusRecords;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderTable)) {
            return false;
        }
        OrderTable that = (OrderTable) o;
        return Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
