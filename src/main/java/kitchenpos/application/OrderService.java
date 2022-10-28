package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.dto.request.ChangeOrderStatusRequest;
import kitchenpos.ui.dto.request.OrderCreateRequest;
import kitchenpos.ui.dto.request.OrderLineItemRequest;
import kitchenpos.ui.dto.response.OrderResponse;
import kitchenpos.ui.dto.response.OrderResponse.OrderLineItemResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository,
                        final OrderDao orderDao,
                        final OrderLineItemDao orderLineItemDao,
                        final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        final List<OrderLineItemRequest> orderLineItems = request.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        final Order order = new Order(request.getOrderTableId(), OrderStatus.COOKING.name(), LocalDateTime.now());
        final Order savedOrder = orderDao.save(order);
        final Long orderId = savedOrder.getId();

        final List<OrderLineItemResponse> orderLineItemResponses = new ArrayList<>();
        for (final OrderLineItemRequest orderLineItemRequest : orderLineItems) {
            final OrderLineItem orderLineItem = new OrderLineItem(orderId, orderLineItemRequest.getMenuId(),
                    orderLineItemRequest.getQuantity());
            final OrderLineItem savedOrderLineItem = orderLineItemDao.save(orderLineItem);
            final OrderLineItemResponse response = new OrderLineItemResponse(savedOrderLineItem.getSeq(),
                    savedOrderLineItem.getOrderId(), savedOrderLineItem.getMenuId(), savedOrderLineItem.getQuantity());
            orderLineItemResponses.add(response);
        }

        return new OrderResponse(savedOrder.getId(), savedOrder.getOrderTableId(),
                savedOrder.getOrderStatus(),
                savedOrder.getOrderedTime(), orderLineItemResponses);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderDao.findAll();

        final List<OrderResponse> response = new ArrayList<>();
        for (final Order order : orders) {
            final List<OrderLineItemResponse> orderLineItemResponses = orderLineItemDao.findAllByOrderId(order.getId())
                    .stream()
                    .map(it -> new OrderLineItemResponse(it.getSeq(), it.getOrderId(), it.getMenuId(),
                            it.getQuantity()))
                    .collect(Collectors.toList());
            final OrderResponse orderResponse = new OrderResponse(order.getId(), order.getOrderTableId(),
                    order.getOrderStatus(), order.getOrderedTime(),
                    orderLineItemResponses);
            response.add(orderResponse);
        }

        return response;
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest request) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus.name());

        orderDao.save(savedOrder);

        final List<OrderLineItemResponse> orderLineItemResponses = orderLineItemDao.findAllByOrderId(orderId)
                .stream()
                .map(it -> new OrderLineItemResponse(it.getSeq(), it.getOrderId(), it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());

        return new OrderResponse(savedOrder.getId(), savedOrder.getOrderTableId(), savedOrder.getOrderStatus(),
                savedOrder.getOrderedTime(), orderLineItemResponses);
    }
}
