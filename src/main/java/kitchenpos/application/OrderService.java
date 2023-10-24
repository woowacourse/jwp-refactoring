package kitchenpos.application;

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
        validateMenus(command);

        final OrderTable orderTable = orderTableRepository.getById(command.getOrderTableId());
        Order order = orderTable.order(command.getOrderLineItems());

        return OrderDto.from(orderRepository.save(order));
    }

    private void validateMenus(final CreateOrderCommand command) {
        List<Long> menuIds = command.getOrderLineItemRequests().stream()
                .map(request -> request.getMenuId())
                .collect(Collectors.toList());

        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("메뉴가 존재하지 않습니다.");
        }
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
