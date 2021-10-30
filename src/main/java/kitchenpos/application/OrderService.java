package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.request.OrderLineItemRequest;
import kitchenpos.ui.request.OrderRequest;
import kitchenpos.ui.request.OrderStatusModifyRequest;
import kitchenpos.ui.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        // TODO: 오더라인 아이템 menuId 발리데이션
//        if (orderLineItemRequests.size() != menuRepository.countByOrderLineItemsIn(orderLineItems)) {
//            throw new IllegalArgumentException();
//        }

        final Order order = new Order();
        order.setId(null);

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        // 오더를 받은 테이블이라면 사람이 있어야 말이 되는거겠지?
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        order.setOrderTable(orderTable);
        order.setOrderStatus(OrderStatus.COOKING);
        order.setOrderedTime(LocalDateTime.now());

        final Order savedOrder = orderRepository.save(order);

        final List<OrderLineItemRequest> orderLineItemRequests = orderRequest.getOrderLineItems();
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();

        for (OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            final OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setOrder(savedOrder);
            orderLineItem.setMenu(menuRepository.findById(orderLineItemRequest.getMenuId()).get());
            orderLineItem.setQuantity(orderLineItemRequest.getQuantity());
            savedOrderLineItems.add(orderLineItemRepository.save(orderLineItem));
        }

        savedOrder.setOrderLineItems(savedOrderLineItems);
        return OrderResponse.of(savedOrder);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        // TODO: FetchJoin으로 한번에 물어오기
        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemRepository.findAllByOrder(order));
        }

        return OrderResponse.toList(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusModifyRequest orderStatusModifyRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(OrderStatus.valueOf(orderStatusModifyRequest.getOrderStatus()));

        orderRepository.save(savedOrder);

        savedOrder.setOrderLineItems(orderLineItemRepository.findAllByOrder(savedOrder));

        return OrderResponse.of(savedOrder);
    }
}
