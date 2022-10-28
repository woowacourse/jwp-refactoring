package kitchenpos.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.order.ChangeOrderStatusRequest;
import kitchenpos.dto.order.CreateOrderRequest;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
        final MenuRepository menuRepository,
        final OrderRepository orderRepository,
        final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(final CreateOrderRequest request) {
        final OrderTable orderTable = findOrderTableById(request.getOrderTableId());

        final Map<Menu, Long> orderLineItems = request.getOrderLineItems().stream()
            .collect(Collectors.toMap(
                it -> findMenuById(it.getMenuId()),
                it -> it.getQuantity()
            ));

        Order order = new Order(orderTable, orderLineItems);
        return orderRepository.save(order);
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest request) {
        final Order order = findOrderById(orderId);
        order.changeStatus(request.getStatus());

        return order;
    }

    private OrderTable findOrderTableById(Long id) {
        return orderTableRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블 입니다."));
    }

    private Menu findMenuById(Long id) {
        return menuRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 주문입니다."));
    }
}
