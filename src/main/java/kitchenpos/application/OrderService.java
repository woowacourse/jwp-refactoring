package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.NotFoundException;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.dto.request.OrderCreateRequest;
import kitchenpos.ui.dto.request.OrderLineItemDto;
import kitchenpos.ui.dto.request.OrderStatusChangeRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {

    private static final String NOT_FOUND_ORDER_TABLE_ERROR_MESSAGE = "존재하지 않는 주문테이블입니다.";
    private static final String NOT_FOUND_MENU_ERROR_MESSAGE = "존재하지 않는 메뉴입니다.";
    private static final String NOT_FOUND_ORDER_ERROR_MESSAGE = "존재하지 않는 주문입니다.";

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
    public Order create(final OrderCreateRequest request) {
        final List<OrderLineItemDto> orderItemDtos = request.getOrderLineItems();
        if (CollectionUtils.isEmpty(orderItemDtos)) {
            throw new IllegalArgumentException();
        }

        validateExistMenu(orderItemDtos);
        Long orderTableId = request.getOrderTableId();
        validateOrderTable(orderTableId);

        Order savedOrder = orderRepository.save(
                new Order(orderTableId, OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>()));
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItemDto orderLineItemDto : orderItemDtos) {
            final OrderLineItem orderLineItem = orderLineItemRepository.save(
                    new OrderLineItem(savedOrder.getId(), orderLineItemDto.getMenuId(),
                            orderLineItemDto.getQuantity()));
            savedOrderLineItems.add(orderLineItem);
        }
        savedOrder.addOrderItems(savedOrderLineItems);

        return savedOrder;
    }

    private void validateExistMenu(final List<OrderLineItemDto> orderItemDtos) {
        final List<Long> menuIds = orderItemDtos.stream()
                .map(OrderLineItemDto::getMenuId)
                .collect(Collectors.toList());

        if (orderItemDtos.size() != menuRepository.countByIdIn(menuIds)) {
            throw new NotFoundException(NOT_FOUND_MENU_ERROR_MESSAGE);
        }
    }

    private void validateOrderTable(final Long orderTableId) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ORDER_TABLE_ERROR_MESSAGE));
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ORDER_ERROR_MESSAGE));

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(orderId);
        savedOrder.changeOrderStatus(request.getOrderStatus(), orderLineItems);

        return savedOrder;
    }
}
