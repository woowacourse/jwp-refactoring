package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.order.OrderedMenu;
import kitchenpos.domain.order.OrderedMenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.repository.MenuGroupRepository;
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
import kitchenpos.repository.OrderedMenuProductRepository;
import kitchenpos.repository.OrderedMenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderedMenuRepository orderedMenuRepository;
    private final OrderedMenuProductRepository orderedMenuProductRepository;


    public OrderService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderedMenuRepository orderedMenuRepository,
            final OrderedMenuProductRepository orderedMenuProductRepository
            ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderedMenuRepository = orderedMenuRepository;
        this.orderedMenuProductRepository = orderedMenuProductRepository;
    }

    @Transactional
    public Long create(final OrderCreateRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(request.orderTableId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "[ERROR] OrderTable이 존재하지 않습니다. id : " + request.orderTableId()
                ));

        final Order order = new Order(OrderStatus.COOKING, LocalDateTime.now());
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
        final List<Menu> savedMenus = menuRepository.findAllById(menuIds);
        if (menuIds.size() != savedMenus.size()) {
            throw new IllegalArgumentException("[ERROR] 없는 메뉴가 존재합니다.");
        }

        final List<OrderedMenu> savedOrderedMenus = getSavedOrderedMenus(savedMenus);
        final List<MenuProduct> menuProducts = menuProductRepository.findAllByMenuIdIn(menuIds);
        saveOrderedMenuProducts(menuProducts, savedOrderedMenus);

        final List<Long> quantities = orderLineItemCreateRequests.stream()
                .map(OrderLineItemCreateRequest::quantity)
                .collect(Collectors.toUnmodifiableList());

        return IntStream.range(0, savedOrderedMenus.size())
                .mapToObj(index -> new OrderLineItem(savedOrderedMenus.get(index), quantities.get(index)))
                .collect(Collectors.toUnmodifiableList());
    }

    private List<OrderedMenu> getSavedOrderedMenus(final List<Menu> savedMenus) {
        return savedMenus.stream()
                .map(menu -> {
                    final MenuGroup menuGroup = menuGroupRepository.getMenuGroupById(menu.menuGroup().id());
                    final OrderedMenu orderedMenu = new OrderedMenu(menu.name(), menu.price(), menuGroup.name());
                    return orderedMenuRepository.save(orderedMenu);
                })
                .collect(Collectors.toUnmodifiableList());
    }

    private void saveOrderedMenuProducts(final List<MenuProduct> menuProducts, final List<OrderedMenu> orderedMenus) {
        IntStream.range(0, menuProducts.size())
                .mapToObj(index -> {
                    final MenuProduct menuProduct = menuProducts.get(index);
                    final Product product = menuProduct.product();
                    return new OrderedMenuProduct(
                            orderedMenus.get(index),
                            product.name(),
                            product.price(),
                            menuProduct.quantity()
                    );
                })
                .forEach(orderedMenuProductRepository::save);
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
        final List<OrderedMenu> orderedMenus = orderLineItems.stream()
                .map(OrderLineItem::orderedMenu)
                .collect(Collectors.toUnmodifiableList());

        return OrderResponse.of(order, orderLineItems, orderedMenus, parseMenuProducts(orderedMenus));
    }

    private List<List<OrderedMenuProduct>> parseMenuProducts(final List<OrderedMenu> orderedMenus) {
        return orderedMenus.stream()
                .map(OrderedMenu::id)
                .map(orderedMenuProductRepository::findAllByOrderedMenuId)
                .collect(Collectors.toUnmodifiableList());
    }
}
