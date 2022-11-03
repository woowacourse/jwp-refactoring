package kitchenpos.ordertable.domain;

import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.ordertable.exception.GuestNumberChangeDisabledException;
import kitchenpos.ordertable.exception.TableEmptyDisabledException;
import kitchenpos.ordertable.validator.OrderChecker;

@Entity
public class OrderTable {

    private static final String SETTING_EMPTY_DISABLED_BY_TABLE_GROUP_EXCEPTION_MESSAGE =
            "Table Group으로 묶인 테이블은 empty를 변경할 수 없습니다.";
    private static final String SETTING_EMPTY_DISABLED_BY_ORDER_NOT_COMPLETE_EXCEPTION =
            "조리중이거나 식사중인 테이블의 empty를 변경할 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private GuestNumber guestNumber;

    private boolean empty;
    private Long tableGroupId;

    protected OrderTable() {
    }

    public OrderTable(GuestNumber guestNumber, boolean empty) {
        this.guestNumber = guestNumber;
        this.empty = empty;
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

    public void setEmpty(boolean empty, OrderChecker orderChecker) {
        if (orderChecker.isNotCompletionOrder(id)) {
            throw new TableEmptyDisabledException(SETTING_EMPTY_DISABLED_BY_ORDER_NOT_COMPLETE_EXCEPTION);
        }
        if (empty) {
            validateTableGroup();
        }
        this.empty = empty;
    }

    private void validateTableGroup() {
        if (hasTableGroup()) {
            throw new TableEmptyDisabledException(SETTING_EMPTY_DISABLED_BY_TABLE_GROUP_EXCEPTION_MESSAGE);
        }
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void group(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void ungroup() {
        tableGroupId = null;
    }

    public boolean hasTableGroup() {
        return Objects.nonNull(tableGroupId);
    }
}
