package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;

@Service
public class OrderService {
    private final OrderDao orderDao;
    private final MenuService menuService;
    private final OrderLineItemService orderLineItemService;
    private final TableService tableService;

    public OrderService(
            final OrderDao orderDao,
            final MenuService menuService,
            final OrderLineItemService orderLineItemService,
            final TableService tableService
    ) {
        this.orderDao = orderDao;
        this.menuService = menuService;
        this.orderLineItemService = orderLineItemService;
        this.tableService = tableService;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {
        validateOrderCreation(orderRequest);
        final OrderTable orderTable = tableService.findById(orderRequest.getOrderTableId());
        final Order order = orderRequest.toEntity(orderTable);
        final Order savedOrder = orderDao.save(order);
        final List<OrderLineItem> orderLineItemList = orderLineItemService.saveAll(savedOrder, orderRequest.getOrderLineItemRequests());
        savedOrder.addOrderLineItems(orderLineItemList);
        return savedOrder;
    }

    private void validateOrderCreation(OrderRequest orderRequest) {
        validateOrderLineItemEmpty(orderRequest);
        validateSavedMenuCount(orderRequest);
        validateOrderTableEmpty(orderRequest);
    }

    private void validateOrderLineItemEmpty(OrderRequest orderRequest) {
        final List<OrderLineItemRequest> orderLineItemRequests = orderRequest.getOrderLineItemRequests();
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException("주문 항목이 비어있습니다.");
        }
    }

    private void validateSavedMenuCount(OrderRequest orderRequest) {
        final List<Long> menuIds = orderRequest.getOrderLineItemRequests()
                                               .stream()
                                               .map(OrderLineItemRequest::getMenuId)
                                               .collect(Collectors.toList());

        if (menuIds.size() != menuService.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("저장되어 있는 메뉴보다 더 많은 메뉴가 입력되었습니다.");
        }
    }

    private void validateOrderTableEmpty(OrderRequest orderRequest) {
        final OrderTable orderTable = tableService.findById(orderRequest.getOrderTableId());
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어있습니다.");
        }
    }

    public List<Order> list() {
        return orderDao.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = findById(orderId);

        if (savedOrder.isCompletion()) {
            throw new IllegalArgumentException("이미 식사가 끝난 주문이기 때문에 상태를 변경할 수 없습니다.");
        }

        savedOrder.changeStatus(orderStatus);

        return savedOrder;
    }

    private Order findById(Long orderId) {
        return orderDao.findById(orderId)
                       .orElseThrow(() -> new IllegalArgumentException("OrderId에 해당하는 주문이 존재하지 않습니다."));
    }

    public boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> orderStatuses) {
        return orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatuses);
    }
}
