package kitchenpos.order.domain.service;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.model.Order;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.model.OrderTable;
import kitchenpos.table.domain.model.TableGroup;
import kitchenpos.table.domain.service.TableUngroupValidator;
import org.springframework.stereotype.Service;

@Service
public class TableUngroupOrderValidator implements TableUngroupValidator {

    private final OrderRepository orderRepository;

    public TableUngroupOrderValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validate(TableGroup tableGroup) {
        List<OrderTable> orderTables = tableGroup.getOrderTables();
        validateOrderTables(orderTables);
        validateOrders(orderTables);
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        orderTables.stream()
            .filter(OrderTable::isAbleToUngroup)
            .findAny()
            .ifPresent(orderTable -> {
                throw new IllegalArgumentException("그룹 해제할 수 없습니다.");
            });
    }

    private void validateOrders(List<OrderTable> orderTables) {
        List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
        List<Order> orders = orderRepository.findAllByOrderTableIdIn(orderTableIds);
        if (!orders.stream().allMatch(Order::isAbleToUngroup)) {
            throw new IllegalArgumentException("그룹 해제할 수 없습니다.");
        }
    }
}
