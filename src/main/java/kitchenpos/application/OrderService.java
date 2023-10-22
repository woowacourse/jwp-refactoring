package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.vo.Quantity;
import kitchenpos.dto.OrderCreateOrderLineItemRequest;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository,
                        final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        validateOrderLineItemsOrderable(request);

        final OrderTable findOrderTable = orderTableRepository.findOrderTableById(request.getOrderTableId());
        final Order newOrder = Order.ofEmptyOrderLineItems(findOrderTable);
        newOrder.addOrderLineItems(createOrderLineItems(request));

        final Order savedOrder = orderRepository.save(newOrder);

        return OrderResponse.from(savedOrder);
    }

    private void validateOrderLineItemsOrderable(final OrderCreateRequest request) {
        final List<Long> requestMenuIds = request.getOrderLineItems()
                .stream()
                .map(OrderCreateOrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        if (request.getOrderLineItems().size() != menuRepository.countByIdIn(requestMenuIds)) {
            throw new IllegalArgumentException("주문 상품 목록 개수와 실제 메뉴 개수와 같지 않습니다. 주문할 수 있는 상품인지 확인해주세요.");
        }
    }

    private List<OrderLineItem> createOrderLineItems(final OrderCreateRequest request) {
        return request.getOrderLineItems()
                .stream()
                .map(orderLineItemRequest -> {
                    final Menu findMenu = menuRepository.findMenuById(orderLineItemRequest.getMenuId());
                    return OrderLineItem.withoutOrder(findMenu, new Quantity(orderLineItemRequest.getQuantity()));
                }).collect(Collectors.toList());
    }

    public List<OrderResponse> list() {
        return OrderResponse.from(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final String orderStatus) {
        final OrderStatus findOrderStatus = OrderStatus.valueOf(orderStatus);

        final Order findOrder = orderRepository.findOrderById(orderId);
        findOrder.changeOrderStatus(findOrderStatus);

        return OrderResponse.from(findOrder);
    }
}
