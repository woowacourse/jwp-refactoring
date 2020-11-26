package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.Table;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.TableRepository;
import kitchenpos.ui.dto.OrderAssembler;
import kitchenpos.ui.dto.OrderChangeStatusRequest;
import kitchenpos.ui.dto.OrderCreateRequest;
import kitchenpos.ui.dto.OrderLineItemsRequest;
import kitchenpos.ui.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;

    public OrderService(final MenuRepository menuRepository, final OrderRepository orderRepository,
        final TableRepository tableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.tableRepository = tableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest orderCreateRequest) {
        Long tableId = orderCreateRequest.getOrderTableId();
        Table table = tableRepository.findById(tableId)
            .orElseThrow(
                () -> new IllegalArgumentException("ID에 해당하는 Table이 없습니다. {" + tableId + "}"));
        List<Menu> foundMenus = findMenus(orderCreateRequest);

        Order order = OrderAssembler.assemble(orderCreateRequest, table, foundMenus);

        Order savedOrder = orderRepository.save(order);
        return OrderResponse.of(savedOrder);
    }

    private List<Menu> findMenus(OrderCreateRequest orderCreateRequest) {
        List<Long> menuIds = orderCreateRequest.getOrderLineItems()
            .stream()
            .map(OrderLineItemsRequest::getMenuId)
            .collect(Collectors.toList());

        List<Menu> foundMenus = menuRepository.findAllById(menuIds);
        validateMenuIds(menuIds, foundMenus);

        return foundMenus;
    }

    private void validateMenuIds(List<Long> menuIds, List<Menu> foundMenus) {
        if (menuIds.size() != foundMenus.size()) {
            throw new IllegalArgumentException("메뉴 ID 중 일부가 유효하지 않습니다.");
        }
    }

    public List<OrderResponse> list() {
        List<Order> orders = orderRepository.findAll();

        return OrderResponse.listOf(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId,
        OrderChangeStatusRequest orderChangeStatusRequest) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(
                () -> new IllegalArgumentException("ID에 해당하는 Order가 없습니다. {" + orderId + "}"));

        OrderStatus orderStatus = OrderStatus.valueOf(orderChangeStatusRequest.getOrderStatus());
        order.changeOrderStatus(orderStatus);

        return OrderResponse.of(order);
    }
}
