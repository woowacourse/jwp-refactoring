package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderLineItemRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderLineItemService orderLineItemService;
    private final MenuService menuService;
    private final OrderTableService orderTableService;

    public OrderService(
        final OrderRepository orderRepository,
        final OrderLineItemRepository orderLineItemRepository,
        final OrderLineItemService orderLineItemService,
        final MenuService menuService,
        final OrderTableService orderTableService
    ) {
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderLineItemService = orderLineItemService;
        this.menuService = menuService;
        this.orderTableService = orderTableService;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {
        final List<OrderLineItem> orderLineItems =
            orderLineItemService.findAllByIds(orderRequest.getOrderLineItemIds());

        menuService.checkCount(orderLineItems);
        OrderTable orderTable = orderTableService.findOrderTable(orderRequest.getOrderTableId());

        Order order = new Order(orderTable, OrderStatus.COOKING, orderLineItems);

        return orderRepository.save(order);
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }


    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(OrderStatus.valueOf(orderRequest.getOrderStatus()));

        return savedOrder;
    }
}
