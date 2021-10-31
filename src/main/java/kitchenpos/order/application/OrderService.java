package kitchenpos.order.application;

import java.util.List;
import java.util.Objects;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderCreateRequestDto;
import kitchenpos.order.dto.OrderCreateResponseDto;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final ApplicationEventPublisher publisher;

    public OrderService(
        final MenuDao menuDao,
        final OrderDao orderDao,
        final ApplicationEventPublisher publisher
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.publisher = publisher;
    }

    @Transactional
    public OrderCreateResponseDto create(final OrderCreateRequestDto orderCreateRequestDto) {
        Order order = orderCreateRequestDto.toEntity();
        List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        order.shouldNotEmpty(orderLineItems);

        final List<Long> menuIds = order.getMenuIds();

        publisher.publishEvent(new OrderStartedEvent(order, orderLineItems, menuIds));

        Order savedOrder = orderDao.save(order);
        return new OrderCreateResponseDto(savedOrder);
    }

    public List<Order> list() {
        final List<Order> orders = orderDao.findAll();
        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderDao.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus.name());

        orderDao.save(savedOrder);
        return savedOrder;
    }
}
