package kitchenpos.domain.order;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.dto.request.CreateOrderRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static kitchenpos.dto.request.CreateOrderRequest.CreateOrderLineItem;

@Component
public class OrderMapper {

    private final MenuRepository menuRepository;

    private OrderMapper(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public Order toOrder(CreateOrderRequest request) {
        return Order.builder()
                .orderTableId(request.getOrderTableId())
                .orderLineItems(getOrderLineItems(request.getOrderLineItems()))
                .orderedTime(LocalDateTime.now())
                .orderStatus(OrderStatus.COOKING)
                .build();
    }

    private Set<OrderLineItem> getOrderLineItems(List<CreateOrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(this::toOrderLineItem)
                .collect(Collectors.toSet());
    }

    private OrderLineItem toOrderLineItem(CreateOrderLineItem request) {
        Menu menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));

        return OrderLineItem.builder()
                .menuId(request.getMenuId())
                .quantity(request.getQuantity())
                .name(menu.getName())
                .price(menu.getPrice())
                .build();
    }
}
