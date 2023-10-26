package kitchenpos.order.service;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.exception.NoSuchDataException;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.type.OrderStatus;
import kitchenpos.order.dto.CreatedOrderEvent;
import kitchenpos.order.dto.OrderLineItemsDto;
import kitchenpos.order.dto.OrderTableIdValidateEvent;
import kitchenpos.order.dto.request.ChangeOrderRequest;
import kitchenpos.order.dto.request.CreateOrderRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.repository.OrderRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final ApplicationEventPublisher publisher;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final ApplicationEventPublisher publisher
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.publisher = publisher;
    }

    public OrderResponse create(final CreateOrderRequest request) {
        final List<OrderLineItemsDto> orderLineItemDtos = request.getOrderLineItems();

        publisher.publishEvent(request);

        final List<Long> menuIds = orderLineItemDtos.stream()
                .map(OrderLineItemsDto::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItemDtos.size() != menuRepository.countByIdIn(menuIds)) {
            throw new NoSuchDataException("입력한 메뉴들이 일치하지 않습니다.");
        }

        publisher.publishEvent(new OrderTableIdValidateEvent(request.getOrderTableId()));

        final Order order = Order.from(request.getOrderTableId());

        final Long orderId = orderRepository.save(order).getId();

        final CreatedOrderEvent event = new CreatedOrderEvent(request.getOrderLineItems(), orderId);

        publisher.publishEvent(event);

        final List<OrderLineItem> orderLineItems = event.getOrderLineItemsDtos().stream()
                .map(OrderLineItem::from)
                .collect(Collectors.toList());

        orderLineItemRepository.saveAll(orderLineItems);

        return OrderResponse.from(Order.of(order, orderLineItems));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchDataException("해당하는 id의 주문이 존재하지 않습니다."));

        savedOrder.validateIsComplete();

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());

        savedOrder.changeOrderStatus(orderStatus);

        return OrderResponse.from(savedOrder);
    }
}
