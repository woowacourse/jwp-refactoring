package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.vo.OrderStatus;
import kitchenpos.dto.order.request.OrderCreateRequest;
import kitchenpos.dto.order.request.OrderLineItemCreateRequest;
import kitchenpos.dto.order.response.OrderResponse;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.domain.ordertable.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final MenuProductRepository menuProductRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderLineItemRepository orderLineItemRepository;


    public OrderService(
            final MenuRepository menuRepository,
            final MenuProductRepository menuProductRepository,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository,
            final OrderLineItemRepository orderLineItemRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuProductRepository = menuProductRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderLineItemRepository = orderLineItemRepository;
    }

    @Transactional
    public Long create(final OrderCreateRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(request.orderTableId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "[ERROR] OrderTable이 존재하지 않습니다. id : " + request.orderTableId()
                ));

        final Order order = new Order(
                OrderStatus.COOKING,
                LocalDateTime.now()
        );
        order.setOrderTable(orderTable);
        final Order savedOrder = orderRepository.save(order);

        final List<OrderLineItem> orderLineItems = createOrderLineItems(request);
        orderLineItems.forEach(orderLineItem -> {
            orderLineItem.setOrder(savedOrder);
            orderLineItemRepository.save(orderLineItem);
        });

        return savedOrder.id();
    }

    private List<OrderLineItem> createOrderLineItems(final OrderCreateRequest request) {
        final List<OrderLineItemCreateRequest> orderLineItemCreateRequests = request.orderLineItems();

        if (orderLineItemCreateRequests.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] OrderLineItem이 비어있습니다.");
        }

        final List<Long> menuIds = orderLineItemCreateRequests.stream()
                .map(OrderLineItemCreateRequest::menuId)
                .collect(Collectors.toUnmodifiableList());

        if (menuRepository.anyIdNotExists(menuIds)) {
            throw new IllegalArgumentException("[ERROR] 없는 메뉴가 존재합니다.");
        }

        final List<Long> quantities = orderLineItemCreateRequests.stream()
                .map(OrderLineItemCreateRequest::quantity)
                .collect(Collectors.toUnmodifiableList());

        return IntStream.range(0, menuIds.size())
                .mapToObj(index -> new OrderLineItem(menuIds.get(index), quantities.get(index)))
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::getOrderResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus requestOrderStatus) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] Order가 존재하지 않습니다. id : " + orderId));
        savedOrder.changeOrderStatus(requestOrderStatus);

        return getOrderResponse(savedOrder);
    }

    private OrderResponse getOrderResponse(final Order order) {
        final List<OrderLineItem> orderLineItems = orderLineItemRepository.findByOrderId(order.id());
        final List<Menu> menus = orderLineItems.stream()
                .map(OrderLineItem::menuId)
                .map(menuRepository::findById)
                .map(menu -> menu.orElseThrow(() -> new IllegalArgumentException("[ERROR] 메뉴가 존재하지 않습니다.")))
                .collect(Collectors.toUnmodifiableList());

        return OrderResponse.of(order, orderLineItems, menus, parseMenuProducts(menus));
    }

    private List<List<MenuProduct>> parseMenuProducts(final List<Menu> menus) {
        return menus.stream()
                .map(Menu::id)
                .map(menuProductRepository::findAllByMenuId)
                .collect(Collectors.toUnmodifiableList());
    }
}
