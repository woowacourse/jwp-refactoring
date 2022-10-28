package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.dto.request.ChangeOrderStatusRequest;
import kitchenpos.ui.dto.request.OrderCreateRequest;
import kitchenpos.ui.dto.request.OrderLineItemRequest;
import kitchenpos.ui.dto.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository,
                        final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        request.verify();
        final List<Long> menuIds = request.getOrderLineItems()
                .stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        final Order order = Order.ofCooking(orderTable,
                LocalDateTime.now(),
                request.getOrderLineItemEntities());

        return OrderResponse.from(
                orderRepository.save(order)
        );
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest request) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new)
                .changeOrderStatus(request.getOrderStatus());
        return OrderResponse.from(
                orderRepository.save(order)
        );
    }
}
