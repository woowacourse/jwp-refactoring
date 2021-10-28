package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderItem;
import kitchenpos.domain.Orders;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderItemRepository;
import kitchenpos.exception.NotFoundException;
import kitchenpos.ui.dto.OrderItemRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderItemService {
    private final MenuRepository menuRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderItemService(MenuRepository menuRepository, OrderItemRepository orderItemRepository) {
        this.menuRepository = menuRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public List<OrderItem> create(List<OrderItemRequest> orderItemRequests, Orders orders) {
        final List<OrderItem> savedOrderItems = new ArrayList<>();
        for (final OrderItemRequest orderItemRequest : orderItemRequests) {
            Menu findMenu = menuRepository.findById(orderItemRequest.getMenuId())
                    .orElseThrow(() -> new NotFoundException("메뉴를 찾을 수 없습니다."));

            OrderItem orderItem = new OrderItem(orders, findMenu, orderItemRequest.getQuantity());
            savedOrderItems.add(orderItemRepository.save(orderItem));
        }
        return savedOrderItems;
    }

    public List<OrderItem> findAllByOrderId(Long orderId) {
        return orderItemRepository.findAllByOrderId(orderId);
    }
}
