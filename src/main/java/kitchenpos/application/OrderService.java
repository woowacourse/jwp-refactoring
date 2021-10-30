package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.exception.menu.NoSuchMenuException;
import kitchenpos.exception.order.CannotChangeOrderStatusAsCompletionException;
import kitchenpos.exception.order.NoSuchOrderException;
import kitchenpos.exception.table.NoSuchOrderTableException;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(NoSuchOrderTableException::new);

        List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItems().stream()
                .map(orderLineItemRequest -> {
                    Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                            .orElseThrow(NoSuchMenuException::new);
                    Long quantity = orderLineItemRequest.getQuantity();
                    return new OrderLineItem(menu, quantity);
                })
                .collect(Collectors.toList());

        Order order = new Order(orderTable, orderLineItems);
        Order savedOrder = orderRepository.save(order);

        orderLineItemRepository.saveAll(orderLineItems);

        return OrderResponse.from(savedOrder);
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(NoSuchOrderException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), order.getOrderStatus())) {
            throw new CannotChangeOrderStatusAsCompletionException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(orderRequest.getOrderStatus());
        order.changeOrderStatus(orderStatus.name());

        return OrderResponse.from(order);
    }
}
