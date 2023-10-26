package kitchenpos.application;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineRequest;
import kitchenpos.dto.request.OrderStatusChangeRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository,
                        OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderResponse create(final OrderCreateRequest request) {
        final OrderTable orderTable = findOrderTable(request);
        Order order = orderTable.order(makeOrderLine(request));
        return OrderResponse.of(orderRepository.save(order));
    }

    private OrderTable findOrderTable(OrderCreateRequest request) {
        return orderTableRepository.findById(request.getOrderTableId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블입니다."));
    }

    private List<OrderLineItem> makeOrderLine(OrderCreateRequest request) {
        List<Menu> menus = findMenus(request);
        if (request.getOrderLines().size() != menus.size()) {
            throw new IllegalArgumentException("존재하지 않는 메뉴가 포함되어 있습니다.");
        }
        return request.getOrderLines().stream()
            .map(req -> new OrderLineItem(null, findMenuId(req, menus), null, req.getQuantity()))
            .collect(toList());
    }

    private List<Menu> findMenus(OrderCreateRequest request) {
        return request.getOrderLines().stream()
            .map(OrderLineRequest::getMenuId)
            .collect(collectingAndThen(toList(), menuRepository::findAllByIdIn));
    }

    private Long findMenuId(OrderLineRequest req, List<Menu> menus) {
        return menus.stream()
            .filter(menu -> menu.getId() == req.getMenuId())
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("해당 메뉴가 존재하지 않습니다."))
            .getId();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return OrderResponse.of(orders);
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order order = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        order.changeStatus(OrderStatus.valueOf(request.getOrderStatus()));

        return OrderResponse.of(order);
    }
}
