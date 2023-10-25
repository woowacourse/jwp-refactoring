package kitchenpos.application.order;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderMenu;
import kitchenpos.domain.order.OrderMenuProduct;
import kitchenpos.dto.order.OrderCreateRequest;
import kitchenpos.dto.order.OrderLineItemRequest;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    private final MenuRepository menuRepository;

    public OrderMapper(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public Order toOrder(OrderCreateRequest request) {
        Long orderTableId = request.getOrderTableId();
        return request.getOrderLineItems().stream()
                .map(this::toOrderLineItem)
                .collect(Collectors.collectingAndThen(toList(), items -> new Order(orderTableId, items)));
    }

    private OrderLineItem toOrderLineItem(OrderLineItemRequest request) {
        return new OrderLineItem(null, request.getMenuId(), request.getQuantity(), toOrderMenu(request));
    }

    private OrderMenu toOrderMenu(OrderLineItemRequest request) {
        Menu menu = menuRepository.getById(request.getMenuId());
        return new OrderMenu(menu.getName(), menu.getPrice(), toOrderMenuProducts(menu.getMenuProducts()));
    }

    private List<OrderMenuProduct> toOrderMenuProducts(MenuProducts menuProducts) {
        return menuProducts.getItems().stream()
                .map(item -> new OrderMenuProduct(item.getName(), item.getPrice(), item.getQuantity()))
                .collect(toList());
    }
}
