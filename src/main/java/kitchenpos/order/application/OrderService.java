package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.MenuSnapshot;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.exception.NotExistMenuException;
import kitchenpos.order.controller.dto.OrderChangeStatusRequest;
import kitchenpos.order.controller.dto.OrderCreateRequest;
import kitchenpos.order.controller.dto.OrderLineItemRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.exception.NotExistOrderException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import kitchenpos.ordertable.exception.NotExistOrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    
    private final MenuRepository menuRepository;
    
    private final OrderTableRepository orderTableRepository;
    
    private final OrderRepository orderRepository;
    
    public OrderService(final MenuRepository menuRepository,
                        final OrderTableRepository orderTableRepository,
                        final OrderRepository orderRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }
    
    @Transactional
    public Order create(final OrderCreateRequest request) {
        final OrderTable orderTable = getOrderTable(request.getOrderTableId());
        final OrderStatus orderStatus = OrderStatus.from(request.getOrderStatus());
        final List<OrderLineItem> orderLineItems = getOrderLineItems(request.getOrderLineItemRequests());
        
        final Order order = Order.of(orderTable,
                orderStatus,
                orderLineItems);
        return orderRepository.save(order);
    }
    
    private OrderTable getOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                                   .orElseThrow(() -> new NotExistOrderTable("존재하지 않는 테이블 입니다"));
    }
    
    private List<OrderLineItem> getOrderLineItems(final List<OrderLineItemRequest> request) {
        return request.stream()
                      .map(orderLineItemRequest ->
                              new OrderLineItem(null,
                                      createMenuSnapshot(orderLineItemRequest.getMenuId()),
                                      orderLineItemRequest.getQuantity()
                              ))
                      .collect(Collectors.toList());
    }
    
    private MenuSnapshot createMenuSnapshot(final Long menuId) {
        Menu savedMenu = menuRepository.findById(menuId)
                                       .orElseThrow(() -> new NotExistMenuException("존재하지 않는 메뉴입니다"));
        return MenuSnapshot.from(savedMenu);
    }
    
    public List<Order> list() {
        return orderRepository.findAll();
    }
    
    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderChangeStatusRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                                                .orElseThrow(() -> new NotExistOrderException("존재하지 않는 주문입니다"));
        savedOrder.changeOrderStatus(OrderStatus.from(request.getOrderStatus()));
        return savedOrder;
    }
}
