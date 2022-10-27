package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderStatusRequest;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository,
        OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {
        if (CollectionUtils.isEmpty(orderRequest.getOrderLineItems())) {
            throw new IllegalArgumentException();
        }

        OrderTable orderTable = findOrderTable(orderRequest);
        validOrderTable(orderTable);

        List<OrderLineItem> orderLineItems = createOrderLineItems(orderRequest);

        Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
        return orderRepository.save(order);
    }

    private OrderTable findOrderTable(OrderRequest orderRequest) {
        return orderTableRepository.findById(orderRequest.getOrderTableId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블입니다"));
    }

    private void validOrderTable(OrderTable orderTable) {
        if(orderTable.isEmpty()){
            throw new IllegalArgumentException("비어있는 테이블에는 주문을 추가할 수 없습니다.");
        }
    }

    private List<OrderLineItem> createOrderLineItems(OrderRequest orderRequest) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();

        for (OrderLineItemRequest orderLineItemRequest : orderRequest.getOrderLineItems()) {
            Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));

            orderLineItems.add(new OrderLineItem(menu, orderLineItemRequest.getQuantity()));
        }

        return orderLineItems;
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order order = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        OrderStatus orderStatus = OrderStatus.valueOf(orderStatusRequest.getOrderStatus());

        order.updateStatus(orderStatus);
        return order;
    }
}
