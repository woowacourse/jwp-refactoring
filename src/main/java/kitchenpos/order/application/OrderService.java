package kitchenpos.order.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.Menus;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.repository.OrderLineItemRepository;
import kitchenpos.order.domain.repository.OrderMenuRepository;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.domain.repository.OrderTableRepository;
import kitchenpos.order.dto.request.OrderLineItemRequest;
import kitchenpos.order.dto.request.OrderRequest;
import kitchenpos.order.dto.response.OrderLineItemResponse;
import kitchenpos.order.dto.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderMenuRepository orderMenuRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository,
            final OrderMenuRepository orderMenuRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderMenuRepository = orderMenuRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final List<OrderLineItemRequest> orderLineItemRequests = orderRequest.getOrderLineItemRequests();

        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException();
        }

        Menus menus = new Menus(menuRepository.findAllById(getMenuIds(orderLineItemRequests)));
        menus.validateDistinctSize(orderLineItemRequests.size());

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        orderTable.validateEmpty();

        Order order = new Order(orderTable, OrderStatus.COOKING.name());
        final Order savedOrder = orderRepository.save(order);

        List<OrderLineItem> savedOrderLineItems = saveOrderLineItems(savedOrder, orderRequest);
        List<OrderLineItemResponse> orderLineItemResponses = getOrderLineItemResponse(savedOrderLineItems);

        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getOrderTable().getId(),
                savedOrder.getOrderStatus(),
                savedOrder.getOrderedTime(),
                orderLineItemResponses
        );
    }

    private List<Long> getMenuIds(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }

    private List<OrderLineItemResponse> getOrderLineItemResponse(List<OrderLineItem> savedOrderLineItems) {
        List<OrderLineItemResponse> orderLineItemResponses = new ArrayList<>();
        for (OrderLineItem orderLineItem : savedOrderLineItems) {
            OrderLineItemResponse orderLineItemResponse = new OrderLineItemResponse(
                    orderLineItem.getSeq(),
                    orderLineItem.getOrder().getId(),
                    orderLineItem.getOrderMenuId(),
                    orderLineItem.getQuantity()
            );
            orderLineItemResponses.add(orderLineItemResponse);
        }
        return orderLineItemResponses;
    }

    private List<OrderLineItem> saveOrderLineItems(Order savedOrder, OrderRequest orderRequest) {
        List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItemRequest orderLineItemRequest : orderRequest.getOrderLineItemRequests()) {
            Menu savedMenu = menuRepository.findById(orderLineItemRequest.getMenuId())
                    .orElseThrow(IllegalArgumentException::new);
            OrderMenu savedOrderMenu = orderMenuRepository.save(
                    new OrderMenu(savedMenu.getName(), savedMenu.getPrice())
            );
            OrderLineItem orderLineItem = new OrderLineItem(
                    savedOrder,
                    savedOrderMenu.getId(),
                    orderLineItemRequest.getQuantity()
            );
            savedOrderLineItems.add(orderLineItemRepository.save(orderLineItem));
        }
        return savedOrderLineItems;
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        List<OrderResponse> orderResponses = new ArrayList<>();
        for (final Order order : orders) {
            List<OrderLineItemResponse> orderLineItemResponses = getOrderLineItemResponse(
                    orderLineItemRepository.findAllByOrderId(order.getId()));
            OrderResponse orderResponse = new OrderResponse(
                    order.getId(),
                    order.getOrderTable().getId(),
                    order.getOrderStatus(),
                    order.getOrderedTime(),
                    orderLineItemResponses
            );
            orderResponses.add(orderResponse);
        }

        return orderResponses;
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrder.validateChangeStatus();

        savedOrder.changeOrderStatus(orderRequest.getOrderStatus());

        List<OrderLineItemResponse> orderLineItemResponses = getOrderLineItemResponse(
                orderLineItemRepository.findAllByOrderId(orderId));

        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getOrderTable().getId(),
                savedOrder.getOrderStatus(),
                savedOrder.getOrderedTime(),
                orderLineItemResponses
        );
    }
}
