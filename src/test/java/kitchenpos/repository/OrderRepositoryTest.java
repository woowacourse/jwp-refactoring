package kitchenpos.repository;

import static kitchenpos.common.fixture.OrderFixture.주문;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
//@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    private Long orderTableId;

    @BeforeEach
    void setUp() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 1, true));
        this.orderTableId = orderTable.getId();
    }

    @Nested
    class 주문_테이블_ID와_주문_상태_목록을_입력해_주문이_있는지_확인할_때 {

        @Test
        void 주문이_있으면_true를_반환한다() {
            // given
            orderRepository.save(주문(orderTableId, OrderStatus.COOKING));

            // when
            boolean actual = orderRepository.existsByOrderTableIdAndOrderStatusIn(
                    orderTableId,
                    List.of(COOKING)
            );

            // then
            assertThat(actual).isTrue();
        }

        @Test
        void 주문이_없으면_false를_반환한다() {
            // given
            orderRepository.save(주문(orderTableId, OrderStatus.COOKING));

            // when
            boolean actual = orderRepository.existsByOrderTableIdAndOrderStatusIn(
                    orderTableId,
                    List.of(MEAL)
            );

            // then
            assertThat(actual).isFalse();
        }
    }

    @Nested
    class 주문_테이블_ID_목록과_주문_상태_목록을_입력해_주문이_있는지_확인할_때 {

        @Test
        void 주문이_있으면_true를_반환한다() {
            // given
            orderRepository.save(주문(orderTableId, OrderStatus.COOKING));

            // when
            boolean actual = orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                    List.of(orderTableId),
                    List.of(COOKING)
            );

            // then
            assertThat(actual).isTrue();
        }

        @Test
        void 주문이_없으면_false를_반환한다() {
            // given
            orderRepository.save(주문(orderTableId, OrderStatus.COOKING));

            // when
            boolean actual = orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                    List.of(orderTableId),
                    List.of(MEAL)
            );

            // then
            assertThat(actual).isFalse();
        }
    }
}
