package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.dto.request.CreateOrderRequest;
import kitchenpos.dto.request.OrderLineItemDto;
import kitchenpos.dto.request.PutOrderStatusRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(final CreateOrderRequest orderRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                                                          .orElseThrow(IllegalArgumentException::new);
        validateEmptyTable(orderTable);
        final Order order = new Order(orderTable.getId(), OrderStatus.COOKING);
        final Order savedOrder = orderRepository.save(order);
        final OrderLineItems orderLineItems = createOrderLineItems(orderRequest);
        savedOrder.addOrderItems(orderLineItems);
        return savedOrder;
    }

    private void validateEmptyTable(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private OrderLineItems createOrderLineItems(final CreateOrderRequest orderRequest) {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        final List<Long> menuIds = new ArrayList<>();
        for (final OrderLineItemDto orderLineItemDto : orderRequest.getOrderLineItems()) {
            final Menu menu = menuRepository.findById(orderLineItemDto.getMenuId())
                                            .orElseThrow(() -> new IllegalArgumentException("잘못된 메뉴입니다."));
            final OrderLineItem orderLineItem = OrderLineItem.of(menu, orderLineItemDto.getQuantity());
            orderLineItems.add(orderLineItem);
            menuIds.add(menu.getId());
        }
        return OrderLineItems.of(orderLineItems, (int) menuRepository.countByIdIn(menuIds));
    }

    @Transactional(readOnly = true)
    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final PutOrderStatusRequest orderStatusRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                                                .orElseThrow(IllegalArgumentException::new);
        savedOrder.changeOrderStatus(orderStatusRequest.getOrderStatus());
        return savedOrder;
    }
}
