package kitchenpos.application;


import kitchenpos.domain.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderValidator {

    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderValidator(OrderTableRepository orderTableRepository, MenuRepository menuRepository) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    public void validate(Order order, List<OrderLineItem> orderLineItems) {
        if (orderLineItems.size() < 1) {
            throw new IllegalArgumentException();
        }

        OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        long orderMenuSize = menuRepository.countByIdIn(menuIds);

        if (orderMenuSize != orderLineItems.size()) {
            throw new IllegalArgumentException();
        }
    }
}
