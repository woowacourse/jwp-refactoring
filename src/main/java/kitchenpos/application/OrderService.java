package kitchenpos.application;

import static kitchenpos.application.exception.ExceptionType.NOT_FOUND_MENU_EXCEPTION;
import static kitchenpos.application.exception.ExceptionType.NOT_FOUND_ORDER_EXCEPTION;
import static kitchenpos.application.exception.ExceptionType.NOT_FOUND_TABLE_EXCEPTION;
import static kitchenpos.domain.OrderStatus.COOKING;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.exception.CustomIllegalArgumentException;
import kitchenpos.menu.domain.JpaMenuRepository;
import kitchenpos.dao.JpaOrderRepository;
import kitchenpos.dao.JpaOrderTableRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.ui.dto.request.OrderRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {
    private final JpaMenuRepository menuRepository;
    private final JpaOrderRepository orderRepository;
    private final JpaOrderTableRepository orderTableRepository;

    public OrderService(final JpaMenuRepository menuRepository, final JpaOrderRepository orderRepository,
                        final JpaOrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public Order create(final OrderRequest request) {
        final List<OrderLineItem> orderLineItems = request.toOrderLineItem();
        validMenu(orderLineItems);
        validOrderTable(request.getOrderTableId());

        return orderRepository.save(convertSaveConditionOrder(request.getOrderTableId(), orderLineItems));
    }

    private Order convertSaveConditionOrder(final Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, COOKING.name(), LocalDateTime.now(),
                orderLineItems);
    }

    private void validOrderTable(final Long orderTableId) {
        if (!orderTableRepository.existsById(orderTableId)) {
            new CustomIllegalArgumentException(NOT_FOUND_TABLE_EXCEPTION);
        }
    }

    private void validMenu(final List<OrderLineItem> orderLineItems) {
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        final List<Menu> menus = menuRepository.findAllById(menuIds);

        if (orderLineItems.size() != menus.size()) {
            throw new CustomIllegalArgumentException(NOT_FOUND_MENU_EXCEPTION);
        }
    }

    @Transactional(readOnly = true)
    public List<Order> list() {
        return orderRepository.findAll();
    }

    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomIllegalArgumentException(NOT_FOUND_ORDER_EXCEPTION));
        savedOrder.changeOrderStatus(order.getOrderStatus());
        return orderRepository.save(savedOrder);
    }
}
