package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.request.order.OrderCreateRequest;
import kitchenpos.ui.request.order.OrderLineItemDto;
import kitchenpos.ui.request.order.OrderStatusChangeRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuDao menuDao;
    private final OrderRepository orderRepository;
    private final OrderTableDao orderTableDao;

    public OrderService(
            final MenuDao menuDao,
            final OrderRepository orderRepository,
            final OrderTableDao orderTableDao
    ) {
        this.menuDao = menuDao;
        this.orderRepository = orderRepository;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public Order create(final OrderCreateRequest request) {
        final List<OrderLineItemDto> orderLineItems = request.getOrderLineItems();
        validateOrderLineItems(orderLineItems);

        final Long orderTableId = request.getOrderTableId();
        validateOrderTable(orderTableId);

        return orderRepository.save(
                new Order(
                        orderTableId,
                        OrderStatus.COOKING.name(),
                        LocalDateTime.now(),
                        mapToOrderLineItems(orderLineItems))
        );
    }

    private void validateOrderLineItems(final List<OrderLineItemDto> orderLineItemDtos) {
        final List<Long> menuIds = orderLineItemDtos.stream()
                .map(OrderLineItemDto::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItemDtos.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTable(final Long orderTableId) {
        final OrderTable orderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private List<OrderLineItem> mapToOrderLineItems(final List<OrderLineItemDto> requests) {
        return requests.stream()
                .map(OrderLineItemDto::toEntity)
                .collect(Collectors.toList());
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));

        return orderRepository.update(savedOrder);
    }
}
