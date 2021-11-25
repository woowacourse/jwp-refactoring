package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.order.OrderChangeRequestDto;
import kitchenpos.application.dto.request.order.OrderCreateRequestDto;
import kitchenpos.application.dto.request.order.OrderLineItemRequestDto;
import kitchenpos.application.dto.response.order.OrderResponseDto;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public OrderResponseDto create(OrderCreateRequestDto orderCreateRequestDto) {
        OrderTable orderTable = orderTableRepository
            .findById(orderCreateRequestDto.getOrderTableId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 OrderTable ID입니다."));
        Order order = new Order(orderTable, OrderStatus.COOKING);

        List<OrderLineItem> orderLineItems = orderCreateRequestDto.getOrderLineItemRequestDtos()
            .stream()
            .map(orderLineItemRequestDto -> convert(orderLineItemRequestDto, order))
            .collect(Collectors.toList());
        order.updateOrderLineItems(orderLineItems);

        return OrderResponseDto.from(orderRepository.save(order));
    }

    private OrderLineItem convert(OrderLineItemRequestDto orderLineItemRequestDto, Order order) {
        Menu menu = menuRepository.findById(orderLineItemRequestDto.getMenuId())
            .orElseThrow(() -> new IllegalArgumentException("주문 항목이 속한 Menu가 DB에 존재하지 않습니다."));
        return new OrderLineItem(order, menu, orderLineItemRequestDto.getQuantity());
    }

    public List<OrderResponseDto> list() {
        return orderRepository.findAll()
            .stream()
            .map(OrderResponseDto::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponseDto changeOrderStatus(OrderChangeRequestDto orderChangeRequestDto) {
        Order order = orderRepository.findById(orderChangeRequestDto.getOrderId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Order입니다."));
        OrderStatus orderStatus = OrderStatus.valueOf(orderChangeRequestDto.getOrderStatus());
        order.changeOrderStatus(orderStatus);
        return OrderResponseDto.from(order);
    }
}
