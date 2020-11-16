package kitchenpos.domain.model.order;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import kitchenpos.domain.model.menu.MenuRepository;
import kitchenpos.domain.model.ordertable.OrderTable;
import kitchenpos.domain.model.ordertable.OrderTableRepository;

@Service
public class CreateOrderVerifier {
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public CreateOrderVerifier(MenuRepository menuRepository,
            OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public Order toOrder(List<OrderLineItem> orderLineItems, Long orderTableId) {
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return new Order(null, orderTableId, null, null, orderLineItems);
    }
}
