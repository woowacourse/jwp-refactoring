package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.domain.OrderedMenu;
import kitchenpos.order.domain.Price;
import kitchenpos.order.exception.NotFoundException;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.order.ui.dto.request.OrderCreateRequest;
import kitchenpos.order.ui.dto.request.OrderLineItemDto;
import kitchenpos.order.ui.dto.request.OrderStatusChangeRequest;
import kitchenpos.order.ui.dto.response.OrderCreateResponse;
import kitchenpos.order.ui.dto.response.OrderFindAllResponse;
import kitchenpos.order.ui.dto.response.OrderStatusChangeResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public OrderCreateResponse create(final OrderCreateRequest request) {
        final List<OrderLineItemDto> orderItemDtos = request.getOrderLineItems();
        validateExistMenu(orderItemDtos);
        final Long orderTableId = request.getOrderTableId();
        validateOrderTable(orderTableId);

        final Order order = orderRepository.save(new Order(orderTableId, OrderStatus.COOKING, LocalDateTime.now()));
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (final OrderLineItemDto orderLineItemDto : orderItemDtos) {
            final Long menuId = orderLineItemDto.getMenuId();
            final OrderLineItem orderLineItem = orderLineItemRepository.save(
                    new OrderLineItem(order, getOrderedMenu(menuId), orderLineItemDto.getQuantity()));
            orderLineItems.add(orderLineItem);
        }

        return OrderCreateResponse.of(order, orderLineItems);
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

    private OrderedMenu getOrderedMenu(final Long menuId) {
        final Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MENU_ERROR_MESSAGE));
        return new OrderedMenu(menu.getName(), new Price(menu.getPrice()));
    }

    public List<OrderFindAllResponse> list() {
        List<Order> orders = orderRepository.findAll();
        List<OrderFindAllResponse> responses = new ArrayList<>();
        for (Order order : orders) {
            List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(order.getId());
            responses.add(OrderFindAllResponse.of(order, orderLineItems));
        }
        return responses;
    }

    @Transactional
    public OrderStatusChangeResponse changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ORDER_ERROR_MESSAGE));

        final List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(orderId);
        savedOrder.changeOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));

        return OrderStatusChangeResponse.of(savedOrder, orderLineItems);
    }
}
