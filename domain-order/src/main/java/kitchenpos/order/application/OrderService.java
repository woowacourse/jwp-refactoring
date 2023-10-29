package kitchenpos.order.application;

import kitchenpos.common.vo.OrderStatus;
import kitchenpos.exception.NotFoundOrderException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.ui.dto.ChangeOrderStatusRequest;
import kitchenpos.order.ui.dto.OrderLineItemDto;
import kitchenpos.order.ui.dto.OrderRequest;
import kitchenpos.order.ui.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(
            final OrderRepository orderRepository,
            final OrderValidator orderValidator
    ) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final OrderLineItems orderLineItems = convertToOrderLineItem(orderRequest.getOrderLineItems());
        final Order order = orderRequest.toEntity(OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
        orderValidator.validate(order);

        final Order savedOrder = orderRepository.save(order);

        return OrderResponse.from(savedOrder);
    }

    private OrderLineItems convertToOrderLineItem(final List<OrderLineItemDto> orderLineItemDtos) {
        final List<OrderLineItem> orderLineItems = orderLineItemDtos.stream()
                                                             .map(OrderLineItemDto::toEntity)
                                                             .collect(Collectors.toList());

        return new OrderLineItems(orderLineItems);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
                     .map(OrderResponse::from)
                     .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest changeStatusRequest) {
        final Order order = orderRepository.findById(orderId)
                                                .orElseThrow(() -> new NotFoundOrderException("해당 주문이 존재하지 않습니다."));

        order.updateOrderStatus(OrderStatus.valueOf(changeStatusRequest.getOrderStatus()));

        return OrderResponse.from(order);
    }
}
