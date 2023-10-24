package kitchenpos.application;

import kitchenpos.application.response.OrderResponse;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderCustomDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.application.request.OrderLineItemsRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuDao menuDao;
    private final OrderCustomDao orderCustomDao;
    private final OrderTableDao orderTableDao;

    public OrderService(final MenuDao menuDao, final OrderCustomDao orderCustomDao, final OrderTableDao orderTableDao) {
        this.menuDao = menuDao;
        this.orderCustomDao = orderCustomDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderResponse create(final Long orderTableId, final List<OrderLineItemsRequest> orderLineItemRequests) {
        validateOrderMenus(orderLineItemRequests);

        final OrderTable orderTable = orderTableDao.findById(orderTableId).orElseThrow(IllegalArgumentException::new);
        validateOrderTableStatus(orderTable);

        final List<OrderLineItem> orderLineItems = orderLineItemRequests
                .stream().map(OrderLineItemsRequest::toEntity)
                .collect(Collectors.toList());

        return OrderResponse.from(orderCustomDao.save(
                new Order(null, orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems)
        ));
    }

    private void validateOrderMenus(final List<OrderLineItemsRequest> orderLineItemRequests) {
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItemRequests.stream()
                .map(OrderLineItemsRequest::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItemRequests.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private static void validateOrderTableStatus(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderCustomDao.findAll();
        return OrderResponse.from(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = orderCustomDao.findMandatoryById(orderId);
        savedOrder.changeOrderStatus(orderStatus.name());

        orderCustomDao.save(savedOrder);

        return OrderResponse.from(savedOrder);
    }
}
