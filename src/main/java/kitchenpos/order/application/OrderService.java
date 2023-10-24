package kitchenpos.order.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.application.dto.OrderStatusChangeRequest;
import kitchenpos.order.domain.MenuProductSnapShot;
import kitchenpos.order.domain.MenuSnapShot;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderService(
            ProductRepository productRepository, MenuRepository menuRepository,
            OrderTableRepository orderTableRepository,
            OrderRepository orderRepository
    ) {
        this.productRepository = productRepository;
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse create(OrderCreateRequest request) {
        List<OrderLineItem> orderLineItems = request.getOrderLineItems().stream()
                .map(it -> {
                    Menu menu = menuRepository.getById(it.getMenuId());
                    List<MenuProductSnapShot> productSnapShots = new ArrayList<>();
                    for (MenuProduct menuProduct : menu.getMenuProducts()) {
                        Product product = productRepository.getById(menuProduct.getProductId());
                        MenuProductSnapShot snapShot = new MenuProductSnapShot(product.getName(), product.getPrice(),
                                menuProduct.getQuantity());
                        productSnapShots.add(snapShot);
                    }
                    MenuSnapShot snapShot = new MenuSnapShot(menu.getMenuGroup().getName(), menu.getName(),
                            menu.getPrice(), productSnapShots);
                    return new OrderLineItem(snapShot, it.getQuantity());
                })
                .collect(Collectors.toList());
        OrderTable orderTable = orderTableRepository.getById(request.getOrderTableId());
        return OrderResponse.from(orderRepository.save(
                new Order(orderTable, orderLineItems)
        ));
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, OrderStatusChangeRequest request) {
        Order savedOrder = orderRepository.getById(orderId);
        OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        savedOrder.setOrderStatus(orderStatus.name());
        orderRepository.save(savedOrder);
        return OrderResponse.from(savedOrder);
    }
}
