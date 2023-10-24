package kitchenpos.application;

import kitchenpos.application.dto.OrderChangeStatusRequest;
import kitchenpos.application.dto.OrderCreateRequest;
import kitchenpos.application.dto.OrderLineItemRequest;
import kitchenpos.domain.*;
import kitchenpos.domain.exception.NotExistMenuException;
import kitchenpos.domain.exception.NotExistOrderException;
import kitchenpos.domain.exception.NotExistOrderTable;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
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
        
        final Order order = new Order(orderTable,
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
                                      findMenuById(orderLineItemRequest.getMenuId()),
                                      orderLineItemRequest.getQuantity()
                              ))
                      .collect(Collectors.toList());
    }
    
    private Menu findMenuById(final Long menuId) {
        return menuRepository.findById(menuId)
                             .orElseThrow(() -> new NotExistMenuException("존재하지 않는 메뉴입니다"));
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
