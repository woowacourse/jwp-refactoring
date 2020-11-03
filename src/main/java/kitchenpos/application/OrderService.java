package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.dto.MenuQuantityRequest;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderLineItemResponse;
import kitchenpos.dto.OrderStatusRequest;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderTableRepository orderTableRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest orderCreateRequest) {
        final List<MenuQuantityRequest> menuQuantities = orderCreateRequest.getMenuQuantities();

        final List<Long> menuIds = menuQuantities.stream()
                .map(MenuQuantityRequest::getMenuId)
                .collect(Collectors.toList());

        if (menuQuantities.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableRepository.findById(orderCreateRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        final Order savedOrder = orderRepository.save(orderCreateRequest.toEntity(orderTable));

        final List<OrderLineItemResponse> orderLineItemResponses = new ArrayList<>();

        for (final MenuQuantityRequest menuQuantity : menuQuantities) {
            final Menu menu = menuRepository.findById(menuQuantity.getMenuId())
                    .orElseThrow(IllegalArgumentException::new);
            OrderLineItem orderLineItem = orderLineItemRepository.save(new OrderLineItem(savedOrder, menu, menuQuantity.getQuantity()));
            orderLineItemResponses.add(OrderLineItemResponse.of(orderLineItem));
        }

        return OrderResponse.of(savedOrder, orderLineItemResponses);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        final List<OrderResponse> orderResponses = new ArrayList<>();

        for (final Order order : orders) {
            List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(order.getId());
            orderResponses.add(
                    OrderResponse.of(order, OrderLineItemResponse.ofList(orderLineItems))
            );
        }

        return orderResponses;
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(orderStatusRequest.getOrderStatus());

        savedOrder.updateStatus(orderStatus.name());

        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(orderId);

        return OrderResponse.of(savedOrder, OrderLineItemResponse.ofList(orderLineItems));
    }
}
