package kitchenpos.order.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.application.MenuService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.repository.OrderDao;
import kitchenpos.order.repository.OrderTableDao;

@Service
public class OrderService {
    private final OrderDao orderDao;
    private final MenuService menuService;
    private final OrderLineItemService orderLineItemService;
    private final OrderTableDao orderTableDao;

    public OrderService(
            final OrderDao orderDao,
            final MenuService menuService,
            final OrderLineItemService orderLineItemService,
            final OrderTableDao orderTableDao
    ) {
        this.orderDao = orderDao;
        this.menuService = menuService;
        this.orderLineItemService = orderLineItemService;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {
        validateOrderCreation(orderRequest);
        final OrderTable orderTable = orderTableDao.findById(orderRequest.getOrderTableId())
                                                   .orElseThrow(() -> new IllegalArgumentException("OrderTableId에 해당하는 주문 테이블이 존재하지 않습니다."));

        final Order order = orderRequest.toEntity(orderTable);
        order.validate();

        final Order savedOrder = orderDao.save(order);

        final List<OrderLineItem> orderLineItemList = orderLineItemService.saveAll(savedOrder, orderRequest.getOrderLineItemRequests());
        savedOrder.addOrderLineItems(orderLineItemList);
        return savedOrder;
    }

    private void validateOrderCreation(OrderRequest orderRequest) {
        validateOrderLineItemEmpty(orderRequest);
        validateSavedMenuCount(orderRequest);
    }

    private void validateOrderLineItemEmpty(OrderRequest orderRequest) {
        final List<OrderLineItemRequest> orderLineItemRequests = orderRequest.getOrderLineItemRequests();
        if (orderLineItemRequests.isEmpty()) {
            throw new IllegalArgumentException("주문 항목이 비어있습니다.");
        }
    }

    private void validateSavedMenuCount(OrderRequest orderRequest) {
        final List<Long> menuIds = orderRequest.getMenuIds();
        if (menuIds.size() != menuService.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("저장되어 있는 메뉴보다 더 많은 메뉴가 입력되었습니다.");
        }
    }

    public List<Order> list() {
        return orderDao.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = findById(orderId);
        savedOrder.changeStatus(orderStatus);
        return savedOrder;
    }

    private Order findById(Long orderId) {
        return orderDao.findById(orderId)
                       .orElseThrow(() -> new IllegalArgumentException("OrderId에 해당하는 주문이 존재하지 않습니다."));
    }
}
