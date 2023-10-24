package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.dto.ChangeOrderStatusRequest;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderTableRepository orderTableRepository,
            final OrderRepository orderRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order create(final OrderRequest request) {
        validateMenu(request);
        final OrderTable table = getTable(request);
        final Order order = request.toOrder(table);
        return orderRepository.save(order);
    }

    private OrderTable getTable(final OrderRequest request) {
        final Long orderTableId = request.getOrderTableId();
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다."));
    }

    private void validateMenu(final OrderRequest request) {
        final List<Long> menuIds = request.getOrderLineItemRequests().stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        final List<Menu> menus = menuRepository.findAllById(menuIds);
        if (menus.size() != menuIds.size()) {
            throw new IllegalArgumentException("없는 메뉴에요");
        }
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("없는 주문이에요."));

        final OrderStatus orderStatus = request.getOrderStatus();
        savedOrder.changeStatus(orderStatus);
        return savedOrder;
    }
}
