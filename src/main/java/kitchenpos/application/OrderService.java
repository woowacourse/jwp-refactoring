package kitchenpos.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.application.dto.ChangeOrderStatusRequest;
import kitchenpos.application.dto.OrderRequest;
import kitchenpos.application.dto.OrderResponse;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
        final MenuRepository menuRepository,
        final OrderRepository orderRepository,
        final OrderLineItemRepository orderLineItemDao,
        final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemDao;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        final List<OrderLineItem> orderLineItems = request.getOrderLineItems().stream()
            .map(it -> new OrderLineItem(
                    menuRepository.findById(it.getMenuId()).orElseThrow(NoSuchElementException::new), it.getQuantity()
                )
            )
            .collect(Collectors.toList());

        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
            .orElseThrow(NoSuchElementException::new);

        final Order savedOrder = orderRepository.save(new Order(orderTable, orderLineItems));
        orderLineItemRepository.saveAll(orderLineItems);

        return OrderResponse.of(savedOrder);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
            .map(OrderResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(NoSuchElementException::new);

        savedOrder.changeOrderStatus(request.getOrderStatus());

        return OrderResponse.of(orderRepository.save(savedOrder));
    }
}
