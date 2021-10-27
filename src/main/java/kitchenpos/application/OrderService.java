package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderLineItemRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemCreateRequest;
import kitchenpos.dto.request.OrderStatusChangeRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.exception.KitchenException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
        final MenuRepository menuRepository,
        final OrderRepository orderRepository,
        final OrderLineItemRepository orderLineItemRepository,
        final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
            .orElseThrow(() -> new KitchenException("존재하지 않는 테이블에 배정되어 있습니다."));

        List<Menu> menus = menuRepository.findAllByIdIn(request.getMenuIds());
        List<OrderLineItem> orderLineItemList = convertToOrderLineItem(request, menus);

        Order savedOrder = orderRepository
            .save(new Order(orderTable, LocalDateTime.now()));
        OrderLineItems orderLineItems = new OrderLineItems(orderLineItemList, savedOrder);
        orderLineItemRepository.saveAll(orderLineItems.getOrderLineItems());
        return OrderResponse.of(savedOrder);
    }

    private List<OrderLineItem> convertToOrderLineItem(OrderCreateRequest request,
        List<Menu> menus) {
        List<OrderLineItemCreateRequest> orderLineItems = request.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new KitchenException("주문 항목이 비어있습니다.");
        }

        return orderLineItems.stream()
            .map(orderLineItemCreateRequest -> {
                Menu menu = findMenu(menus, orderLineItemCreateRequest.getMenuId());
                Long quantity = orderLineItemCreateRequest.getQuantity();
                return new OrderLineItem(menu, quantity);
            }).collect(Collectors.toList());
    }

    private Menu findMenu(List<Menu> menus, Long menuId) {
        return menus.stream()
            .filter(menu -> menuId.equals(menu.getId()))
            .findAny()
            .orElseThrow(() -> new KitchenException("존재하지 않는 메뉴가 포함되어 있습니다."));
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
            .map(OrderResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId,
        final OrderStatusChangeRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(() -> new KitchenException("존재하지 않는 주문입니다."));
        savedOrder.changeOrderStatus(request.getOrderStatus());
        return OrderResponse.of(savedOrder);
    }
}
