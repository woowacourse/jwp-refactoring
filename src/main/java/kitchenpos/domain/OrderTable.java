package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import kitchenpos.exception.GuestNumberChangeDisabledException;
import kitchenpos.exception.TableEmptyDisabledException;

@Entity
public class OrderTable {

    private static final String SETTING_EMPTY_DISABLED_BY_TABLE_GROUP_EXCEPTION_MESSAGE =
            "Table Group으로 묶인 테이블은 empty를 변경할 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private GuestNumber guestNumber;

    private boolean empty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "orderTable")
    private Order order;

    protected OrderTable() {
    }

    public OrderTable(Long id, GuestNumber guestNumber, boolean empty, TableGroup tableGroup) {
        this.id = id;
        this.guestNumber = guestNumber;
        this.empty = empty;
        this.tableGroup = tableGroup;
    }

    public Long getId() {
        return id;
    }

    public int getGuestNumber() {
        return guestNumber.getValue();
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeGuestNumber(GuestNumber guestNumber) {
        validateEmptiness();
        this.guestNumber = guestNumber;
    }

    private void validateEmptiness() {
        if (empty) {
            throw new GuestNumberChangeDisabledException();
        }
    }

    public void setEmpty(boolean empty) {
        validateOrderStatusIfExists();
        validateTableGroup();
        this.empty = empty;
    }

    private void validateOrderStatusIfExists() {
        Order order = getOrder();
        if (Objects.nonNull(order) && order.isNotCompletionOrderStatus()) {
            throw new IllegalArgumentException("조리중이거나 식사중인 테이블의 empty를 변경할 수 없습니다.");
        }
    }

    private void validateTableGroup() {
        if (hasTableGroup()) {
            throw new TableEmptyDisabledException(SETTING_EMPTY_DISABLED_BY_TABLE_GROUP_EXCEPTION_MESSAGE);
        }
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void setTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void ungroup() {
        tableGroup = null;
    }

    public Order getOrder() {
        return order;
    }

    public boolean hasTableGroup() {
        return Objects.nonNull(tableGroup);
    }

    public boolean isNotCompletionOrderTable() {
        if (Objects.isNull(order)) {
            return false;
        }
        return order.isNotCompletionOrderStatus();
    }
}
