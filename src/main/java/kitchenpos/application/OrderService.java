package kitchenpos.application;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderLineItemRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.request.OrderStatusRequest;
import kitchenpos.dto.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository,
                        final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);
        List<OrderLineItem> orderLineItems = newOrderLineItem(orderRequest);

        Order savedOrder = orderRepository.save(new Order(orderTable, orderLineItems));

        return OrderResponse.from(savedOrder);
    }

    private List<OrderLineItem> newOrderLineItem(final OrderRequest orderRequest) {
        if (Objects.isNull(orderRequest.getOrderLineItems())) {
            throw new IllegalArgumentException();
        }

        final Map<Long, Long> menuIdAndQuantity = orderRequest.toMap();
        List<Menu> menus = menuRepository.findAllById(menuIdAndQuantity.keySet());
        validateMenusSize(orderRequest, menus);

        return orderLineItemsFromMenus(menuIdAndQuantity, menus);
    }

    private List<OrderLineItem> orderLineItemsFromMenus(final Map<Long, Long> menuIdAndQuantity,
                                                        final List<Menu> menus) {
        return menus.stream()
            .map(menu -> new OrderLineItem(menu, menuIdAndQuantity.get(menu.getId())))
            .collect(Collectors.toList());
    }

    private void validateMenusSize(final OrderRequest orderRequest, final List<Menu> menus) {
        if (orderRequest.getOrderLineItems().size() != menus.size()) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderResponse> list() {
        return OrderResponse.listFrom(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId,
                                           final OrderStatusRequest orderStatusRequest) {
        Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);
        OrderStatus orderStatus = OrderStatus.valueOf(orderStatusRequest.getOrderStatus());

        savedOrder.changeOrder(orderStatus);
        return OrderResponse.from(savedOrder);
    }
}
