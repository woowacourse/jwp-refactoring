package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.OrderChangeOrderStatusRequest;
import kitchenpos.application.dto.OrderCreateRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository, final OrderRepository orderRepository,
                        final OrderLineItemRepository orderLineItemRepository,
                        final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(final OrderCreateRequest request) {
        validateAllExistOrderLineItems(request.getOrderLineItemIds());
        final Order order = Order.forSave(OrderStatus.COOKING,
                                          orderLineItemRepository.findAllById(request.getOrderLineItemIds()));

        return orderRepository.save(order);
    }

    private void validateAllExistOrderLineItems(final List<Long> orderLineItemIds) {
        final boolean existsAll = orderLineItemIds.stream()
            .allMatch(menuRepository::existsById);

        if (!existsAll) {
            throw new IllegalArgumentException("존재하지 않는 주문 항목이 포함되어 있습니다.");
        }
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderChangeOrderStatusRequest request) {
        final Order order = orderRepository.getById(orderId);
        order.changeOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));

        return order;
    }
}
