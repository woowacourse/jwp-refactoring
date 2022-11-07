package kitchenpos.application.order;

import java.util.ArrayList;
import kitchenpos.application.order.dto.request.CreateOrderDto;
import kitchenpos.application.order.dto.request.CreateOrderLineItemDto;
import kitchenpos.application.order.dto.response.OrderDto;
import kitchenpos.application.order.dto.request.UpdateOrderStatusDto;
import kitchenpos.common.domain.menu.Menu;
import kitchenpos.common.domain.menu.MenuHistory;
import kitchenpos.common.repository.menu.MenuHistoryRepository;
import kitchenpos.common.repository.menu.MenuRepository;
import kitchenpos.common.domain.order.Order;
import kitchenpos.common.domain.order.OrderLineItem;
import kitchenpos.common.repository.order.OrderLineItemRepository;
import kitchenpos.common.repository.order.OrderRepository;
import kitchenpos.common.repository.table.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final MenuRepository menuRepository;
    private final MenuHistoryRepository menuHistoryRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository,
                        MenuHistoryRepository menuHistoryRepository,
                        OrderRepository orderRepository,
                        OrderLineItemRepository orderLineItemRepository,
                        OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.menuHistoryRepository = menuHistoryRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional(readOnly = true)
    public List<OrderDto> list() {
        return orderRepository.findAll()
                .stream()
                .map(it -> OrderDto.of(it, orderLineItemRepository.findAllByOrderId(it.getId())))
                .collect(Collectors.toList());
    }

    public OrderDto create(final CreateOrderDto createOrderDto) {
        validateMenus(createOrderDto);
        final Order order = orderRepository.save(Order.of(orderTableRepository.get(createOrderDto.getOrderTableId())));
        final List<OrderLineItem> orderLineItems = saveOrderLineItems(createOrderDto, order);
        return OrderDto.of(order, orderLineItems);
    }

    private void validateMenus(CreateOrderDto createOrderDto) {
        final List<Long> menuIds = createOrderDto.getOrderLineItems()
                .stream()
                .map(CreateOrderLineItemDto::getMenuId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(menuIds) || menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("주문할 수 없는 메뉴 정보가 포함되어있습니다.");
        }
    }

    private List<OrderLineItem> saveOrderLineItems(CreateOrderDto createOrderDto, Order order) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (CreateOrderLineItemDto orderLineDto : createOrderDto.getOrderLineItems()) {
            Menu menu = menuRepository.get(orderLineDto.getMenuId());
            MenuHistory menuHistory = menuHistoryRepository.findMostRecentByMenu(menu);
            OrderLineItem orderLineItem = new OrderLineItem(order.getId(), menuHistory, orderLineDto.getQuantity());
            orderLineItems.add(orderLineItemRepository.save(orderLineItem));
        }
        return orderLineItems;
    }

    public OrderDto changeOrderStatus(final UpdateOrderStatusDto updateOrderStatusDto) {
        final Long orderId = updateOrderStatusDto.getOrderId();
        Order order = orderRepository.get(orderId);
        order.changeOrderStatus(updateOrderStatusDto.getOrderStatus());
        order = orderRepository.save(order);
        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(orderId);
        return OrderDto.of(order, orderLineItems);
    }
}
