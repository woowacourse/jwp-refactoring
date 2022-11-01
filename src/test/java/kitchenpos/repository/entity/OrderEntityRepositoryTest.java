package kitchenpos.repository.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.OrderFixtures;
import kitchenpos.domain.Order;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.entity.OrderEntityRepository;
import kitchenpos.repository.entity.OrderEntityRepositoryImpl;
import kitchenpos.support.RepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class OrderEntityRepositoryTest {

    private final OrderRepository orderRepository;
    private final OrderEntityRepository orderEntityRepository;

    @Autowired
    public OrderEntityRepositoryTest(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.orderEntityRepository = new OrderEntityRepositoryImpl(orderRepository);
    }

    @Test
    void getById() {
        // given
        Order order = orderRepository.save(OrderFixtures.createOrder());
        // when
        Order foundOrder = orderEntityRepository.getById(order.getId());
        // then
        assertThat(order).isSameAs(foundOrder);
    }

    @Test
    void getByIdWithInvalidId() {
        // given
        Long invalidId = 999L;
        // when & then
        assertThatThrownBy(() -> orderEntityRepository.getById(invalidId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
