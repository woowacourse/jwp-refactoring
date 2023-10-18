package kitchenpos.application;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderCreationRequest;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.dto.request.OrderStatusUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            MenuRepository menuRepository,
            OrderRepository orderRepository,
            OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(OrderCreationRequest request) {
        List<OrderLineItemRequest> orderLineItemRequests = request.getOrderLineItemRequests();

        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException("주문 상품이 비어있는 상태로 주문을 생성할 수 없습니다.");
        }

        OrderTable orderTable = findOrderTableById(request.getOrderTableId());
        Order order = Order.createWithEmptyOrderLinItems(orderTable);

        initializeOrderLineItems(request, order);
        orderRepository.save(order);

        return OrderResponse.from(order);
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NoSuchElementException("ID에 해당하는 주문 테이블이 존재하지 않습니다."));
    }

    private void initializeOrderLineItems(OrderCreationRequest request, Order order) {
        Map<Long, Long> quantitiesByMenuId = request.getQuantitiesByMenuId();
        List<OrderLineItem> orderLineItems = menuRepository.findAllById(quantitiesByMenuId.keySet())
                .stream()
                .map(menu -> OrderLineItem.create(order, menu, quantitiesByMenuId.get(menu.getId())))
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(quantitiesByMenuId.keySet())) {
            throw new IllegalArgumentException("주문 상품에 존재하지 않는 메뉴가 존재합니다.");
        }

        order.initializeOrderLineItems(orderLineItems);
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, OrderStatusUpdateRequest request) {
        Order order = findOrderById(orderId);

        String orderStatus = request.getOrderStatus();
        order.changeOrderStatus(OrderStatus.valueOf(orderStatus));

        return OrderResponse.from(order);
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("ID에 해당하는 주문이 존재하지 않습니다."));
    }

}
