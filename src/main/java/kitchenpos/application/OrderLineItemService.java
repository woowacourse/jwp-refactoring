package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.repository.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.repository.OrderLineItemRepository;
import kitchenpos.dto.order.OrderLineItemRequests;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderLineItemService {
    private final OrderLineItemRepository orderLineItemRepository;
    private final MenuRepository menuRepository;
    private final ProductService productService;

    @Transactional
    public void createOrderLineItems(Order order, OrderLineItemRequests orderLineItemRequests) {
        final List<Long> menuIds = orderLineItemRequests.getMenuIds();
        final Map<Long, Long> menuQuantityMatcher = orderLineItemRequests.getMenuQuantityMatcher();
        final List<Menu> menus = menuRepository.findAllById(menuIds);

        if (menus.isEmpty() || menus.size() != menuIds.size()) {
            throw new IllegalArgumentException("주문 상품의 조회가 잘못되었습니다.");
        }

        for (final Menu menu : menus) {
            Long menuId = menu.getId();
            Long quantity = menuQuantityMatcher.get(menuId);
            OrderLineItem orderLineItem = new OrderLineItem(order, menu, quantity);
            orderLineItemRepository.save(orderLineItem);
        }
    }
}
