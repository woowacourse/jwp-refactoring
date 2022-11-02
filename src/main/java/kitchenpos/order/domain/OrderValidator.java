package kitchenpos.order.domain;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.dto.request.CreateOrderLineItemRequest;
import kitchenpos.order.dto.request.CreateOrderRequest;
import kitchenpos.order.repository.OrderTableRepository;

@Component
public class OrderValidator {
    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderValidator(OrderTableRepository orderTableRepository, MenuRepository menuRepository) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    public void validateCreateOrder(final CreateOrderRequest request) {
        validateOrderTableById(request.getOrderTableId());
        validateMenusById(request.getOrderLineItems());
    }

    private void validateOrderTableById(final Long orderTableId) {
        orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블 입니다."));
    }

    private void validateMenusById(final List<CreateOrderLineItemRequest> orderLineItems) {
        final List<Long> menuIds = orderLineItems.stream()
            .map(CreateOrderLineItemRequest::getMenuId)
            .collect(Collectors.toList());

        final List<Menu> menus = menuRepository.findAllByIdIn(menuIds);

        if (menuIds.size() != menus.size()) {
            throw new IllegalArgumentException("존재하지 않는 메뉴입니다.");
        }
    }

}
