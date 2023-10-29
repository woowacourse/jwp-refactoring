package kitchenpos.service;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderMenu;
import kitchenpos.domain.OrderMenuProduct;
import kitchenpos.domain.type.OrderStatus;
import kitchenpos.dto.OrderLineItemsDto;
import kitchenpos.dto.request.ChangeOrderRequest;
import kitchenpos.dto.request.CreateOrderRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.exception.EmptyListException;
import kitchenpos.exception.NoSuchDataException;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderMenuProductRepository;
import kitchenpos.repository.OrderMenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.validator.OrderValidator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@ComponentScan(basePackages = "kitchenpos")
@Transactional
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderMenuProductRepository orderMenuProductRepository;
    private final OrderMenuRepository orderMenuRepository;
    private final OrderValidator orderValidator;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderMenuProductRepository orderMenuProductRepository,
            final OrderMenuRepository orderMenuRepository,
            final OrderValidator orderValidator
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderMenuProductRepository = orderMenuProductRepository;
        this.orderMenuRepository = orderMenuRepository;
        this.orderValidator = orderValidator;
    }

    public OrderResponse create(final CreateOrderRequest request) {
        final List<OrderLineItemsDto> orderLineItemDtos = request.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItemDtos)) {
            throw new EmptyListException("아이템이 비어있습니다.");
        }

        final List<Long> menuIds = orderLineItemDtos.stream()
                .map(OrderLineItemsDto::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItemDtos.size() != menuRepository.countByIdIn(menuIds)) {
            throw new NoSuchDataException("입력한 메뉴들이 일치하지 않습니다.");
        }

        orderValidator.validate(request.getOrderTableId());

        final Order order = orderRepository.save(Order.from(request.getOrderTableId()));

        final List<OrderLineItem> orderLineItems = orderLineItemDtos.stream()
                .map(OrderLineItem::from)
                .collect(Collectors.toList());

        orderLineItemRepository.saveAll(orderLineItems);

        createOrderSnapShot(orderLineItemDtos);

        return OrderResponse.from(Order.of(order, orderLineItems));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchDataException("해당하는 id의 주문이 존재하지 않습니다."));

        savedOrder.validateIsComplete();

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());

        savedOrder.changeOrderStatus(orderStatus);

        return OrderResponse.from(savedOrder);
    }

    private void createOrderSnapShot(final List<OrderLineItemsDto> orderLineItemsDtos){
        final List<Long> menuIds = orderLineItemsDtos.stream()
                .map(OrderLineItemsDto::getMenuId)
                .collect(Collectors.toList());
        final List<Menu> menus = menuRepository.findAllById(menuIds);
        for (final OrderLineItemsDto dto : orderLineItemsDtos) {
            final Menu menu = findMenuById(menus, dto.getMenuId());
            final OrderMenu orderMenu = orderMenuRepository.save(
                    new OrderMenu(
                            menu.getName(),
                            menu.getPrice(),
                            menu.getMenuGroupId()
                    )
            );
            orderMenuRepository.save(orderMenu);
            saveAllProduct(menu.getMenuProducts());
            dto.changeToOrderMenuId(orderMenu.getId());
        }
    }

    private Menu findMenuById(final List<Menu> menus, final Long id) {
        return menus.stream()
                .filter(menu -> menu.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchDataException("해당하는 id의 메뉴가 없습니다."));
    }

    private void saveAllProduct(final List<MenuProduct> menuProducts) {
        final List<OrderMenuProduct> orderMenuProducts = menuProducts.stream()
                .map(menuProduct -> new OrderMenuProduct(
                                menuProduct.getMenuId(),
                                menuProduct.getProductId(),
                                menuProduct.getQuantity()
                        )
                )
                .collect(Collectors.toList());
        orderMenuProductRepository.saveAll(orderMenuProducts);
    }
}
