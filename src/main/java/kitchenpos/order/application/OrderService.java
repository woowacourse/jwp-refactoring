package kitchenpos.order.application;

import kitchenpos.dto.OrderResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.event.OrderPreparedEvent;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderValidator orderValidator;
    private final OrderMapper orderMapper;
    private final ApplicationEventPublisher publisher;

    public OrderService(final MenuRepository menuRepository,
                        final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository,
                        final OrderValidator orderValidator,
                        final OrderMapper orderMapper,
                        final ApplicationEventPublisher publisher
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderValidator = orderValidator;
        this.orderMapper = orderMapper;
        this.publisher = publisher;
    }

    @Transactional
    public OrderResponse create(final OrderSheet requestOrderSheet) {
        final Order newOrder = orderMapper.map(requestOrderSheet);
        newOrder.prepare(orderValidator);
        final Order savedOrder = orderRepository.save(newOrder);

        publisher.publishEvent(new OrderPreparedEvent(newOrder.getId()));
        return getOrderResponse(savedOrder);
    }

    private OrderResponse getOrderResponse(final Order savedOrder) {
        final OrderTable findOrderTable = orderTableRepository.findOrderTableById(savedOrder.getOrderTableId());
        final List<Menu> findMenus = savedOrder.getOrderLineItems().getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .map(menuRepository::findMenuById)
                .collect(Collectors.toList());
        return OrderResponse.from(savedOrder, findOrderTable, findMenus);
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(this::getOrderResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final String orderStatus) {
        final OrderStatus findOrderStatus = OrderStatus.valueOf(orderStatus);

        final Order findOrder = orderRepository.findOrderById(orderId);
        findOrder.changeOrderStatus(findOrderStatus);

        return getOrderResponse(findOrder);
    }
}
