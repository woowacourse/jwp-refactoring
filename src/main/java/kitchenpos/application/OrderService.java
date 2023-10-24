package kitchenpos.application;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.KitchenPosException;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.dto.OrderCreateRequest;
import kitchenpos.ui.dto.OrderLineCreateRequest;
import kitchenpos.ui.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
        OrderRepository orderRepository,
        MenuRepository menuRepository,
        OrderTableRepository orderTableRepository
    ) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderResponse create(OrderCreateRequest request) {
        OrderTable orderTable = findOrderTable(request.getOrderTableId());
        List<OrderLineCreateRequest> orderLineCreateRequests = request.getOrderLineCreateRequests();
        validateOrderLineRequests(orderLineCreateRequests);
        Order order = orderRepository.save(new Order(null, OrderStatus.COOKING, LocalDateTime.now(), orderTable));
        List<OrderLineItem> orderLineItems = createOrderLineItems(orderLineCreateRequests);
        order.addOrderLineItems(orderLineItems);
        return OrderResponse.from(order);
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new KitchenPosException("해당 주문 테이블이 없습니다. orderTableId=" + orderTableId));
    }

    private void validateOrderLineRequests(List<OrderLineCreateRequest> requests) {
        if (CollectionUtils.isEmpty(requests)) {
            throw new KitchenPosException("주문 목록 항목은 비어있을 수 없습니다.");
        }
    }

    private List<OrderLineItem> createOrderLineItems(List<OrderLineCreateRequest> requests) {
        List<Menu> menus = findMenus(requests);
        validateNotExistsMenus(menus, requests);
        Map<Long, Long> menuIdToQuantity = requests.stream()
            .collect(toMap(OrderLineCreateRequest::getMenuId, OrderLineCreateRequest::getQuantity));
        return menus.stream()
            .map(menu -> new OrderLineItem(null, menuIdToQuantity.get(menu.getId()), menu.getId()))
            .collect(toList());
    }

    private List<Menu> findMenus(List<OrderLineCreateRequest> requests) {
        return requests.stream()
            .map(OrderLineCreateRequest::getMenuId)
            .collect(collectingAndThen(toList(), menuRepository::findByIdIn));
    }

    private void validateNotExistsMenus(List<Menu> menus, List<OrderLineCreateRequest> requests) {
        if (menus.size() == requests.size()) {
            return;
        }
        Set<Long> requestMenuIds = getNotExistMenuIds(menus, requests);
        throw new KitchenPosException("존재하지 않는 메뉴가 있습니다. notExistMenuIds=" + requestMenuIds);
    }

    private Set<Long> getNotExistMenuIds(List<Menu> menus, List<OrderLineCreateRequest> requests) {
        Set<Long> existMenuIds = menus.stream()
            .map(Menu::getId)
            .collect(toSet());
        Set<Long> requestMenuIds = requests.stream()
            .map(OrderLineCreateRequest::getMenuId)
            .collect(toSet());
        requestMenuIds.removeAll(existMenuIds);
        return requestMenuIds;
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream()
            .map(OrderResponse::from)
            .collect(toList());
    }

    public OrderResponse changeOrderStatus(Long orderId, OrderStatus orderStatus) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new KitchenPosException("해당 주문이 없습니다. orderId=" + orderId));
        order.changeStatus(orderStatus);
        return OrderResponse.from(order);
    }
}
