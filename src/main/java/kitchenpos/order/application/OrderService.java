package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.ui.dto.request.OrderChangeStatusRequest;
import kitchenpos.order.ui.dto.request.OrderCreatedRequest;
import kitchenpos.order.ui.dto.response.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository, OrderLineItemRepository orderLineItemRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreatedRequest orderCreatedRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderCreatedRequest.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException(orderCreatedRequest.getOrderTableId() + " 테이블이 존재하지 않습니다."));

        final Order savedOrder = orderRepository.save(Order.create(orderTable));

        final List<OrderLineItem> orderLineItems = orderCreatedRequest.getOrderLineItemRequests()
                .stream()
                .map(orderLineItemRequest -> {
                    final Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                            .orElseThrow(() -> new IllegalArgumentException("Menu id : " + orderLineItemRequest.getMenuId() + "는 등록되지 않아 주문할 수 없습니다."));
                    return OrderLineItem.create(savedOrder, menu, orderLineItemRequest.getQuantity());
                })
                .collect(toList());

        List<OrderLineItem> savedOrderLineItems = orderLineItemRepository.saveAll(orderLineItems);

        return OrderResponse.create(savedOrder, savedOrderLineItems);
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(order -> OrderResponse.create(order, orderLineItemRepository.findAllByOrder(order)))
                .collect(toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderChangeStatusRequest orderChangeStatusRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Id : " + orderId + "인 주문이 존재하지 않습니다."));

        if (savedOrder.isCompletion()) {
            throw new IllegalArgumentException("orderId : " + orderId + " : 완료된 주문은 상태를 변경할 수 없습니다.");
        }

        savedOrder.changeOrderStatus(orderChangeStatusRequest.getOrderStatus());
        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrder(savedOrder);

        return OrderResponse.create(savedOrder, orderLineItems);
    }
}
