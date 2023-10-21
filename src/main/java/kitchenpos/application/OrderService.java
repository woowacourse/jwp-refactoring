package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.ChangeOrderStatusCommand;
import kitchenpos.application.dto.CreateOrderCommand;
import kitchenpos.application.dto.domain.OrderDto;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderTableRepository orderTableRepository,
            final OrderRepository orderRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderDto create(final CreateOrderCommand command) {
        if (noOrderLineItems(command)) {
            throw new IllegalArgumentException("주문항목은 최소 한개 이상이어야 합니다.");
        }
        if (invalidOrderLineItems(command)) {
            throw new IllegalArgumentException("주문항목은 각각 다른 메뉴이며 존재해야합니다.");
        }

        final OrderTable orderTable = orderTableRepository.getById(command.getOrderTableId());
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("테이블이 주문 가능한 테이블이어야 합니다.");
        }

        Order order = new Order(null, orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(),
                command.getOrderLineItems());
        return OrderDto.from(orderRepository.save(order));
    }

    private boolean invalidOrderLineItems(final CreateOrderCommand command) {
        return command.getOrderLineItemRequests().size() != menuRepository.countByIdIn(command.getMenuIds());
    }

    private boolean noOrderLineItems(final CreateOrderCommand command) {
        return CollectionUtils.isEmpty(command.getOrderLineItemRequests());
    }

    public List<OrderDto> list() {
        return orderRepository.findAll().stream()
                .map(OrderDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDto changeOrderStatus(final ChangeOrderStatusCommand command) {
        final Long orderId = command.getOrderId();
        final Order order = orderRepository.getById(orderId);

        final OrderStatus orderStatus = OrderStatus.valueOf(command.getOrderStatus());
        order.changeOrderStatus(orderStatus);
        return OrderDto.from(order);
    }

}
