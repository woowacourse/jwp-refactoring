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

import java.util.ArrayList;
import java.util.List;

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
        if (createOrderLineItemRequests.isEmpty()) {
            throw new IllegalArgumentException("주문 항목이 비어있습니다.");
        }

        final List<Long> menuIds = request.getMenuIds();
        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("주문 항목의 메뉴는 중복될 수 없습니다.");
        }

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (final CreateOrderLineItemRequest createOrderLineItemRequest : createOrderLineItemRequests) {
            final Menu findMenu = menuRepository.findById(createOrderLineItemRequest.getMenuId())
                                                .orElseThrow(() -> new IllegalArgumentException("메뉴가 존재하지 않습니다."));
            final OrderLineItem orderLineItem = new OrderLineItem(findMenu, createOrderLineItemRequest.getQuantity());
            orderLineItems.add(orderLineItem);
        }

        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                                                          .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));

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

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus);

        return savedOrder;
    }
}
