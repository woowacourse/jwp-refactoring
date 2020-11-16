package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderChangeStatusRequest;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.exception.AlreadyEmptyTableException;
import kitchenpos.exception.EmptyMenuOrderException;
import kitchenpos.exception.MenuNotFoundException;
import kitchenpos.exception.OrderNotFoundException;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderLineItemRepository orderLineItemRepository;

    public OrderService(
        final MenuRepository menuRepository,
        final OrderRepository orderRepository,
        final OrderTableRepository orderTableRepository,
        final OrderLineItemRepository orderLineItemRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderLineItemRepository = orderLineItemRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        List<Long> menuIds = request.getMenuIds();

        if (CollectionUtils.isEmpty(menuIds)) {
            throw new EmptyMenuOrderException();
        }

        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new MenuNotFoundException();
        }

        Long orderTableId = request.getOrderTableId();
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new OrderTableNotFoundException(orderTableId));

        if (orderTable.isEmpty()) {
            throw new AlreadyEmptyTableException(orderTableId);
        }

        Order order = request.toEntity();
        Order savedOrder = orderRepository.save(order);

        final Long orderId = savedOrder.getId();
        List<OrderLineItem> orderLineItems = request.getOrderLineItems().stream()
            .map(item -> item.toEntity(orderId))
            .collect(Collectors.toList());
        orderLineItemRepository.saveAll(orderLineItems);

        return OrderResponse.of(savedOrder);
    }

    public List<OrderResponse> list() {
        return OrderResponse.listOf(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId,
        final OrderChangeStatusRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException(orderId));

        savedOrder.changeOrderStatus(request.getOrderStatus());

        return OrderResponse.of(savedOrder);
    }
}
