package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderCreateRequest.OrderLineItemDto;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderStatusRequest;
import kitchenpos.exception.CannotMakeOrderWithEmptyTableException;
import kitchenpos.exception.MenuNotFoundException;
import kitchenpos.exception.OrderNotFoundException;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.exception.RequestOrderLineItemIsEmptyException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.Order.MenuIdQuantityAndPrice;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
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

    public OrderResponse create(OrderCreateRequest request) {
        List<OrderLineItemDto> orderLineItemDtos = request.getOrderLineItems();
        validateOrderLineItemIsNotEmpty(orderLineItemDtos);

        OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(OrderTableNotFoundException::new);

        validateOrderTableNotEmpty(orderTable);

        List<Long> menuIds = orderLineItemDtos.stream()
                .map(OrderLineItemDto::getMenuId)
                .collect(toList());
        Map<Long, Menu> menuById = menuRepository.findAllByIdIn(menuIds).stream()
                .collect(Collectors.toMap(Menu::getId, Function.identity()));
        validateRequestedMenuesExists(menuIds, menuById);

        Order order = Order.of(orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(),
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

    private void validateRequestedMenuesExists(List<Long> menuIds, Map<Long, Menu> menuById) {
        if (menuIds.size() != menuById.size()) {
            throw new MenuNotFoundException();
        }
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(toList());
    }

    public OrderResponse changeOrderStatus(Long orderId, OrderStatusRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        order.changeOrderStatus(request.getOrderStatus());

        return OrderResponse.from(order);
    }
}
