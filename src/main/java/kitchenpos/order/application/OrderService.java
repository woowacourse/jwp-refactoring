package kitchenpos.order.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.ui.dto.OrderCreateRequest;
import kitchenpos.order.ui.dto.OrderResponse;
import kitchenpos.order.ui.dto.OrderUpdateRequest;
import kitchenpos.table.application.OrderTableService;
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
        menuService.validateExistenceByIds(request.getMenuIds());

        final OrderTable orderTable = orderTableService.findByIdOrThrow(request.getOrderTableId());
        final Order order = new Order(orderTable, request.getOrderLineItems());

        final Order saved = orderRepository.save(order);
        return OrderResponse.from(saved);
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
