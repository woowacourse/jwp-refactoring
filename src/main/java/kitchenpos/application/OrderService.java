package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.dto.OrderChangeStatusRequest;
import kitchenpos.ui.dto.OrderRequest;
import kitchenpos.ui.dto.OrderRequest.OrderInnerLineItems;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuDao menuDao;
    private final OrderTableRepository orderTables;
    private final OrderRepository orders;

    public OrderService(final MenuDao menuDao,
                        final OrderTableRepository orderTables,
                        final OrderRepository orders) {
        this.menuDao = menuDao;
        this.orderTables = orderTables;
        this.orders = orders;
    }

    @Transactional
    public Order create(final OrderRequest request) {
        final var orderLineItemRequests = request.getOrderLineItems();
        final List<Long> menuIds = collectMenuIds(orderLineItemRequests);

        // 중복 메뉴면 안된다 && DB에 없는 메뉴면 안된다 ?
        if (orderLineItemRequests.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTables.get(request.getOrderTableId());
        final var order = new Order(orderTable, mapToOrderLineItems(orderLineItemRequests));

        return orders.add(order);
    }

    private List<Long> collectMenuIds(final List<OrderInnerLineItems> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(OrderInnerLineItems::getMenuId)
                .collect(Collectors.toList());
    }

    private List<OrderLineItem> mapToOrderLineItems(final List<OrderInnerLineItems> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(request -> new OrderLineItem(
                        null,
                        request.getMenuId(),
                        request.getQuantity()))
                .collect(Collectors.toList());
    }

    public List<Order> list() {
        return orders.getAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderChangeStatusRequest request) {
        final Order order = orders.get(orderId);
        order.changeStatus(request.getOrderStatus());

        return orders.add(order);
    }
}
