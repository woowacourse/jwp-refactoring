package kitchenpos.ordercrud.service;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderCrudService {

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderCrudService(
        OrderRepository orderRepository,
        OrderValidator orderValidator
    ) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public Order create(final Order order) {
        orderValidator.validate(order);

        return orderRepository.save(order);
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }
}
