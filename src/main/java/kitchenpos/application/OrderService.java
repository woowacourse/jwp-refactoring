package kitchenpos.application;

import static kitchenpos.application.exception.ExceptionType.NOT_FOUND_MENU_EXCEPTION;
import static kitchenpos.application.exception.ExceptionType.NOT_FOUND_ORDER_EXCEPTION;
import static kitchenpos.application.exception.ExceptionType.NOT_FOUND_TABLE_EXCEPTION;
import static kitchenpos.domain.OrderStatus.COOKING;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.exception.CustomIllegalArgumentException;
import kitchenpos.dao.JpaOrderRepository;
import kitchenpos.dao.JpaOrderTableRepository;
import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.request.OrderRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {
    private final MenuDao menuDao;
    private final JpaOrderRepository orderRepository;
    private final JpaOrderTableRepository orderTableRepository;

    public OrderService(final MenuDao menuDao, final JpaOrderRepository orderRepository,
                        final JpaOrderTableRepository orderTableRepository) {
        this.menuDao = menuDao;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public Order create(final OrderRequest request) {
        final Order order = request.toOrder();
        final List<OrderLineItem> orderLineItems = order.getOrderLineItems();
        validMenu(orderLineItems);
        final Order saveConditionOrder = convertSaveConditionOrder(request.getOrderTableId(), order);
        return orderRepository.save(saveConditionOrder);
    }

    private Order convertSaveConditionOrder(final Long orderTableId, final Order order) {
        final OrderTable orderTable = getOrderTable(orderTableId);
        return new Order(orderTable.getId(), COOKING.name(), LocalDateTime.now(),
                order.getOrderLineItems());
    }

    private OrderTable getOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new CustomIllegalArgumentException(NOT_FOUND_TABLE_EXCEPTION));
    }

    private void validMenu(final List<OrderLineItem> orderLineItems) {
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
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
