package kitchenpos.domain.order;

import kitchenpos.domain.menu.Menu;
import kitchenpos.exception.InvalidOrderLineItemDtosException;
import kitchenpos.exception.MenuNotFoundException;
import kitchenpos.repository.MenuRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderLineItemAssembler {
    private final MenuRepository menuRepository;

    public OrderLineItemAssembler(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<OrderLineItem> createOrderLineItems(OrderLineItemDtos orderLineItemDtos, Order order) {
        validateOrderLineItemDtos(orderLineItemDtos);

        return orderLineItemDtos.stream()
                .map(orderLineItemDto -> {
                    Long menuId = orderLineItemDto.getMenuId();
                    Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new MenuNotFoundException(menuId));
                    return OrderLineItem.of(order, menu, orderLineItemDto.getQuantity());
                })
                .collect(Collectors.toList());
    }

    private void validateOrderLineItemDtos(OrderLineItemDtos orderLineItemDtos) {
        List<Long> menuIds = orderLineItemDtos.getMenuIds();
        long menuCount = menuRepository.countByIdIn(menuIds);

        if (orderLineItemDtos.isNotEqualSize(menuCount)) {
            throw new InvalidOrderLineItemDtosException("주문 항목 내에서 중복되는 메뉴가 없어야 합니다!");
        }
    }
}
