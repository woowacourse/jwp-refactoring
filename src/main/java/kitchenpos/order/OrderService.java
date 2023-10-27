package kitchenpos.order;

import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderPlaceableValidator orderPlaceableValidator;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository, OrderPlaceableValidator orderPlaceableValidator) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderPlaceableValidator = orderPlaceableValidator;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {
        return orderRepository.save(new Order(
                orderPlaceableValidator,
                orderRequest.getOrderTableId(),
                toOrderLineItems(orderRequest.getOrderLineItems())
        ));
    }

    @Transactional(readOnly = true)
    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order order = orderRepository.getBy(orderId);

        if (orderStatus == OrderStatus.COOKING) {
            order.cook();
        }
        if (orderStatus == OrderStatus.MEAL) {
            order.serve();
        }
        if (orderStatus == OrderStatus.COMPLETION) {
            order.complete();
        }
        return order;
    }

    private List<OrderLineItem> toOrderLineItems(List<OrderLineItemRequest> orderLineItemsRequests) {
        if (Objects.nonNull(orderLineItemsRequests)) {
            return orderLineItemsRequests.stream()
                    .map(this::toOrderLineItem)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private OrderLineItem toOrderLineItem(OrderLineItemRequest request) {
        Menu menu = menuRepository.getBy(request.getMenuId());

        return new OrderLineItem(
                request.getMenuId(),
                request.getQuantity(),
                menu.getName(),
                menu.getPrice()
        );
    }
}
