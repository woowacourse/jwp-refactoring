package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.order.OrderChangeRequest;
import kitchenpos.dto.order.OrderCreateRequest;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("해당 주문 테이블이 존재하지 않습니다."));
        orderTable.validateEmptyTable();

        final Order newOrder = new Order(orderLineItems, orderTable, OrderStatus.COOKING);
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
                        .map(orderLineItemRequest ->
                                new OrderLineItem(null, savedMenu, orderLineItemRequest.getQuantity())))
                .collect(Collectors.toList());
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderChangeRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));

        final OrderStatus orderStatus = request.getOrderStatus();
        savedOrder.changeOrderStatus(orderStatus);

        return orderRepository.save(savedOrder);
    }
}
