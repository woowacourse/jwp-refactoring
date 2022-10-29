package kitchenpos.application;

import kitchenpos.application.request.order.ChangeOrderStatusRequest;
import kitchenpos.application.request.order.OrderLineItemRequest;
import kitchenpos.application.request.order.OrderRequest;
import kitchenpos.application.response.ResponseAssembler;
import kitchenpos.application.response.order.OrderResponse;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final MenuDao menuDao;
    private final OrderTableDao orderTableDao;
    private final OrderRepository orderRepository;
    private final ResponseAssembler responseAssembler;

    public OrderService(final MenuDao menuDao, final OrderTableDao orderTableDao, final OrderRepository orderRepository,
                        final ResponseAssembler responseAssembler) {
        this.menuDao = menuDao;
        this.orderTableDao = orderTableDao;
        this.orderRepository = orderRepository;
        this.responseAssembler = responseAssembler;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        validateOrderTableNotEmpty(request.getOrderTableId());

        final var orderLineItems = asOrderLineItems(request.getOrderLineItems());
        validateMenuNotDuplicated(orderLineItems);

        final var order = asOrder(request, orderLineItems);
        final var savedOrder = orderRepository.save(order);
        return responseAssembler.orderResponse(savedOrder);
    }

    private void validateOrderTableNotEmpty(final Long orderTableId) {
        final OrderTable orderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블을 찾을 수 없습니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어 있습니다.");
        }
    }

    private void validateMenuNotDuplicated(List<OrderLineItem> orderLineItems) {
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("중복된 메뉴의 주문 항목이 존재합니다.");
        }
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return responseAssembler.orderResponses(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest request) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));

        order.updateOrderStatus(request.getOrderStatus());

        final var updatedOrder = orderRepository.update(order);
        return responseAssembler.orderResponse(updatedOrder);
    }

    private List<OrderLineItem> asOrderLineItems(final List<OrderLineItemRequest> requests) {
        return requests.stream()
                .map(this::asOrderLineItem)
                .collect(Collectors.toUnmodifiableList());
    }

    private OrderLineItem asOrderLineItem(final OrderLineItemRequest request) {
        final var orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(request.getMenuId());
        orderLineItem.setQuantity(request.getQuantity());
        return orderLineItem;
    }

    private Order asOrder(final OrderRequest request, final List<OrderLineItem> orderLineItems) {
        return new Order(
                request.getOrderTableId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                orderLineItems
        );
    }
}
