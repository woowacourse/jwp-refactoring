package kitchenpos.application.order;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.table.OrderStatusRecord;
import kitchenpos.domain.table.OrderStatusRecordRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.order.mapper.OrderDtoMapper;
import kitchenpos.dto.order.mapper.OrderLineItemMapper;
import kitchenpos.dto.order.mapper.OrderMapper;
import kitchenpos.dto.order.request.OrderCreateRequest;
import kitchenpos.dto.order.request.OrderLineItemCreateRequest;
import kitchenpos.dto.order.request.OrderStatusChangeRequest;
import kitchenpos.dto.order.response.OrderResponse;
import kitchenpos.exception.badrequest.DuplicateOrderLineItemException;
import kitchenpos.exception.badrequest.MenuNotExistsException;
import kitchenpos.exception.badrequest.OrderNotExistsException;
import kitchenpos.exception.badrequest.OrderTableNotExistsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderLineItemMapper orderLineItemMapper;
    private final OrderDtoMapper orderDtoMapper;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderStatusRecordRepository orderStatusRecordRepository;

    public OrderService(final OrderMapper orderMapper, final OrderLineItemMapper orderLineItemMapper,
                        final OrderDtoMapper orderDtoMapper, final MenuRepository menuRepository,
                        final OrderRepository orderRepository, final OrderTableRepository orderTableRepository,
                        final OrderStatusRecordRepository orderStatusRecordRepository) {
        this.orderMapper = orderMapper;
        this.orderLineItemMapper = orderLineItemMapper;
        this.orderDtoMapper = orderDtoMapper;
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderStatusRecordRepository = orderStatusRecordRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest orderCreateRequest) {
        List<OrderLineItem> orderLineItems = createOrderLineItems(orderCreateRequest);
        Order order = createAndSaveOrder(orderCreateRequest, orderLineItems);
        return orderDtoMapper.toOrderResponse(order);
    }

    private List<OrderLineItem> createOrderLineItems(final OrderCreateRequest orderCreateRequest) {
        List<Menu> menus = findMenus(orderCreateRequest.getOrderLineItems());
        return orderLineItemMapper.toOrderLineItems(orderCreateRequest.getOrderLineItems(), menus);
    }

    private List<Menu> findMenus(final List<OrderLineItemCreateRequest> orderLineItemCreateRequests) {
        List<Long> menuIds = toMenuIds(orderLineItemCreateRequests);
        validateNotDuplicate(menuIds);
        validateMenuExists(menuIds);
        return menuRepository.findAllById(menuIds);
    }

    private List<Long> toMenuIds(final List<OrderLineItemCreateRequest> orderLineItemCreateRequests) {
        return orderLineItemCreateRequests.stream()
                .map(OrderLineItemCreateRequest::getMenuId)
                .collect(Collectors.toList());
    }

    private void validateNotDuplicate(final List<Long> menuIds) {
        if (new HashSet<>(menuIds).size() != menuIds.size()) {
            throw new DuplicateOrderLineItemException();
        }
    }

    private void validateMenuExists(final List<Long> menuIds) {
        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new MenuNotExistsException();
        }
    }

    private Order createAndSaveOrder(final OrderCreateRequest orderCreateRequest,
                                     final List<OrderLineItem> orderLineItems) {
        OrderTable orderTable = orderTableRepository.findById(orderCreateRequest.getOrderTableId())
                .orElseThrow(OrderTableNotExistsException::new);
        Order order = orderRepository.save(
                orderMapper.toOrder(orderCreateRequest, orderLineItems, orderTable.isEmpty()));
        saveOrderStatusRecord(orderTable, order);
        return order;
    }

    private void saveOrderStatusRecord(final OrderTable orderTable, final Order order) {
        orderStatusRecordRepository.save(new OrderStatusRecord(order.getId(), orderTable, order.getOrderStatus()));
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return orderDtoMapper.toOrderResponses(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId,
                                           final OrderStatusChangeRequest orderStatusChangeRequest) {
        Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(OrderNotExistsException::new);
        savedOrder.changeOrderStatus(orderStatusChangeRequest.getOrderStatus());
        changeOrderStatusRecord(orderId, orderStatusChangeRequest);
        return orderDtoMapper.toOrderResponse(savedOrder);
    }

    private void changeOrderStatusRecord(final Long orderId, final OrderStatusChangeRequest orderStatusChangeRequest) {
        OrderStatusRecord orderStatusRecord = orderStatusRecordRepository.findById(orderId)
                .orElseThrow(OrderNotExistsException::new);
        orderStatusRecord.changeOrderStatus(orderStatusChangeRequest.getOrderStatus());
    }
}
