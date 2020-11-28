package kitchenpos.order.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.model.Order;
import kitchenpos.orderline.model.OrderLineItem;
import kitchenpos.order.model.OrderStatus;
import kitchenpos.ordertable.model.OrderTable;
import kitchenpos.order.model.OrderVerifier;
import kitchenpos.order.application.dto.OrderCreateRequestDto;
import kitchenpos.orderline.application.dto.OrderLineCreateRequestDto;
import kitchenpos.order.application.dto.OrderResponseDto;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.orderline.repository.OrderLineItemRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.repository.OrderTableRepository;

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
    public OrderResponseDto create(final OrderCreateRequestDto orderCreateRequest) {
        final List<OrderLineItem> orderLineItems = orderCreateRequest.getOrderLineCreateRequests().stream()
            .map(OrderLineCreateRequestDto::toEntity)
            .collect(Collectors.toList());

        final List<Long> menuIds = orderLineItems.stream()
            .map(OrderLineItem::getMenuId)
            .collect(Collectors.toList());

        final int savedMenuCount = menuRepository.countAllByIds(menuIds);
        final Long orderTableId = orderCreateRequest.getOrderTableId();
        final OrderTable orderTable = findBy(orderTableId);

        OrderVerifier.validateOrderCreation(orderLineItems, savedMenuCount, orderTable);

        final Order savedOrder = orderRepository.save(orderCreateRequest.toEntity());
        final Long orderId = savedOrder.getId();
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.changeOrderId(orderId);
        }
        orderLineItemRepository.saveAll(orderLineItems);

        return OrderResponseDto.from(savedOrder, savedOrderLineItems);
    }

    private OrderTable findBy(Long orderTableId) {
        if (Objects.isNull(orderTableId)) {
            throw new IllegalArgumentException("존재하지 않는 테이블에 주문할 수 없습니다.");
        }

        return orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블에 주문할 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> list() {
        final List<Order> orders = orderRepository.findAll();

        List<OrderResponseDto> orderResponses = new ArrayList<>();
        for (final Order order : orders) {
            List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(order.getId());
            orderResponses.add(OrderResponseDto.from(order, orderLineItems));
        }

        return orderResponses;
    }

    @Transactional
    public OrderResponseDto changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문번호입니다."));

        order.changeOrderStatus(orderStatus);
        Order savedOrder = orderRepository.save(order);

        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(savedOrder.getId());

        return OrderResponseDto.from(savedOrder, orderLineItems);
    }
}
