package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderMenuRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderMenuRequest;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.request.OrderStatusRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.exceptions.EntityNotExistException;
import kitchenpos.exceptions.MenuNotExistException;
import kitchenpos.exceptions.OrderAlreadyCompletionException;
import kitchenpos.exceptions.OrderTableEmptyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderMenuRepository orderMenuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository, final OrderMenuRepository orderMenuRepository,
                        final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderMenuRepository = orderMenuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final Order order = convertToOrder(orderRequest);
        validateMenusExist(orderRequest.getOrderLineItems().size(), order);
        validateOrderTableExistAndNotEmpty(orderRequest);

        for (OrderLineItem orderLineItem : order.getOrderLineItems()) {
            orderMenuRepository.save(orderLineItem.getOrderMenu());
        }

        orderRepository.save(order);
        return OrderResponse.from(order);
    }

    private Order convertToOrder(final OrderRequest orderRequest) {
        final List<Long> menuIds = orderRequest.getOrderLineItems()
                .stream()
                .map(OrderMenuRequest::getMenuId)
                .collect(Collectors.toList());
        final List<Menu> menus = menuRepository.findByIdIn(menuIds);

        return orderRequest.toEntity(menus);
    }

    private void validateMenusExist(final int menusSize, final Order order) {
        if (!order.isOrderLineItemsSizeEqualTo(menusSize)) {
            throw new MenuNotExistException();
        }
    }

    private void validateOrderTableExistAndNotEmpty(final OrderRequest orderRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(EntityNotExistException::new);
        if (orderTable.isEmpty()) {
            throw new OrderTableEmptyException();
        }
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(EntityNotExistException::new);
        validateOrderCompletion(savedOrder);
        savedOrder.updateOrderStatus(OrderStatus.valueOf(orderStatusRequest.getOrderStatus()));
        return OrderResponse.from(savedOrder);
    }

    private void validateOrderCompletion(final Order savedOrder) {
        if (savedOrder.isCompletion()) {
            throw new OrderAlreadyCompletionException();
        }
    }
}
