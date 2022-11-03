package kitchenpos.repository.entity;

import kitchenpos.domain.order.Order;
import kitchenpos.repository.OrderRepository;
import org.springframework.context.annotation.Lazy;

public class OrderEntityRepositoryImpl implements OrderEntityRepository {

    private final OrderRepository orderRepository;

    @Lazy
    public OrderEntityRepositoryImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다 : " + id));
    }
}
