package kitchenpos.domain.table;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.tablegroup.TableGroupRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static kitchenpos.domain.order.OrderStatus.COOKING;
import static kitchenpos.domain.order.OrderStatus.MEAL;

@Component
public class OrderTableValidator {

    private final OrderRepository orderRepository;
    private final TableGroupRepository tableGroupRepository;

    public OrderTableValidator(final OrderRepository orderRepository, TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public void validateUpdateEmpty(final OrderTable orderTable) {
        validateOrderStatus(orderTable.getId());
        if (orderTable.getTableGroupId() != null && tableGroupRepository.existsById(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException("단체 지정된 테이블은 상태를 변경할 수 없습니다.");
        }
    }

    private void validateOrderStatus(final Long orderTableId) {
        final Optional<Order> order = orderRepository.findByOrderTableId(orderTableId);
        if (order.isEmpty()) {
            return;
        }
        if (order.get().getOrderStatus() == COOKING || order.get().getOrderStatus() == MEAL) {
            throw new IllegalArgumentException("주문 상태가 조리중 또는 식사중인 테이블은 빈 테이블로 변경할 수 없습니다.");
        }
    }

    public void validateUpdateGuestNumber(final OrderTable orderTable) {
        if (orderTable.getNumberOfGuests() < 0) {
            throw new IllegalArgumentException("방문한 손님 수는 음수가 될 수 없습니다.");
        }
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블에는 손님을 지정할 수 없습니다.");
        }
    }
}
