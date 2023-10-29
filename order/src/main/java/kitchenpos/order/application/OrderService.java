package kitchenpos.order.application;

import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuRepository;
import kitchenpos.order.Order;
import kitchenpos.order.OrderLineItem;
import kitchenpos.order.OrderRepository;
import kitchenpos.order.OrderValidator;
import kitchenpos.order.ui.OrderLineItemDto;
import kitchenpos.order.ui.OrderRequest;
import kitchenpos.order.ui.OrderResponse;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;
    private final OrderValidator orderValidator;

    public OrderService(final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository,
                        final MenuRepository menuRepository,
                        final OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다. 주문을 등록할 수 없습니다."));

        final Order order = new Order(orderTable.getId(), orderRequest.getOrderStatus(), orderRequest.getOrderedTime());

        orderRepository.save(order);
        final List<OrderLineItem> orderLineItems = getOrderLineItems(orderRequest);
        order.addOrderLineItems(orderLineItems);
        order.place(orderValidator);

        return OrderResponse.from(order);
    }

    private List<OrderLineItem> getOrderLineItems(OrderRequest orderRequest) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemDto dto : orderRequest.getOrderLineItemDtos()) {
            Order order = orderRepository.findById(dto.getOrderId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
            Menu menu = menuRepository.findById(dto.getMenuId())
                    .orElseThrow(() -> new IllegalArgumentException("주문항목에 존재하지 않는 메뉴가 있습니다. 주문을 등록할 수 없습니다."));
            orderLineItems.add(new OrderLineItem(order, menu.getName(), menu.getPrice(), dto.getQuantity()));
        }

        return orderLineItems;
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest request) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다. 주문상태를 변경할 수 없습니다."));
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문테이블이 존재하지 않습니다. 주문상태를 변경할 수 없습니다."));

        final Order changeOrder = new Order(orderTable.getId(), request.getOrderStatus(), request.getOrderedTime());
        order.updateStatus(changeOrder);

        return OrderResponse.from(order);
    }
}
