package kitchenpos.domain.table;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static kitchenpos.exception.table.OrderTableExceptionType.CAN_NOT_CHANGE_EMPTY_GROUPED_ORDER_TABLE;
import static kitchenpos.exception.table.OrderTableExceptionType.CAN_NOT_CHANGE_NUMBER_OF_GUESTS_EMPTY_ORDER_TABLE;
import static kitchenpos.exception.table.OrderTableExceptionType.NUMBER_OF_GUESTS_CAN_NOT_NEGATIVE;
import static kitchenpos.exception.table.TableGroupExceptionType.ORDER_TABLE_CAN_NOT_EMPTY;
import static kitchenpos.exception.table.TableGroupExceptionType.ORDER_TABLE_CAN_NOT_HAVE_TABLE_GROUP;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.exception.table.OrderTableException;
import kitchenpos.exception.table.TableGroupException;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable emptyTable() {
        return new OrderTable(0, true);
    }

    public void group(TableGroup tableGroup) {
        if (!empty) {
            throw new TableGroupException(ORDER_TABLE_CAN_NOT_EMPTY);
        }
        if (Objects.nonNull(this.tableGroup)) {
            throw new TableGroupException(ORDER_TABLE_CAN_NOT_HAVE_TABLE_GROUP);
        }
        this.tableGroup = tableGroup;
        empty = false;
    }

    public void ungroup() {
        tableGroup = null;
        empty = false;
    }

    public void changeEmpty(boolean empty) {
        if (Objects.nonNull(tableGroup)) {
            throw new OrderTableException(CAN_NOT_CHANGE_EMPTY_GROUPED_ORDER_TABLE);
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new OrderTableException(NUMBER_OF_GUESTS_CAN_NOT_NEGATIVE);
        }
        if (empty) {
            throw new OrderTableException(CAN_NOT_CHANGE_NUMBER_OF_GUESTS_EMPTY_ORDER_TABLE);
        }
        this.numberOfGuests = numberOfGuests;
    }

    public Long id() {
        return id;
    }

    public TableGroup tableGroup() {
        return tableGroup;
    }

    public int numberOfGuests() {
        return numberOfGuests;
    }

    public boolean empty() {
        return empty;
    }
}
