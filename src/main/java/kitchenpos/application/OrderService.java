package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderLineItemRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.TableRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Quantity;
import kitchenpos.dto.OrderLineItemDto;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.exception.EmptyTableOrderException;
import kitchenpos.exception.MenuNotEnoughException;
import kitchenpos.exception.MenuNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final TableRepository tableRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository,
                        OrderLineItemRepository orderLineItemRepository, TableRepository tableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.tableRepository = tableRepository;
    }

    @Transactional
    public OrderResponse create(OrderRequest orderRequest) {
        List<OrderLineItemDto> orderLineItemDtos = orderRequest.getOrderLineItems();
        validateMenuCount(orderLineItemDtos);
        OrderTable orderTable = findOrderTable(orderRequest);
        validateOrderTableEmptiness(orderTable);
        Order order = new Order(orderTable, OrderStatus.COOKING, new ArrayList<>());
        Order savedOrder = orderRepository.save(order);
        createOrderLineItem(orderLineItemDtos, savedOrder);
        return new OrderResponse(savedOrder);
    }

    private void createOrderLineItem(List<OrderLineItemDto> orderLineItemDtos, Order savedOrder) {
        for (OrderLineItemDto orderLineItemDto : orderLineItemDtos) {
            OrderLineItem orderLineItem = new OrderLineItem(
                    savedOrder,
                    findMenu(orderLineItemDto.getMenuId()),
                    new Quantity(orderLineItemDto.getQuantity()));
            orderLineItemRepository.save(orderLineItem);
        }
    }

    private Menu findMenu(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(MenuNotFoundException::new);
    }

    private void validateOrderTableEmptiness(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new EmptyTableOrderException();
        }
    }

    private OrderTable findOrderTable(OrderRequest orderRequest) {
        return tableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
    }

    private void validateMenuCount(List<OrderLineItemDto> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new MenuNotEnoughException();
        }
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::new)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public Order changeOrderStatus(Long orderId, OrderRequest orderRequest) {
        Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        OrderStatus orderStatus = OrderStatus.from(orderRequest.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus);
        return savedOrder;
    }
}
