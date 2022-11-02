package kitchenpos.order.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.application.OrderLineItemDto;
import kitchenpos.order.dto.request.ChangeOrderStatusRequest;
import kitchenpos.order.dto.request.CreateOrderLineItemRequest;
import kitchenpos.order.dto.request.CreateOrderRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.order.repository.OrderRepository;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final OrderValidator orderValidator;

    public OrderService(OrderRepository orderRepository, MenuRepository menuRepository, OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final CreateOrderRequest request) {
        orderValidator.validateCreateOrder(request);

        final Map<Long, Menu> menus = getMenus(request.getOrderLineItems());
        final List<OrderLineItemDto> orderLineItems = new ArrayList<>();
        for (CreateOrderLineItemRequest orderLineItem : request.getOrderLineItems()) {
            final Menu menu = menus.get(orderLineItem.getMenuId());
            final OrderLineItemDto orderLineItemDto = new OrderLineItemDto(
                menu.getName(),
                menu.getPrice(),
                orderLineItem.getQuantity()
            );

            orderLineItems.add(orderLineItemDto);
        }

        final Order order = orderRepository.save(new Order(request.getOrderTableId(), orderLineItems));

        return new OrderResponse(order);
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
            .map(OrderResponse::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest request) {
        final Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 주문입니다."));

        order.changeStatus(request.getStatus());

        return new OrderResponse(order);
    }

    private Map<Long, Menu> getMenus(final List<CreateOrderLineItemRequest> orderLineItems) {
        final List<Long> menuIds = orderLineItems.stream()
            .map(CreateOrderLineItemRequest::getMenuId)
            .collect(Collectors.toList());

        final List<Menu> menus = menuRepository.findAllByIdIn(menuIds);

        if (menuIds.size() != menus.size()) {
            throw new IllegalArgumentException("존재하지 않는 메뉴입니다.");
        }

        return menus.stream()
            .collect(Collectors.toMap(it -> it.getId(), it -> it));
    }

}
