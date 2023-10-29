package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuHistory;
import kitchenpos.menu.domain.MenuHistoryRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.event.OrderPreparedEvent;
import kitchenpos.order.application.request.OrderStatusUpdateRequest;
import kitchenpos.order.application.response.OrderHistoryResponse;
import kitchenpos.order.application.response.OrderResponse;
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

    private final MenuHistoryRepository menuHistoryRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderValidator orderValidator;
    private final OrderMapper orderMapper;
    private final ApplicationEventPublisher publisher;

    public OrderService(final MenuHistoryRepository menuHistoryRepository,
                        final MenuRepository menuRepository,
                        final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository,
                        final OrderValidator orderValidator,
                        final OrderMapper orderMapper,
                        final ApplicationEventPublisher publisher
    ) {
        this.menuHistoryRepository = menuHistoryRepository;
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

        publisher.publishEvent(new OrderPreparedEvent(savedOrder.getId()));
        return getOrderResponse(savedOrder);
    }

    private OrderResponse getOrderResponse(final Order order) {
        final OrderTable findOrderTable = orderTableRepository.findOrderTableById(order.getOrderTableId());
        final List<Menu> findMenus = menuRepository.findInMenuIds(getMenuIds(order));
        return OrderResponse.from(order, findOrderTable, findMenus);
    }

    private List<Long> getMenuIds(final Order order) {
        return order.getOrderLineItems()
                .getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    public List<OrderHistoryResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(this::getOrderHistoryResponse)
                .collect(Collectors.toList());
    }

    private OrderHistoryResponse getOrderHistoryResponse(final Order savedOrder) {
        final OrderTable findOrderTable = orderTableRepository.findOrderTableById(savedOrder.getOrderTableId());
        final List<MenuHistory> findMenuHistories = menuHistoryRepository.findByOrderId(savedOrder.getId());
        return OrderHistoryResponse.from(savedOrder, findOrderTable, findMenuHistories);
    }

    @Transactional
    public OrderHistoryResponse changeOrderStatus(final Long orderId, final OrderStatusUpdateRequest request) {
        final OrderStatus findOrderStatus = OrderStatus.valueOf(request.getOrderStatus());

        final Order findOrder = orderRepository.findOrderById(orderId);
        findOrder.changeOrderStatus(findOrderStatus);

        return getOrderHistoryResponse(findOrder);
    }
}
