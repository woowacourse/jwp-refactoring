package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import kitchenpos.table.application.response.OrderTableResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.application.OrderTableCondition;
import kitchenpos.table.application.TableService;
import org.springframework.stereotype.Component;

@Component
public class OrderTableConditionInjector {

    private final OrderTableCondition orderTableCondition;
    private final TableService tableService;
    private final OrderRepository orderRepository;

    public OrderTableConditionInjector(
        OrderTableCondition orderTableCondition, TableService tableService,
        OrderRepository orderRepository) {
        this.orderTableCondition = orderTableCondition;
        this.tableService = tableService;
        this.orderRepository = orderRepository;
    }

    @PostConstruct
    public void act() {
        orderTableCondition.addUngroupCondition(tableGroupId -> {
            final List<Long> orderTableIds = tableService.findAllByTableGroupId(tableGroupId)
                .stream()
                .map(OrderTableResponse::getId)
                .collect(Collectors.toList());

            return orderRepository.findByOrderTableIds(orderTableIds)
                .stream()
                .anyMatch(Order::unableUngroup);
        });

        orderTableCondition.addChangeCondition(orderTableId ->
            orderRepository.findByOrderTableId(orderTableId)
                .stream()
                .anyMatch(Order::unableUngroup));
    }
}
