package kitchenpos.order.application;

import static kitchenpos.exception.ExceptionType.INVALID_TABLE_UNGROUP_EXCEPTION;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;

import java.util.Arrays;
import java.util.List;
import kitchenpos.exception.CustomIllegalArgumentException;
import kitchenpos.order.domain.JpaOrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.TableValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.springframework.stereotype.Component;

@Component
public class OrderChangeValidator implements TableValidator {

    private static final List<OrderStatus> INVALID_ORDER_STATUS = Arrays.asList(COOKING, MEAL);

    private final JpaOrderRepository orderRepository;

    public OrderChangeValidator(final JpaOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateChangeStatus(final OrderTable orderTable) {
        orderTable.validTableGroupCondition();
        validExistOrderTables(orderTable);
    }

    private void validExistOrderTables(final OrderTable orderTable) {
        final boolean exists = orderRepository.existsByOrderTableAndOrderStatusIn(orderTable, INVALID_ORDER_STATUS);

        if (exists) {
            throw new CustomIllegalArgumentException(INVALID_TABLE_UNGROUP_EXCEPTION);
        }
    }

    @Override
    public void validateUngroup(final TableGroup tableGroup) {
        final boolean exists = orderRepository.existsByOrderTableInAndOrderStatusIn(tableGroup.getOrderTables(),
                INVALID_ORDER_STATUS);

        if (exists) {
            throw new CustomIllegalArgumentException(INVALID_TABLE_UNGROUP_EXCEPTION);
        }
    }
}
