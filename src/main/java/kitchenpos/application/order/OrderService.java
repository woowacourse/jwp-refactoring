package kitchenpos.application.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.domain.Quantity;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.request.OrderChangeStatusRequest;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemCreateRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.exception.CustomError;
import kitchenpos.exception.NotFoundException;
import kitchenpos.repository.menu.MenuRepository;
import kitchenpos.repository.order.OrderRepository;
import kitchenpos.repository.table.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;

    public OrderService(final OrderTableRepository orderTableRepository,
                        final OrderRepository orderRepository, final MenuRepository menuRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        final OrderTable orderTable = findOrderTableById(request);
        orderTable.validateNotEmpty();
        final Order order = new Order(orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(),
                mapToOrderLineItems(request));
        orderRepository.save(order);
        return OrderResponse.from(order);
    }

    private List<OrderLineItem> mapToOrderLineItems(final OrderCreateRequest request) {
        return request.getOrderLineItems()
                .stream()
                .map(item -> {
                    final Menu menu = getMenuById(item);
                    return new OrderLineItem(item.getMenuId(), new Quantity(item.getQuantity()),
                            new Name(menu.getName()), new Price(menu.getPrice()));
                }).collect(Collectors.toList());
    }

    private Menu getMenuById(OrderLineItemCreateRequest item) {
        return menuRepository.findById(item.getMenuId())
                .orElseThrow(() -> new NotFoundException(CustomError.MENU_NOT_FOUND_ERROR));
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderChangeStatusRequest request) {
        final Order order = findOrderById(orderId);
        order.changeStatus(OrderStatus.from(request.getOrderStatus()));
        return OrderResponse.from(order);
    }

    private OrderTable findOrderTableById(final OrderCreateRequest request) {
        return orderTableRepository.findById(request.getTableId())
                .orElseThrow(() -> new NotFoundException(CustomError.TABLE_NOT_FOUND_ERROR));
    }

    private Order findOrderById(final Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(CustomError.ORDER_NOT_FOUND_ERROR));
    }
}
