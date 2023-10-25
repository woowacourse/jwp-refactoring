package kitchenpos.order;

import kitchenpos.menu.MenuRepository;
import kitchenpos.order.service.OrderCreateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderCreateService orderCreateService;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            OrderCreateService orderCreateService) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderCreateService = orderCreateService;
    }

    @Transactional
    public Order create(final Long orderTableId, final Order order) {
        final List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("한번의 주문에서 중복 메뉴를 주문할 수 없습니다.");
        }

        return orderCreateService.create(orderTableId, order);
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));

        savedOrder.changeOrderStatus(orderStatus);

        return savedOrder;
    }
}
