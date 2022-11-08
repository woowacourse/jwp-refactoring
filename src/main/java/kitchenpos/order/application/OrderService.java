package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.order.dao.OrderRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemDto;
import kitchenpos.order.dto.request.OrderCreateRequest;
import kitchenpos.order.dto.request.OrderStatusChangeRequest;
import kitchenpos.order.dto.response.OrderResponse;
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

    public OrderResponse create(final OrderCreateRequest request) {
        final List<OrderLineItemDto> orderLineItems = request.getOrderLineItems();
        validateOrderLineItems(orderLineItems);

        final Long orderTableId = request.getOrderTableId();
        validateOrderTable(orderTableId);

        final Order savedOrder = orderRepository.save(
                new Order(
                        orderTableId,
                        OrderStatus.COOKING,
                        LocalDateTime.now(),
                        mapToOrderLineItems(orderLineItems))
        );
        return OrderResponse.from(savedOrder);
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
    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order savedOrder = orderRepository.findById(orderId);

        savedOrder.changeOrderStatus(request.getOrderStatus());

        return OrderResponse.from(orderRepository.update(savedOrder));
    }
}
