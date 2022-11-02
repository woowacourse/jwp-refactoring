package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.order.dao.OrderRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderLineItemDto;
import kitchenpos.order.dto.OrderStatusChangeRequest;
import kitchenpos.product.domain.OrderStatus;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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

    public Order create(final OrderCreateRequest request) {
        final List<OrderLineItemDto> orderLineItems = request.getOrderLineItems();
        validateOrderLineItems(orderLineItems);

        final Long orderTableId = request.getOrderTableId();
        validateOrderTable(orderTableId);

        return orderRepository.save(
                new Order(
                        orderTableId,
                        OrderStatus.COOKING,
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

    @Transactional(readOnly = true)
    public List<Order> list() {
        return orderRepository.findAll();
    }

    public Order changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order savedOrder = orderRepository.findById(orderId);

        savedOrder.changeOrderStatus(request.getOrderStatus());

        return orderRepository.update(savedOrder);
    }
}
