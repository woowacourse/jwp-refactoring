package kitchenpos.order.application;

import static kitchenpos.exception.ExceptionType.NOT_FOUND_ORDER_EXCEPTION;

import java.util.Optional;
import kitchenpos.exception.CustomIllegalArgumentException;
import kitchenpos.order.domain.JpaOrderRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.table.application.TableValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.springframework.stereotype.Component;

@Component
public class OrderChangeValidator implements TableValidator {

    private final JpaOrderRepository orderRepository;

    public OrderChangeValidator(final JpaOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateChangeStatus(final OrderTable orderTable) {
        orderTable.validTableGroupCondition();
        validExistOrderTables(orderTable.getId());
    }

    private void validExistOrderTables(final Long orderTableId) {
        final Optional<Order> order = orderRepository.findByOrderTableId(orderTableId);
        order.ifPresent(Order::validExistOrderStatus);
    }

    @Override
    public void validateUngroup(final TableGroup tableGroup) {
        for (OrderTable orderTable : tableGroup.getOrderTables()) {
            final Order order = orderRepository.findById(orderTable.getId())
                    .orElseThrow(() -> new CustomIllegalArgumentException(NOT_FOUND_ORDER_EXCEPTION));
            order.validExistOrderStatus();
        }
    }
}
