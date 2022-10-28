package kitchenpos.application;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderLineItemRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemDao orderLineItemDao,
            final OrderTableDao orderTableDao
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public Order create(final OrderCreateRequest orderRequest) {
        final OrderTable orderTable = orderTableDao.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));
        orderTable.validateEmpty();

        final List<Long> menuIds = extractMenuIds(orderRequest);
        final Order order = Order.create(orderRequest.getOrderTableId(), mapToOrderLineItem(orderRequest),
                menuRepository.countByIdIn(menuIds));

        return orderRepository.save(order);
    }

    private List<Long> extractMenuIds(final OrderCreateRequest orderRequest) {
        return orderRequest.getOrderLineItemRequests()
                .stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }

    private List<OrderLineItem> mapToOrderLineItem(final OrderCreateRequest orderRequest) {
        return orderRequest.getOrderLineItemRequests()
                .stream()
                .map(OrderLineItemRequest::toEntity)
                .collect(Collectors.toList());
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문번호입니다."));

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException("이미 완료된 주문입니다.");
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());
        savedOrder.setOrderStatus(orderStatus.name());

        orderRepository.save(savedOrder);

        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return savedOrder;
    }
}
