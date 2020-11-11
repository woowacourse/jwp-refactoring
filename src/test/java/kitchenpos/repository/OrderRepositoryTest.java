package kitchenpos.repository;

import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.DomainFactory.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @DisplayName("OrderTable들 중 OrderStatus에 해당하는 Order가 있는지 판단")
    @Test
    void existsByOrderTableIdInAndOrderStatusInTest() {
        final OrderTable firstOrderTable = orderTableRepository.save(createOrderTable(1, false, null));
        final OrderTable secondOrderTable = orderTableRepository.save(createOrderTable(1, false, null));
        final OrderTable thirdOrderTable = orderTableRepository.save(createOrderTable(1, false, null));
        final List<Long> orderTableIds = Arrays.asList(
                firstOrderTable.getId(), secondOrderTable.getId(), thirdOrderTable.getId()
        );

        boolean expect = orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)
        );

        assertThat(expect).isFalse();
    }

    @DisplayName("OrderTable에 OrderStatus에 맞는 Order가 있는지 판단")
    @Test
    void existsByOrderTableIdAndOrderStatusInTest() {
        final OrderTable orderTable = orderTableRepository.save(createOrderTable(1, false, null));

        boolean expect = orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getId(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));

        assertThat(expect).isFalse();
    }
}
