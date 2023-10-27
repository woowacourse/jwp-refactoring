package kitchenpos.order.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.exception.MenuException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderTableLog;
import kitchenpos.order.domain.OrderTableLogRepository;
import kitchenpos.order.domain.OrderedMenu;
import kitchenpos.order.domain.OrderedMenuProduct;
import kitchenpos.order.domain.OrderedMenuProductRepository;
import kitchenpos.order.domain.OrderedMenuRepository;
import kitchenpos.order.dto.request.OrderCreateRequest;
import kitchenpos.order.dto.request.OrderLineItemCreateRequest;
import kitchenpos.order.dto.request.OrderUpdateRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.order.exception.OrderException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderedMenuRepository orderedMenuRepository;
    private final OrderedMenuProductRepository orderedMenuProductRepository;
    private final OrderTableLogRepository orderTableLogRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            MenuRepository menuRepository,
            MenuGroupRepository menuGroupRepository,
            MenuProductRepository menuProductRepository,
            OrderLineItemRepository orderLineItemRepository,
            OrderedMenuRepository orderedMenuRepository,
            OrderedMenuProductRepository orderedMenuProductRepository,
            OrderTableLogRepository orderTableLogRepository,
            OrderRepository orderRepository,
            OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderedMenuRepository = orderedMenuRepository;
        this.orderedMenuProductRepository = orderedMenuProductRepository;
        this.orderTableLogRepository = orderTableLogRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(OrderCreateRequest request) {
        Order order = new Order();
        OrderTableLog orderTableLog = createOrderTableLog(request, order);
        List<OrderLineItem> orderLineItems = createOrderLineItems(request, order);

        orderRepository.save(order);
        orderTableLogRepository.save(orderTableLog);
        orderLineItemRepository.saveAll(orderLineItems);

        return OrderResponse.of(order, orderTableLog, orderLineItems);
    }

    private OrderTableLog createOrderTableLog(OrderCreateRequest request, Order order) {
        OrderTable orderTable = orderTableRepository.getById(request.getOrderTableId());
        if (orderTable.isEmpty()) {
            throw new OrderException("빈 테이블을 등록할 수 없습니다.");
        }
        return new OrderTableLog(order, orderTable.getId(), orderTable.getNumberOfGuests());
    }

    private List<OrderLineItem> createOrderLineItems(OrderCreateRequest orderCreateRequest, Order order) {
        List<Menu> menus = findMenus(orderCreateRequest);
        return saveOrderLineItems(orderCreateRequest, order, menus);
    }

    private List<Menu> findMenus(OrderCreateRequest orderCreateRequest) {
        List<Long> menuIds = orderCreateRequest.getOrderLineItems().stream().map(OrderLineItemCreateRequest::getMenuId)
                .collect(Collectors.toList());
        return menuRepository.findAllById(menuIds);
    }

    private List<OrderLineItem> saveOrderLineItems(
            OrderCreateRequest orderCreateRequest,
            Order order,
            List<Menu> menus
    ) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemCreateRequest request : orderCreateRequest.getOrderLineItems()) {
            Menu targetMenu = findTargetMenu(menus, request);
            OrderedMenu orderedMenu = saveOrderedMenu(targetMenu);
            saveOrderedMenuProducts(targetMenu, orderedMenu);
            long quantity = request.getQuantity();

            OrderLineItem orderLineItem = new OrderLineItem(order, orderedMenu, quantity);
            orderLineItems.add(orderLineItem);
        }

        if (orderLineItems.isEmpty()) {
            throw new OrderException("주문 항목이 없습니다.");
        }

        return orderLineItems;
    }

    private Menu findTargetMenu(List<Menu> menus, OrderLineItemCreateRequest request) {
        return menus.stream()
                .filter(menu -> Objects.equals(menu.getId(), request.getMenuId()))
                .findFirst()
                .orElseThrow(() -> new MenuException("해당하는 메뉴가 존재하지 않습니다."));
    }

    private OrderedMenu saveOrderedMenu(Menu targetMenu) {
        MenuGroup menuGroup = menuGroupRepository.getById(targetMenu.getMenuGroupId());
        String menuGroupName = menuGroup.getName();

        OrderedMenu orderedMenu = new OrderedMenu(targetMenu.getName(), targetMenu.getPrice(), menuGroupName);
        orderedMenuRepository.save(orderedMenu);
        return orderedMenu;
    }

    private void saveOrderedMenuProducts(Menu targetMenu, OrderedMenu orderedMenu) {
        List<MenuProduct> menuProducts = menuProductRepository.findAllByMenu(targetMenu);
        List<OrderedMenuProduct> orderedMenuProducts = menuProducts.stream()
                .map(menuProduct -> new OrderedMenuProduct(
                        orderedMenu,
                        menuProduct.getName(),
                        menuProduct.getPrice(),
                        menuProduct.getQuantity()))
                .collect(Collectors.toList());

        orderedMenuProductRepository.saveAll(orderedMenuProducts);
    }

    public List<OrderResponse> readAll() {
        List<Order> orders = orderRepository.findAll();
        List<OrderTableLog> orderTableLogs = orderTableLogRepository.findAllByOrderIn(orders);
        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderIn(orders);

        return getOrderResponses(orders, orderTableLogs, orderLineItems);
    }

    private List<OrderResponse> getOrderResponses(
            List<Order> orders,
            List<OrderTableLog> orderTableLogs,
            List<OrderLineItem> orderLineItems
    ) {
        return orders.stream()
                .map(order -> OrderResponse.of(
                        order,
                        orderTableLogs.stream()
                                .filter(orderTableLog -> orderTableLog.getOrder().equals(order))
                                .findFirst()
                                .get(),
                        orderLineItems.stream()
                                .filter(orderLineItem -> orderLineItem.getOrder().equals(order))
                                .collect(Collectors.toList()))
                ).collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderUpdateRequest request) {
        Order order = orderRepository.getById(orderId);
        order.changeOrderStatus(request.getOrderStatus());

        OrderTableLog orderTableLog = orderTableLogRepository.findByOrder(order);
        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrder(order);
        return OrderResponse.of(order, orderTableLog, orderLineItems);
    }
}
