package kitchenpos.repository;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.fixture.DomainFixture.createOrder;
import static kitchenpos.fixture.DomainFixture.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        orderTable1 = orderTableRepository.save(createOrderTable());
        orderTable2 = orderTableRepository.save(createOrderTable());
        orderRepository.save(createOrder(orderTable1)).changeOrderStatus(MEAL);
        orderRepository.save(createOrder(orderTable2));
    }

    @Test
    void 주문테이블_아이디_목록과_주문상태_목록에_해당하는_요소가_있는지_확인한다() {
        assertAll(
                () -> assertThat(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                        List.of(orderTable1.getId(), orderTable2.getId()), List.of(COOKING))).isTrue(),
                () -> assertThat(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                        List.of(orderTable1.getId()), List.of(COOKING))).isFalse(),
                () -> assertThat(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                        List.of(orderTable1.getId()), List.of(COOKING, MEAL))).isTrue()
        );
    }

    @Test
    void 주문테이블_아이디와_주문상태_목록에_해당하는_요소가_있는지_확인한다() {
        assertAll(
                () -> assertThat(orderRepository.existsByOrderTableIdAndOrderStatusIn(
                        orderTable1.getId(), List.of(COOKING))).isFalse(),
                () -> assertThat(orderRepository.existsByOrderTableIdAndOrderStatusIn(
                        orderTable1.getId(), List.of(MEAL))).isTrue(),
                () -> assertThat(orderRepository.existsByOrderTableIdAndOrderStatusIn(
                        orderTable1.getId(), List.of(COOKING, MEAL))).isTrue()
        );
    }
}
