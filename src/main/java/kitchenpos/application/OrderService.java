package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.exception.menu.NoSuchMenuException;
import kitchenpos.exception.table.NoSuchOrderTableException;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    public Order create(final OrderRequest orderRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(NoSuchOrderTableException::new);

        List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItemRequests().stream()
                .map(orderLineItemRequest -> {
                    Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                            .orElseThrow(NoSuchMenuException::new);
                    Long quantity = orderLineItemRequest.getQuantiy();
                    return new OrderLineItem(menu, quantity);
                })
                .collect(Collectors.toList());

        Order order = new Order(orderTable, orderLineItems);

        orderLineItemRepository.saveAll(orderLineItems);

        return orderRepository.save(order);
    }

    public List<Order> list() {
        final List<Order> orders = orderRepository.findAll();

        // todo Response dto 만들어 OrderLineItems 담기

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(orderRequest.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus.name());

        orderRepository.save(savedOrder);

//        todo Response DTO 만들어 담아내기
//        savedOrder.setOrderLineItems(orderLineItemRepository.findAllByOrderId(orderId));

        return savedOrder;
    }
}
