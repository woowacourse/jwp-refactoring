package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.TableRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.Table;
import kitchenpos.ui.dto.OrderAssembler;
import kitchenpos.ui.dto.OrderChangeStatusRequest;
import kitchenpos.ui.dto.OrderCreateRequest;
import kitchenpos.ui.dto.OrderLineItemsOfOrderRequest;
import kitchenpos.ui.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;

    public OrderService(final MenuRepository menuRepository, final OrderRepository orderRepository,
        final TableRepository tableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.tableRepository = tableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest orderCreateRequest) {
        final List<OrderLineItemsOfOrderRequest> OrderLineItemsOfOrderRequests
            = orderCreateRequest.getOrderLineItems();

        if (CollectionUtils.isEmpty(OrderLineItemsOfOrderRequests)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = OrderLineItemsOfOrderRequests.stream()
            .map(OrderLineItemsOfOrderRequest::getMenuId)
            .collect(Collectors.toList());

        if (OrderLineItemsOfOrderRequests.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final Table table = tableRepository.findById(orderCreateRequest.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);

        if (table.isEmpty()) {
            throw new IllegalArgumentException();
        }

        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();

        List<Long> orderLineItemsOfOrderMenuIds = OrderLineItemsOfOrderRequests.stream()
            .map(OrderLineItemsOfOrderRequest::getMenuId)
            .collect(Collectors.toList());
        List<Menu> foundMenus = menuRepository.findAllById(orderLineItemsOfOrderMenuIds);

        Order order = OrderAssembler.assemble(orderCreateRequest, table, foundMenus);

        final Order savedOrder = orderRepository.save(order);
        return OrderResponse.of(savedOrder);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return OrderResponse.listOf(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId,
        final OrderChangeStatusRequest orderChangeStatusRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus
            .valueOf(orderChangeStatusRequest.getOrderStatus());
        savedOrder.setOrderStatus(orderStatus.name());

        return OrderResponse.of(savedOrder);
    }
}
