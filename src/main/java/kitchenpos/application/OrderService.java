package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Orders;
import kitchenpos.dto.OrdersCreateRequest;
import kitchenpos.dto.OrdersCreateRequest.OrderLineItemDto;
import kitchenpos.dto.OrdersResponse;
import kitchenpos.dto.OrdersStatusRequest;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository,
            OrderLineItemRepository orderLineItemRepository,
            OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrdersResponse create(OrdersCreateRequest request) {
        // 요청의 orderLinItem 내용이 없는지 확인
        List<OrderLineItemDto> orderLineItemDtos = request.getOrderLineItems();
        if (CollectionUtils.isEmpty(orderLineItemDtos)) {
            throw new IllegalArgumentException();
        }

        // orderTableId가 실제로 존재하는지 확인
        OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        // orderTable이 isEmpty()상태라면 예외
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        // order entity를 생성
        Orders orders = orderRepository.save(
                new Orders(orderTable, OrderStatus.COOKING.name(), LocalDateTime.now()));

        for (OrderLineItemDto orderLineItemDto : orderLineItemDtos) {
            orderLineItemRepository.save(new OrderLineItem(orders,
                    menuRepository.findById(orderLineItemDto.getMenuId())
                            .orElseThrow(IllegalArgumentException::new),
                    orderLineItemDto.getQuantity()));
        }

        return OrdersResponse.from(orders);
    }

    public List<OrdersResponse> list() {
        List<Orders> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrdersResponse::from)
                .collect(toList());
    }

    @Transactional
    public OrdersResponse changeOrderStatus(Long orderId, OrdersStatusRequest request) {
        Orders orders = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (orders.isStatusNotChangeable()) {
            throw new IllegalArgumentException();
        }

        orders.setOrderStatus(request.getOrderStatus());

        return OrdersResponse.from(orders);
    }
}
