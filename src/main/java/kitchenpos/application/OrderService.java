package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderHistory;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.NotFoundMenuException;
import kitchenpos.exception.NotFoundOrderException;
import kitchenpos.exception.NotFoundOrderTableException;
import kitchenpos.exception.OrderMenusCountException;
import kitchenpos.exception.OrderTableEmptyException;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderHistoryRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.dto.OrderLineItemDto;
import kitchenpos.ui.dto.request.ChangeOrderStatusRequest;
import kitchenpos.ui.dto.request.OrderCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderHistoryRepository orderHistoryRepository;

    public OrderService(MenuRepository menuRepository,
                        OrderRepository orderRepository,
                        OrderLineItemRepository orderLineItemRepository,
                        OrderTableRepository orderTableRepository,
                        OrderHistoryRepository orderHistoryRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderHistoryRepository = orderHistoryRepository;
    }

    @Transactional
    public Order create(OrderCreateRequest orderCreateRequest) {
        validateOrderLineItems(orderCreateRequest.getOrderLineItems());

        OrderTable orderTable = getOrderTable(orderCreateRequest.getOrderTableId());
        Order savedOrder = saveOrder(orderTable);
        saveOrderLineItems(orderCreateRequest.getOrderLineItems(), savedOrder);

        return savedOrder;
    }

    private void validateOrderLineItems(List<OrderLineItemDto> orderLineItemDtos) {
        List<Long> menuIds = getMenuIds(orderLineItemDtos);
        if (CollectionUtils.isEmpty(menuIds)) {
            throw new OrderMenusCountException();
        }
        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new NotFoundMenuException();
        }
    }

    private List<Long> getMenuIds(List<OrderLineItemDto> orderLineItemDtos) {
        return orderLineItemDtos.stream()
                .map(OrderLineItemDto::getMenuId)
                .collect(Collectors.toList());
    }

    private OrderTable getOrderTable(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(NotFoundOrderTableException::new);
        if (orderTable.isEmpty()) {
            throw new OrderTableEmptyException();
        }
        return orderTable;
    }

    private Order saveOrder(OrderTable orderTable) {
        return orderRepository.save(new Order(orderTable.getId()));
    }

    private void saveOrderLineItems(List<OrderLineItemDto> orderLineItemDtos, Order order) {
        for (OrderLineItemDto dto : orderLineItemDtos) {
            OrderHistory orderHistory = saveOrderHistory(dto.getMenuId());
            orderLineItemRepository.save(new OrderLineItem(order.getId(), orderHistory.getId(), dto.getQuantity()));
        }
    }

    private OrderHistory saveOrderHistory(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(NotFoundMenuException::new);
        return orderHistoryRepository.save(new OrderHistory(menu.getName(), menu.getPrice()));
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(Long orderId, ChangeOrderStatusRequest changeOrderStatusRequest) {
        Order order = findOrder(orderId);
        order.changeOrderStatus(OrderStatus.valueOf(changeOrderStatusRequest.getOrderStatus()));
        return order;
    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(NotFoundOrderException::new);
    }
}
