package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Orders;
import kitchenpos.dto.OrdersCreateRequest;
import kitchenpos.dto.OrdersCreateRequest.OrderLineItemDto;
import kitchenpos.dto.OrdersResponse;
import kitchenpos.dto.OrdersStatusRequest;
import kitchenpos.exception.CannotMakeOrderWithEmptyTableException;
import kitchenpos.exception.InvalidRequestParameterException;
import kitchenpos.exception.MenuNotFoundException;
import kitchenpos.exception.OrderNotFoundException;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
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
            throw new InvalidRequestParameterException();
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
