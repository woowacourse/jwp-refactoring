package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.repository.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.repository.OrderLineItemRepository;
import kitchenpos.dto.order.OrderLineItemRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderLineItemService {
    private final OrderLineItemRepository orderLineItemRepository;
    private final MenuRepository menuRepository;

    @Transactional
    public void createOrderLineItems(Order order, List<OrderLineItemRequest> orderLineItemRequests) {
        validate(order, orderLineItemRequests);
        final List<OrderLineItem> orderLineItems = orderLineItemRequests.stream()
                .map(request -> {
                    Long menuId = request.getMenuId();
                    Long quantity = request.getQuantity();
                    Menu menu = menuRepository.findById(menuId)
                            .orElseThrow(() -> new IllegalArgumentException("주문 메뉴를 찾을 수 없습니다."));
                    return new OrderLineItem(order, menu, quantity);
                })
                .collect(Collectors.toList());
        orderLineItemRepository.saveAll(orderLineItems);
    }

    private void validate(Order order, List<OrderLineItemRequest> orderLineItemRequests) {
        if (Objects.isNull(order)) {
            throw new IllegalArgumentException("잘못된 주문이 입력되었습니다.");
        }
        if (Objects.isNull(orderLineItemRequests) || orderLineItemRequests.isEmpty()) {
            throw new IllegalArgumentException("잘못된 메뉴가 입력되었습니다.");
        }
    }
}
