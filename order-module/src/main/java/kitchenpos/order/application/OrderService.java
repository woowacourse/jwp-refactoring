package kitchenpos.order.application;

import kitchenpos.order.application.event.OrderPreparedEvent;
import kitchenpos.order.application.request.OrderStatusUpdateRequest;
import kitchenpos.order.application.response.OrderHistoryResponse;
import kitchenpos.order.domain.MenuHistory;
import kitchenpos.order.domain.MenuHistoryRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private final MenuHistoryRepository menuHistoryRepository;
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final OrderMapper orderMapper;
    private final ApplicationEventPublisher publisher;

    public OrderService(final MenuHistoryRepository menuHistoryRepository,
                        final OrderRepository orderRepository,
                        final OrderValidator orderValidator,
                        final OrderMapper orderMapper,
                        final ApplicationEventPublisher publisher
    ) {
        this.menuHistoryRepository = menuHistoryRepository;
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
        this.orderMapper = orderMapper;
        this.publisher = publisher;
    }

    @Transactional
    public OrderHistoryResponse create(final OrderSheet requestOrderSheet) {
        final Order newOrder = orderMapper.map(requestOrderSheet);
        newOrder.prepare(orderValidator);
        final Order savedOrder = orderRepository.save(newOrder);

        publisher.publishEvent(new OrderPreparedEvent(savedOrder.getId()));
        return getOrderHistoryResponse(savedOrder);
    }

    public List<OrderHistoryResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(this::getOrderHistoryResponse)
                .collect(Collectors.toList());
    }

    private OrderHistoryResponse getOrderHistoryResponse(final Order savedOrder) {
        final List<MenuHistory> findMenuHistories = menuHistoryRepository.findByOrderId(savedOrder.getId());
        return OrderHistoryResponse.from(savedOrder, findMenuHistories);
    }

    @Transactional
    public OrderHistoryResponse changeOrderStatus(final Long orderId, final OrderStatusUpdateRequest request) {
        final OrderStatus findOrderStatus = OrderStatus.valueOf(request.getOrderStatus());

        final Order findOrder = orderRepository.findOrderById(orderId);
        findOrder.changeOrderStatus(findOrderStatus);

        return getOrderHistoryResponse(findOrder);
    }
}
