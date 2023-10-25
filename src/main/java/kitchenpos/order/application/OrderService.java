package kitchenpos.order.application;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.Order.MenuIdQuantityAndPrice;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderCreateRequest.OrderLineItemDto;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.exception.CannotMakeOrderWithEmptyTableException;
import kitchenpos.order.exception.MenuNotFoundException;
import kitchenpos.order.exception.OrderNotFoundException;
import kitchenpos.order.exception.RequestOrderLineItemIsEmptyException;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.exception.OrderTableNotFoundException;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    public OrderResponse create(OrderCreateRequest request) {
        List<OrderLineItemDto> orderLineItemDtos = request.getOrderLineItemDtos();
        validateOrderLineItemIsNotEmpty(orderLineItemDtos);

        OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(OrderTableNotFoundException::new);

        validateOrderTableNotEmpty(orderTable);

        List<Long> menuIds = orderLineItemDtos.stream()
                .map(OrderLineItemDto::getMenuId)
                .collect(toList());
        Map<Long, Menu> menuById = menuRepository.findAllByIdIn(menuIds).stream()
                .collect(Collectors.toMap(Menu::getId, Function.identity()));
        if (menuIds.size() != menuById.size()) {
            throw new MenuNotFoundException();
        }

        Order order = Order.of(orderTable, OrderStatus.COOKING, LocalDateTime.now(),
                orderLineItemDtos.stream()
                        .map(dto -> new MenuIdQuantityAndPrice(dto.getMenuId(), dto.getQuantity(),
                                menuById.get(dto.getMenuId()).getPrice()))
                        .collect(toList()));

        return OrderResponse.from(order);
    }

    private void validateOrderLineItemIsNotEmpty(List<OrderLineItemDto> orderLineItemDtos) {
        if (CollectionUtils.isEmpty(orderLineItemDtos)) {
            throw new RequestOrderLineItemIsEmptyException();
        }
    }

    private void validateOrderTableNotEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new CannotMakeOrderWithEmptyTableException();
        }
    }

    public List<OrderResponse> list() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, OrderStatusRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        order.changeOrderStatus(request.getOrderStatus());

        return OrderResponse.from(order);
    }
}
