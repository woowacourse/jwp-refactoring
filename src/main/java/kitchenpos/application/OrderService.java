package kitchenpos.application;

import java.util.ArrayList;
import kitchenpos.application.dto.request.CreateOrderDto;
import kitchenpos.application.dto.request.CreateOrderLineItemDto;
import kitchenpos.application.dto.response.OrderDto;
import kitchenpos.application.dto.request.UpdateOrderStatusDto;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuHistory;
import kitchenpos.domain.repository.MenuHistoryRepository;
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

    @Transactional
    public OrderDto create(final CreateOrderDto createOrderDto) {
        validateMenus(createOrderDto);
        final Order order = orderRepository.save(Order.of(findOrderTable(createOrderDto.getOrderTableId())));
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

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<OrderLineItem> saveOrderLineItems(CreateOrderDto createOrderDto, Order order) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (CreateOrderLineItemDto orderLineDto : createOrderDto.getOrderLineItems()) {
            MenuHistory menuHistory = findCurrentMenuHistory(orderLineDto.getMenuId());
            OrderLineItem orderLineItem = new OrderLineItem(order.getId(), menuHistory, orderLineDto.getQuantity());
            orderLineItems.add(orderLineItemRepository.save(orderLineItem));
        }
        return orderLineItems;
    }

    private MenuHistory findCurrentMenuHistory(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));
        return menuHistoryRepository.findFirstByMenuOrderByCreatedTimeDesc(menu)
                .orElseThrow(() -> new IllegalArgumentException("메뉴 정보가 존재하지 않습니다."));
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
