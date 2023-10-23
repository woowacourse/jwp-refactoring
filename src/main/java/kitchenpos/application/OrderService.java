package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderLineItemDto;
import kitchenpos.application.request.OrderCreateRequest;
import kitchenpos.application.response.OrderResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    public OrderResponse create(OrderCreateRequest orderCreateRequest) {
        List<OrderLineItemDto> orderLineItemDtos = orderCreateRequest.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItemDtos)) {
            throw new IllegalArgumentException("주문의 메뉴가 존재하지 않습니다.");
        }

        OrderTable orderTable = orderTableRepository.findById(orderCreateRequest.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("EMPTY 상태인 테이블에 주문할 수 없습니다.");
        }

        Order order = Order.builder()
                .orderTable(orderTable)
                .orderStatus(OrderStatus.COOKING)
                .build();

        List<OrderLineItem> orderLineItems = orderLineItemDtos.stream()
                .map(orderLineItemDto -> getOrderLineItem(orderLineItemDto, order))
                .collect(Collectors.toList());

        order.addOrderLineItems(orderLineItems);
        Order savedOrder = orderRepository.save(order);

        return OrderResponse.from(savedOrder);
    }

    private OrderLineItem getOrderLineItem(OrderLineItemDto orderLineItemDto, Order order) {
        Menu menu = findMenuById(orderLineItemDto.getMenuId());
        return OrderLineItem.builder()
                .order(order)
                .menu(menu)
                .quantity(orderLineItemDto.getQuantity())
                .build();
    }

    private Menu findMenuById(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 메뉴를 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, String orderStatusRequest) {
        Order foundOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 주문을 찾을 수 없습니다."));

        if (foundOrder.isCompleted()) {
            throw new IllegalArgumentException("완료된 주문의 상태를 변경할 수 없습니다.");
        }

        OrderStatus orderStatus = OrderStatus.from(orderStatusRequest);
        foundOrder.updateStatus(orderStatus);
        return OrderResponse.from(foundOrder);
    }
}
