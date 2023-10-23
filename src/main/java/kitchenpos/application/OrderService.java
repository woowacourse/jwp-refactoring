package kitchenpos.application;

import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.ui.dto.OrderLineItemRequest;
import kitchenpos.ui.dto.OrderRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(final OrderRequest order) {
        List<OrderLineItem> orderLineItems = toOrderLineItems(order.getOrderLineItems());

        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        return orderRepository.save(new Order(orderTable, orderLineItems));
    }

    private List<OrderLineItem> toOrderLineItems(List<OrderLineItemRequest> orderLineItemsRequests) {
        if (Objects.nonNull(orderLineItemsRequests)) {
            return orderLineItemsRequests.stream()
                    .map(it -> new OrderLineItem(
                            menuRepository.findById(it.getMenuId()).orElseThrow(IllegalArgumentException::new),
                            it.getQuantity()
                    ))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Transactional(readOnly = true)
    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (orderStatus == OrderStatus.COOKING) {
            savedOrder.cook();
        }
        if (orderStatus == OrderStatus.MEAL) {
            savedOrder.serve();
        }
        if (orderStatus == OrderStatus.COMPLETION) {
            savedOrder.complete();
        }
        return savedOrder;
    }
}
