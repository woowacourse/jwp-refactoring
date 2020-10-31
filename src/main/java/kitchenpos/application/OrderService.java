package kitchenpos.application;

import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderLineItemRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.dto.order.OrderLineItemDto;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemDao;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemDao,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId()).orElseThrow(IllegalArgumentException::new);
        Order orderToSave = Order.of(orderTable);
        final Order savedOrder = orderRepository.save(orderToSave);

        addOrderLineItemToOrder(orderRequest, savedOrder);

        return OrderResponse.of(savedOrder);
    }

    private void addOrderLineItemToOrder(OrderRequest orderRequest, Order savedOrder) {
        List<OrderLineItem> orderLineItems = savedOrder.getOrderLineItems();
        for (OrderLineItemDto orderLineItemDto : orderRequest.getOrderLineItems()) {
            Menu menu = menuRepository.findById(orderLineItemDto.getMenuId()).orElseThrow(IllegalArgumentException::new);
            OrderLineItem orderLineItem = new OrderLineItem(savedOrder, menu, orderLineItemDto.getQuantity());
            orderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
    }

    @Transactional
    public List<OrderResponse> list() {
        return OrderResponse.listOf(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest changeRequest) {
        final Order savedOrder = orderRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(changeRequest.getOrderStatus());

        return OrderResponse.of(savedOrder);
    }
}
