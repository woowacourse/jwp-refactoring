package kitchenpos.dao;

import static kitchenpos.common.fixture.OrderFixture.주문;
import static kitchenpos.common.fixture.OrderTableFixture.주문_테이블;
import static kitchenpos.common.fixture.TableGroupFixture.단체_지정;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import javax.sql.DataSource;
import kitchenpos.common.DaoTest;
import kitchenpos.domain.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DaoTest
class JdbcTemplateOrderDaoTest {

    @Autowired
    private DataSource dataSource;

    private JdbcTemplateOrderDao jdbcTemplateOrderDao;
    private Long orderTableId;

    @BeforeEach
    void setUp() {
        jdbcTemplateOrderDao = new JdbcTemplateOrderDao(dataSource);

        JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao = new JdbcTemplateTableGroupDao(dataSource);
        Long tableGroupId = jdbcTemplateTableGroupDao.save(단체_지정()).getId();

        JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao = new JdbcTemplateOrderTableDao(dataSource);
        orderTableId = jdbcTemplateOrderTableDao.save(주문_테이블(tableGroupId)).getId();
    }

    @Test
    void 주문을_저장한다() {
        // given
        Order order = 주문(orderTableId);

        // when
        Order savedOrder = jdbcTemplateOrderDao.save(order);

        // then
        assertSoftly(softly -> {
            softly.assertThat(savedOrder.getId()).isNotNull();
            softly.assertThat(savedOrder).usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(주문(orderTableId));
        });
    }

    @Test
    void ID로_주문을_조회한다() {
        // given
        Long orderId = jdbcTemplateOrderDao.save(주문(orderTableId)).getId();

        // when
        Order order = jdbcTemplateOrderDao.findById(orderId).get();

        // then
        assertThat(order).usingRecursiveComparison()
                .isEqualTo(주문(orderId, orderTableId));
    }

    @Test
    void 전체_주문을_조회한다() {
        // given
        Long orderId_A = jdbcTemplateOrderDao.save(주문(orderTableId)).getId();
        Long orderId_B = jdbcTemplateOrderDao.save(주문(orderTableId)).getId();

        // when
        List<Order> orders = jdbcTemplateOrderDao.findAll();

        // then
        assertThat(orders).usingRecursiveComparison()
                .isEqualTo(List.of(주문(orderId_A, orderTableId), 주문(orderId_B, orderTableId)));
    }

    @Nested
    class 주문_테이블_ID와_주문_상태_목록을_입력해_주문이_있는지_확인할_때 {

        @Test
        void 주문이_있으면_true를_반환한다() {
            // given
            jdbcTemplateOrderDao.save(주문(orderTableId, MEAL));

            // when
            boolean actual = jdbcTemplateOrderDao.existsByOrderTableIdAndOrderStatusIn(
                    orderTableId,
                    List.of(MEAL.name())
            );

            // then
            assertThat(actual).isTrue();
        }

        @Test
        void 주문이_없으면_false를_반환한다() {
            // given
            jdbcTemplateOrderDao.save(주문(orderTableId, MEAL));

            // when
            boolean actual = jdbcTemplateOrderDao.existsByOrderTableIdAndOrderStatusIn(
                    orderTableId,
                    List.of(COOKING.name())
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
            jdbcTemplateOrderDao.save(주문(orderTableId, MEAL));

            // when
            boolean actual = jdbcTemplateOrderDao.existsByOrderTableIdInAndOrderStatusIn(
                    List.of(orderTableId),
                    List.of(MEAL.name())
            );

            // then
            assertThat(actual).isTrue();
        }

        @Test
        void 주문이_있으면_false를_반환한다() {
            // given
            jdbcTemplateOrderDao.save(주문(orderTableId, MEAL));

            // when
            boolean actual = jdbcTemplateOrderDao.existsByOrderTableIdInAndOrderStatusIn(
                    List.of(orderTableId),
                    List.of(COOKING.name())
            );

            // then
            assertThat(actual).isFalse();
        }
    }
}
