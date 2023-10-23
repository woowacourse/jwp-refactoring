package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.order.OrderCreateRequest;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.order.OrderUpdateStatusRequest;
import kitchenpos.mapper.OrderMapper;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Transactional
@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    /**
     * 음.............id 로 변환하고 하나씩 찾는 것 보다 효율적인 방법은 없나..........
     * in으로 한번에 찾으면 없는걸 검증하기 어려븜.. 따로 처리 해줘야함...
     */
    // TODO: 2023/10/23
    public OrderResponse create(
            final OrderCreateRequest request
    ) {
        final Order order = saveOrder(request);
        saveOrderLineItems(request.getOrderLineItems(), order);

        return OrderMapper.toOrderResponse(order);
    }

    private List<Long> convertToMenuIds(final List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }

    // TODO: 2023/10/23 로직 분리
    private Order saveOrder(
            final OrderCreateRequest request
    ) {
        final List<OrderLineItemRequest> orderLineItemRequests = request.getOrderLineItems();
        validateEmptyByOrderLineItems(orderLineItemRequests);
        final List<Long> menuIds = convertToMenuIds(orderLineItemRequests);
        validateOrderLineItemSize(orderLineItemRequests, menuIds);
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 order table 입니다."));
        validateEmptyByOrderTable(orderTable);
        return orderRepository.save(OrderMapper.toOrder(orderTable, OrderStatus.COOKING, LocalDateTime.now()));
    }

    private void validateEmptyByOrderTable(
            final OrderTable orderTable
    ) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("order table은 비어있을 수 없습니다.");
        }
    }

    // TODO: 2023/10/23 로직 분리
    private void saveOrderLineItems(
            final List<OrderLineItemRequest> orderLineItems,
            final Order savedOrder
    ) {
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItemRequest orderLineItem : orderLineItems) {
            Menu menu = menuRepository.findById(orderLineItem.getMenuId())
                    .orElseThrow(() -> new NoSuchElementException("존재하지 않는 menu입니다."));
            savedOrderLineItems.add(orderLineItemRepository.save(OrderLineItem.of(savedOrder, menu, orderLineItem.getQuantity())));
        }
        savedOrder.updateOrderLineItems(savedOrderLineItems);
    }

    private void validateOrderLineItemSize(
            final List<OrderLineItemRequest> orderLineItems,
            final List<Long> menuIds
    ) {
        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("메뉴의 메뉴 그룹은 기존에 존재하는 메뉴 그룹이어야 합니다.");
        }
    }

    private void validateEmptyByOrderLineItems(
            final List<OrderLineItemRequest> orderLineItems
    ) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("order line item 은 1개 이상이어야 합니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> readAll() {
        final List<Order> orders = orderRepository.findAll();

        return OrderMapper.toOrderResponses(orders);
    }

    public OrderResponse changeOrderStatus(
            final Long orderId,
            final OrderUpdateStatusRequest request
    ) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 주문입니다."));
        order.updateOrderStatus(request.getOrderStatus());

        return OrderMapper.toOrderResponse(order);
    }
}
