package kitchenpos.application;

import kitchenpos.application.dto.request.CreateOrderDto;
import kitchenpos.application.dto.request.CreateOrderLineItemDto;
import kitchenpos.application.dto.response.OrderDto;
import kitchenpos.application.dto.request.UpdateOrderStatusDto;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.repository.OrderLineItemRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.table.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository,
                        OrderRepository orderRepository,
                        OrderLineItemRepository orderLineItemRepository,
                        OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderDto create(final CreateOrderDto createOrderDto) {
        validateMenus(createOrderDto);
        final Order order = orderRepository.save(Order.of(findOrderTable(createOrderDto.getOrderTableId())));
        return OrderDto.of(order, createOrderDto.getOrderLineItems()
                .stream()
                .map(it -> new OrderLineItem(order.getId(), it.getMenuId(), it.getQuantity()))
                .map(orderLineItemRepository::save)
                .collect(Collectors.toList()));
    }

    private void validateMenus(CreateOrderDto createOrderDto) {
        final List<Long> menuIds = createOrderDto.getOrderLineItems()
                .stream()
                .map(CreateOrderLineItemDto::getMenuId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(menuIds) || menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<OrderDto> list() {
        return orderRepository.findAll()
                .stream()
                .map(it -> OrderDto.of(it, orderLineItemRepository.findAllByOrderId(it.getId())))
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDto changeOrderStatus(final UpdateOrderStatusDto updateOrderStatusDto) {
        final Long orderId = updateOrderStatusDto.getOrderId();
        Order order = findOrder(orderId);
        order.changeOrderStatus(updateOrderStatusDto.getOrderStatus());
        order = orderRepository.save(order);
        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(orderId);
        return OrderDto.of(order, orderLineItems);
    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
