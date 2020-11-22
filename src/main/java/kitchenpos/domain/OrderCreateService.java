package kitchenpos.domain;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class OrderCreateService {
    private final MenuRepository menuRepository;
    private final TableRepository tableRepository;

    public OrderCreateService(MenuRepository menuRepository, TableRepository tableRepository) {
        this.menuRepository = menuRepository;
        this.tableRepository = tableRepository;
    }

    public Order create(Long orderTableId, List<OrderLineItemCreateInfo> orderLineItems) {
        verifyFindAllMenu(orderLineItems);

        OrderTable orderTable = tableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);
        verifyTableNotEmpty(orderTable);

        return Order.place(orderTableId, orderLineItems.stream().map(OrderLineItemCreateInfo::toOrderLineItem).collect(
            Collectors.toList()));
    }

    private void verifyTableNotEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void verifyFindAllMenu(List<OrderLineItemCreateInfo> orderLineItems) {
        List<Long> menuIds = orderLineItems.stream()
            .map(OrderLineItemCreateInfo::getMenuId)
            .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }
}
