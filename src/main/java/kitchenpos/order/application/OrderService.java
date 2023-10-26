package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.exception.MenuException;
import kitchenpos.order.application.dto.CreateOrderDto;
import kitchenpos.order.application.dto.CreateOrderLineItemDto;
import kitchenpos.order.application.dto.OrderDto;
import kitchenpos.order.application.dto.UpdateOrderStatusDto;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.OrderException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.exception.OrderTableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            MenuRepository menuRepository,
            OrderRepository orderRepository,
            OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderDto create(CreateOrderDto createOrderDto) {
        Long orderTableId = createOrderDto.getOrderTableId();
        validateOrderTable(orderTableId);
        List<OrderLineItem> orderLineItems = makeOrderLineItems(createOrderDto.getOrderLineItems());
        Order order = new Order(orderTableId, orderLineItems, LocalDateTime.now());
        orderRepository.save(order);

        return OrderDto.from(order);
    }

    private void validateOrderTable(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new OrderTableException("주문 테이블을 찾을 수 없어 주문을 진행할 수 없습니다."));
        if (orderTable.getEmpty()) {
            throw new OrderTableException("주문테이블이 주문을 할 수 없는 상태라 주문을 추가할 수 없습니다.");
        }
    }

    private List<OrderLineItem> makeOrderLineItems(List<CreateOrderLineItemDto> createOrderLineItems) {
        Set<OrderLineItem> orderLineItems = new HashSet<>();
        for (CreateOrderLineItemDto createOrderLineItem : createOrderLineItems) {
            Menu menu = findMenu(createOrderLineItem.getMenuId());
            Long quantity = createOrderLineItem.getQuantity();
            OrderLineItem orderLineItem = new OrderLineItem(quantity, menu.getName(), menu.getPrice());
            orderLineItems.add(orderLineItem);
        }
        validateOrderLineItemIsNotEmpty(orderLineItems);
        validateDuplicatedOrderLineItems(
                createOrderLineItems, orderLineItems);
        return new ArrayList<>(orderLineItems);
    }

    private Menu findMenu(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuException("주문 메뉴를 찾을 수 없어 주문을 진행할 수 없습니다."));
    }

    private void validateOrderLineItemIsNotEmpty(Set<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new OrderException("주문아이템이 한 개도 존재하지 않아 주문을 진행할 수 없습니다.");
        }
    }

    private void validateDuplicatedOrderLineItems(
            List<CreateOrderLineItemDto> createOrderLineItems,
            Set<OrderLineItem> orderLineItems) {
        if (createOrderLineItems.size() != orderLineItems.size()) {
            throw new OrderException("중복된 주문아이템 항목이 있어 주문을 진행할 수 없습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<OrderDto> list() {
        List<Order> orders = orderRepository.findAllWithOrderLineItems();
        return orders.stream()
                .map(OrderDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDto changeOrderStatus(UpdateOrderStatusDto updateOrderStatusDto) {
        Long orderId = updateOrderStatusDto.getOrderId();
        OrderStatus orderStatus = OrderStatus.parseOrderStatus(updateOrderStatusDto.getOrderStatus());
        Order order = findOrder(orderId);
        order.changeOrderStatus(orderStatus);
        return OrderDto.from(order);
    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("존재하지 않은 주문이라 주문상태를 변경할 수 없습니다."));
    }
}
