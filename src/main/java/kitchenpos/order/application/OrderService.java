package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderChangeRequest;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
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
    public Order create(final OrderCreateRequest request) {
        final List<OrderLineItemRequest> orderLineItemRequests = request.getOrderLineItems();

        final List<Long> menuIds = getMenuIds(orderLineItemRequests);

        if (orderLineItemRequests.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("요청한 주문 항목의 개수와 저장된 메뉴의 개수가 댜릅니다.");
        }

        final List<Menu> savedMenus = menuRepository.findAllByIdIn(menuIds);
        final List<OrderLineItem> orderLineItems = getOrderLineItems(savedMenus, orderLineItemRequests);

        final OrderTable orderTable = findOrderTableBy(request.getOrderTableId());
        orderTable.validateEmptyTable();

        final Order newOrder = new Order(orderLineItems, orderTable.getId(), OrderStatus.COOKING);
        return orderRepository.save(newOrder);
    }

    private List<Long> getMenuIds(final List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }

    private List<OrderLineItem> getOrderLineItems(
            final List<Menu> savedMenus,
            final List<OrderLineItemRequest> orderLineItemRequests
    ) {
        return savedMenus.stream()
                .flatMap(savedMenu -> orderLineItemRequests.stream()
                        .map(orderLineItemRequest -> new OrderLineItem(
                                null,
                                savedMenu.getName(),
                                savedMenu.getPrice(),
                                orderLineItemRequest.getQuantity())))
                .collect(Collectors.toList());
    }

    private OrderTable findOrderTableBy(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문 테이블이 존재하지 않습니다."));
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderChangeRequest request) {
        final Order savedOrder = findOrderBy(orderId);

        final OrderStatus orderStatus = request.getOrderStatus();
        savedOrder.changeOrderStatus(orderStatus);

        return orderRepository.save(savedOrder);
    }

    private Order findOrderBy(final Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));
    }
}
