package kitchenpos.order.application;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.RequestOrderLineItemIsEmptyException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.dto.OrdersCreateRequest;
import kitchenpos.order.dto.OrdersCreateRequest.OrderLineItemDto;
import kitchenpos.order.dto.OrdersResponse;
import kitchenpos.order.dto.OrdersStatusRequest;
import kitchenpos.order.exception.CannotMakeOrderWithEmptyTableException;
import kitchenpos.menu.exception.MenuNotFoundException;
import kitchenpos.order.exception.OrderNotFoundException;
import kitchenpos.table.exception.OrderTableNotFoundException;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository,
            OrderLineItemRepository orderLineItemRepository,
            OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrdersResponse create(OrdersCreateRequest request) {
        List<OrderLineItemDto> orderLineItemDtos = request.getOrderLineItems();
        validateOrderLineItemIsNotEmpty(orderLineItemDtos);

        OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(OrderTableNotFoundException::new);

        validateOrderTableNotEmpty(orderTable);

        Orders orders = orderRepository.save(
                new Orders(orderTable, OrderStatus.COOKING, LocalDateTime.now()));

        for (OrderLineItemDto orderLineItemDto : orderLineItemDtos) {
            orderLineItemRepository.save(new OrderLineItem(orders,
                    menuRepository.findById(orderLineItemDto.getMenuId())
                            .orElseThrow(MenuNotFoundException::new),
                    orderLineItemDto.getQuantity()));
        }

        return OrdersResponse.from(orders);
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

    public List<OrdersResponse> list() {
        List<Orders> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrdersResponse::from)
                .collect(toList());
    }

    @Transactional
    public OrdersResponse changeOrderStatus(Long orderId, OrdersStatusRequest request) {
        Orders orders = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        orders.changeOrderStatus(request.getOrderStatus());

        return OrdersResponse.from(orders);
    }
}
