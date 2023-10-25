package kitchenpos.order.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderUpdateRequest;
import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private final OrderTableService orderTableService;
    private final MenuService menuService;
    private final OrderRepository orderRepository;

    public OrderService(final OrderRepository orderRepository,
                        final OrderTableService orderTableService,
                        final MenuService menuService) {
        this.orderRepository = orderRepository;
        this.orderTableService = orderTableService;
        this.menuService = menuService;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        final OrderTable orderTable = orderTableService.findByIdOrThrow(request.getOrderTableId());

        menuService.validateExistenceByIds(request.getMenuIds());
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 주문 테이블입니다.");
        }

        final Order order = new Order(orderTable.getId(), request.getOrderLineItems());
        return OrderResponse.from(orderRepository.save(order));
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderUpdateRequest request) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        order.changeOrderStatus(request.getOrderStatus());
        return OrderResponse.from(order);
    }
}
