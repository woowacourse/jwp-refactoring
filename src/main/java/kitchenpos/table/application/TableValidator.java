package kitchenpos.table.application;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.TableGroupRepository;
import org.springframework.stereotype.Component;

@Component
public class TableValidator {

    private final OrderRepository orderRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableValidator(OrderRepository orderRepository, TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public void validateGroup(Long tableGroupId) {
        tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("테이블 그룹이 존재하지 않습니다."));
    }

    public void validateEmpty(Long orderTableId) {
        List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);
        orders.stream()
                .filter(Order::isProgress)
                .findAny()
                .ifPresent(order -> {
                    throw new IllegalArgumentException("진행중인 주문이 있는 테이블은 수정할 수 없습니다.");
                });
    }
}
