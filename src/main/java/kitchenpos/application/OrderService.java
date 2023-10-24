package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.entity.Order;
import kitchenpos.domain.entity.OrderLineItem;
import kitchenpos.domain.entity.OrderStatus;
import kitchenpos.domain.entity.OrderTable;
import kitchenpos.dto.request.order.ChangeOrderRequest;
import kitchenpos.dto.request.order.CreateOrderRequest;
import kitchenpos.dto.OrderLineItemsDto;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.exception.EmptyListException;
import kitchenpos.exception.EmptyTableException;
import kitchenpos.exception.GroupTableException;
import kitchenpos.exception.NoSuchDataException;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository,
                        OrderLineItemRepository orderLineItemRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderResponse create(final CreateOrderRequest request) {
        final List<OrderLineItemsDto> orderLineItemDtos = request.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItemDtos)) {
            throw new EmptyListException("아이템이 비어있습니다.");
        }

        final List<Long> menuIds = orderLineItemDtos.stream()
                .map(OrderLineItemsDto::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItemDtos.size() != menuRepository.countByIdIn(menuIds)) {
            throw new NoSuchDataException("입력한 메뉴들이 일치하지 않습니다.");
        }

        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(()->new NoSuchDataException("입력한 id의 테이블이 존재하지 않습니다."));

        if (orderTable.isEmpty()) {
            throw new EmptyTableException("비어있는 테이블의 주문은 생성할 수 없습니다.");
        }

        final Order order = Order.builder()
                .orderTableId(request.getOrderTableId())
                .orderStatus(OrderStatus.COOKING)
                .orderedTime(LocalDateTime.now())
                .build();

        final Long orderId = orderRepository.save(order).getId();
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        final List<OrderLineItem> orderLineItems = orderLineItemDtos.stream()
                .map(OrderLineItem::from)
                .collect(Collectors.toList());
        for (final OrderLineItem orderLineItem : orderLineItems) {
            final OrderLineItem newOrderLineItem = new OrderLineItem(
                    orderLineItem.getSeq(),
                    orderId,
                    orderLineItem.getMenuId(),
                    orderLineItem.getQuantity()
            );
            savedOrderLineItems.add(orderLineItemRepository.save(newOrderLineItem));
        }

        return OrderResponse.from(Order.builder()
                .id(orderId)
                .orderTableId(order.getOrderTable())
                .orderStatus(order.getOrderStatus())
                .orderedTime(order.getOrderedTime())
                .orderLineItems(savedOrderLineItems)
                .build()
        );
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(()->new NoSuchDataException("해당하는 id의 주문이 존재하지 않습니다."));

        if (Objects.equals(OrderStatus.COMPLETION, savedOrder.getOrderStatus())) {
            throw new GroupTableException("계산이 완료된 주문의 상태를 변경할 수 없습니다.");
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());

        final Order order = Order.builder()
                .id(orderId)
                .orderTableId(savedOrder.getOrderTable())
                .orderStatus(orderStatus)
                .orderedTime(savedOrder.getOrderedTime())
                .orderLineItems(orderLineItemRepository.findAllByOrderId(orderId))
                .build();

        orderRepository.save(order);

        return OrderResponse.from(order);
    }
}
