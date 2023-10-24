package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.ui.dto.ChangeOrderStatusRequest;
import kitchenpos.ui.dto.CreateOrderLineItemRequest;
import kitchenpos.ui.dto.CreateOrderRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(final CreateOrderRequest request) {
        final List<CreateOrderLineItemRequest> createOrderLineItemRequests = request.getOrderLineItems();
        if (CollectionUtils.isEmpty(createOrderLineItemRequests)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = request.getMenuIds();

        if (createOrderLineItemRequests.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (final CreateOrderLineItemRequest createOrderLineItemRequest : createOrderLineItemRequests) {
            final Menu findMenu = menuRepository.findById(createOrderLineItemRequest.getMenuId())
                                                .orElseThrow(() -> new IllegalArgumentException());
            orderLineItems.add(createOrderLineItemRequest.toEntity(findMenu));
        }

        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                                                          .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        final Order order = new Order(orderTable, OrderStatus.COOKING);

        final Order savedOrder = orderRepository.save(order);

        savedOrder.addOrderLineItems(orderLineItems);

        return savedOrder;
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                                                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION, savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus);

        return savedOrder;
    }
}
