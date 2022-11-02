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
import kitchenpos.exception.badrequest.OrderTableEmptyException;
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
        List<Menu> menus = findMenus(orderCreateRequest.getOrderLineItems());
        OrderTable orderTable = orderTableRepository.findById(orderCreateRequest.getOrderTableId())
                .orElseThrow(OrderTableNotExistsException::new);
        validateOrderTableNotEmpty(orderTable);
        List<OrderLineItem> orderLineItems = orderLineItemMapper
                .toOrderLineItems(orderCreateRequest.getOrderLineItems(), menus);
        Order order = orderRepository.save(orderMapper.toOrder(orderCreateRequest, orderLineItems));
        orderStatusRecordRepository.save(new OrderStatusRecord(order.getId(), orderTable, order.getOrderStatus()));
        return orderDtoMapper.toOrderResponse(order);
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

    private void validateOrderTableNotEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new OrderTableEmptyException();
        }
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
        OrderStatusRecord orderStatusRecord = orderStatusRecordRepository.findById(orderId)
                .orElseThrow(OrderNotExistsException::new);
        orderStatusRecord.changeOrderStatus(orderStatusChangeRequest.getOrderStatus());
        return orderDtoMapper.toOrderResponse(savedOrder);
    }
}
