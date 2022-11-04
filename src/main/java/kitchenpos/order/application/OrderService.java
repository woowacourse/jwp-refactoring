package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.application.dto.OrderRequest;
import kitchenpos.order.application.dto.OrderStatusUpdateRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import kitchenpos.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final ProductRepository productRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {
        if (CollectionUtils.isEmpty(orderRequest.getOrderLineItemsRequest())) {
            throw new IllegalArgumentException();
        }

        final List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItemsRequest().stream()
                .map(request -> request.toOrderLineItem(menuRepository.findById(request.getMenuId())
                        .orElseThrow(IllegalArgumentException::new)))
                .collect(Collectors.toList());

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        final Order order = orderRequest.toOrder(orderTable, orderLineItems);

        return orderRepository.save(order);
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatusUpdateRequest orderStatusUpdateRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        final OrderStatus orderStatus = orderStatusUpdateRequest.toOrderStatus();
        savedOrder.changeOrderStatus(orderStatus);

        return savedOrder;
    }
}
