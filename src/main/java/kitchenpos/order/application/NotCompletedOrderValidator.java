package kitchenpos.order.application;

import org.springframework.stereotype.Component;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.application.TableValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

@Component
public class NotCompletedOrderValidator implements TableValidator {

    private final OrderRepository orderRepository;

    public NotCompletedOrderValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateChangeStatus(OrderTable orderTable) {
        if (existNotCompletedOrder(orderTable)) {
            throw new IllegalArgumentException("주문이 완료되지 않아 상태를 변경할 수 없습니다.");
        }
    }

    private boolean existNotCompletedOrder(OrderTable orderTable) {
        return orderRepository.existsByOrderTableAndOrderStatusIn(
            orderTable, OrderStatus.listInProgress()
        );
    }

    @Override
    public void validateUngroup(TableGroup tableGroup) {
        if (existNotCompletedOrder(tableGroup)) {
            throw new IllegalArgumentException("완료되지 않은 주문이 있어서 해제할 수 없습니다.");
        }
    }

    private boolean existNotCompletedOrder(TableGroup tableGroup) {
        return orderRepository.existsByOrderTableInAndOrderStatusIn(
            tableGroup.getOrderTables(),
            OrderStatus.listInProgress()
        );
    }
}
