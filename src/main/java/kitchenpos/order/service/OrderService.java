package kitchenpos.order.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderEditRequest;
import kitchenpos.order.dto.OrderResponses;
import kitchenpos.table.domain.Table;
import kitchenpos.table.domain.TableRepository;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final TableRepository tableRepository;

    public OrderService(
        final MenuRepository menuRepository,
        final OrderRepository orderRepository,
        final OrderLineItemRepository orderLineItemRepository,
        final TableRepository tableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.tableRepository = tableRepository;
    }

    @Transactional
    public Long create(final OrderCreateRequest request) {
        final Table savedTable = findTable(request.getTableId());
        Order order = new Order(savedTable, OrderStatus.COOKING);

        final Order savedOrder = orderRepository.save(order);
        OrderLineItems orderLineItems = new OrderLineItems(order, request.getOrderLineItemDtos());

        List<OrderLineItem> savedOrderLineItems = orderLineItems.stream()
            .map(orderLineItemRepository::save)
            .collect(Collectors.toList());
        
        savedOrder.changeOrderLineItems(savedOrderLineItems);

        return savedOrder.getId();
    }

    public OrderResponses list() {
        List<Order> orders = orderRepository.findAllWithOrderLineItemsAndTable();
        return OrderResponses.from(orders);
    }

    @Transactional
    public void changeOrderStatus(Long orderId, OrderEditRequest request) {
        final Order savedOrder = findOne(orderId);
        savedOrder.changeOrderStatus(request.getOrderStatus());
    }

    private Order findOne(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);
    }

    private Table findTable(Long tableId) {
        return tableRepository.findById(tableId)
            .orElseThrow(IllegalArgumentException::new);
    }
}
