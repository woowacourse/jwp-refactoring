package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.exception.NotFoundMenuException;
import kitchenpos.exception.OrderMenusCountException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.ui.dto.OrderLineItemDto;
import kitchenpos.ui.dto.request.ChangeOrderStatusRequest;
import kitchenpos.ui.dto.request.OrderCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuDao menuDao;

    public OrderService(OrderRepository orderRepository, MenuDao menuDao) {
        this.orderRepository = orderRepository;
        this.menuDao = menuDao;
    }

    @Transactional
    public Order create(OrderCreateRequest orderCreateRequest) {
        Order order = new Order(orderCreateRequest.getOrderTableId(), OrderStatus.COOKING, LocalDateTime.now());
        List<OrderLineItem> savedOrderLineItems = getOrderLineItems(orderCreateRequest.getOrderLineItems());

        return orderRepository.save(order, savedOrderLineItems);
    }

    private List<OrderLineItem> getOrderLineItems(List<OrderLineItemDto> orderLineItemDtos) {
        validateOrderLineItems(orderLineItemDtos);
        List<OrderLineItem> orderLineItems = new ArrayList<>();

        for (OrderLineItemDto orderLineItemDto : orderLineItemDtos) {
            menuDao.findById(orderLineItemDto.getMenuId())
                    .orElseThrow(NotFoundMenuException::new);
            orderLineItems.add(new OrderLineItem(orderLineItemDto.getMenuId(), orderLineItemDto.getQuantity()));
        }

        return orderLineItems;
    }

    private static void validateOrderLineItems(List<OrderLineItemDto> orderLineItemDtos) {
        if (CollectionUtils.isEmpty(orderLineItemDtos)) {
            throw new OrderMenusCountException();
        }
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(Long orderId, ChangeOrderStatusRequest changeOrderStatusRequest) {
        return orderRepository.changeOrderStatus(orderId,
                OrderStatus.valueOf(changeOrderStatusRequest.getOrderStatus()));
    }
}
