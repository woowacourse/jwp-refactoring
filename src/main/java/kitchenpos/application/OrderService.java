package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.application.dto.request.OrderLineItemRequest;
import kitchenpos.application.dto.request.OrderRequest;
import kitchenpos.application.dto.request.OrderStatusRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final MenuProductRepository menuProductRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderLineItemRepository orderLineItemRepository;

    public OrderService(
            MenuRepository menuRepository,
            OrderRepository orderRepository,
            OrderTableRepository orderTableRepository,
            OrderLineItemRepository orderLineItemRepository,
            MenuProductRepository menuProductRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.menuProductRepository = menuProductRepository;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        final Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now());
        final Order savedOrder = orderRepository.save(order);

        final List<OrderLineItem> orderLineItems = toOrderLineItems(order, orderRequest.getOrderLineItems());
        validateOrderLineItemsIsNotDuplicatedAndExist(orderRequest.getOrderLineItems(), orderLineItems);
        validateOrderLineItemsIsNotEmpty(orderLineItems);

        return savedOrder;
    }

    private void validateOrderLineItemsIsNotEmpty(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderLineItemsIsNotDuplicatedAndExist(
            List<OrderLineItemRequest> orderLineItemRequests, List<OrderLineItem> orderLineItems) {
        if (orderLineItems.size() != orderLineItemRequests.size()) {
            throw new IllegalArgumentException();
        }
    }

    private List<OrderLineItem> toOrderLineItems(Order order, List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(orderLineItemRequest -> toOrderLineItem(order, orderLineItemRequest))
                .distinct()
                .collect(Collectors.toList());
    }

    private OrderLineItem toOrderLineItem(Order order, OrderLineItemRequest orderLineItemRequest) {
        final MenuProduct menuProduct = menuProductRepository.findById(
                orderLineItemRequest.getMenuId())
                .orElseThrow(IllegalArgumentException::new);
        final Product product = menuProduct.getProduct();

        return orderLineItemRepository.save(new OrderLineItem(
                order,
                product.getName(),
                product.getPrice(),
                orderLineItemRequest.getQuantity()));
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        validateOrderIsNotComplete(savedOrder);

        savedOrder.changeOrderStatus(OrderStatus.valueOf(orderStatusRequest.getOrderStatus()));
        return savedOrder;
    }

    private void validateOrderIsNotComplete(Order savedOrder) {
        if (savedOrder.isCompletion()) {
            throw new IllegalArgumentException();
        }
    }
}
