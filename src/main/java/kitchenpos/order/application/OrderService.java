package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.request.OrderCreateRequest;
import kitchenpos.order.dto.request.OrderLineItemCreateRequest;
import kitchenpos.order.dto.request.OrderStatusUpdateRequest;
import kitchenpos.order.dto.service.OrderLineItemCreateDto;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository,
        OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(final OrderCreateRequest request) {

        List<OrderLineItemCreateRequest> orderLineItemRequests = request.getOrderLineItems();
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException();
        }

        OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);

        List<OrderLineItemCreateDto> orderLineItems = orderLineItemRequests.stream()
            .map(this::convertItemRequestToDto)
            .collect(Collectors.toUnmodifiableList());

        Order order = new Order(orderTable, orderLineItems);

        return orderRepository.save(order);
    }

    private OrderLineItemCreateDto convertItemRequestToDto(OrderLineItemCreateRequest request) {
        Menu menu = menuRepository.findById(request.getMenuId())
            .orElseThrow(IllegalArgumentException::new);

        return new OrderLineItemCreateDto(menu, request.getQuantity());
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatusUpdateRequest request) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        order.updateStatus(request.getOrderStatus());
        return order;
    }
}
