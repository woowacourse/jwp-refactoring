package kitchenpos.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.entity.OrderTable;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderTableRepository;

@Service
public class OrderCreateService {
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderCreateService(MenuRepository menuRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(List<OrderLineItem> orderLineItems, Long orderTableId) {
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
    }
}
