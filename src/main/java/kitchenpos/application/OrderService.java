package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.dto.request.CreateOrderRequest;
import kitchenpos.dto.request.OrderLineItemDto;
import kitchenpos.dto.request.PutOrderStatusRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(final CreateOrderRequest orderRequest) {
        final Long orderTableId = orderRequest.getOrderTableId();
        if (Objects.isNull(orderTableId) || !orderTableRepository.existsById(orderTableId)) {
            throw new IllegalArgumentException();
        }
        final Order order = new Order(orderTableId, OrderStatus.COOKING);
        final Order savedOrder = orderRepository.save(order);

        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        final List<Menu> menus = new ArrayList<>();
        for (final OrderLineItemDto orderLineItemDto : orderRequest.getOrderLineItems()) {
            final Menu menu = menuRepository.findById(orderLineItemDto.getMenuId())
                                            .orElseThrow(() -> new IllegalArgumentException("잘못된 메뉴입니다."));
            final OrderLineItem orderLineItem = new OrderLineItem(menu, orderLineItemDto.getQuantity());
            orderLineItems.add(orderLineItem);
            menus.add(menu);
        }
        savedOrder.setOrderLineItems(orderLineItems);
        validateMenuIds(orderLineItems.size(), menus);

        return savedOrder;
    }

    private void validateMenuIds(final int orderLineItemSize, final List<Menu> menus) {
        final List<Long> menuIds = menus.stream()
                                        .map(Menu::getId)
                                        .collect(Collectors.toUnmodifiableList());
        if (orderLineItemSize != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final PutOrderStatusRequest orderStatusRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                                                .orElseThrow(IllegalArgumentException::new);
        savedOrder.changeOrderStatus(orderStatusRequest.getOrderStatus());
        return savedOrder;
    }
}
