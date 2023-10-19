package kitchenpos.application;

import kitchenpos.dao.OrderRepository;
import kitchenpos.domain.Order;
import kitchenpos.ui.dto.OrderCreateRequest;
import kitchenpos.ui.dto.OrderResponse;
import kitchenpos.ui.dto.OrderUpdateRequest;
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
        orderTableService.validateNotEmptyById(request.getOrderTableId());

        final Order order = orderRepository.save(request.toEntity());
        return OrderResponse.from(order);
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
