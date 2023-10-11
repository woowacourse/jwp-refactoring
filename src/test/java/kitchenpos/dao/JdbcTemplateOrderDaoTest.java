package kitchenpos.dao;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.helper.JdbcTestHelper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.fixture.OrderFixture.주문_생성;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JdbcTemplateOrderDaoTest extends JdbcTestHelper {

    @Autowired
    private JdbcTemplateOrderDao jdbcTemplateOrderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    void 주문을_저장한다() {
        // given
        OrderTable orderTable = orderTableDao.save(주문_테이블_생성(null, 1, false));
        Order order = 주문_생성(orderTable.getId(), COOKING.name(), LocalDateTime.now(), List.of());

        // when
        Order result = jdbcTemplateOrderDao.save(order);

        // then
        assertThat(result.getOrderStatus()).isEqualTo(COOKING.name());
    }

    @Test
    void 아이디로_주문을_조회한다() {
        // given
        OrderTable orderTable = orderTableDao.save(주문_테이블_생성(null, 1, false));
        Order order = 주문_생성(orderTable.getId(), COOKING.name(), LocalDateTime.now(), List.of());
        Order save = jdbcTemplateOrderDao.save(order);

        // when
        Optional<Order> result = jdbcTemplateOrderDao.findById(save.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(result).isPresent();
            softly.assertThat(result.get()).usingRecursiveComparison().isEqualTo(save);
        });
    }

    @Test
    void 모든_주문을_조회한다() {
        // when
        int result = jdbcTemplateOrderDao.findAll().size();

        // then
        assertThat(result).isEqualTo(0);
    }

    @Test
    void 주문_테이블_id가_일치하고_주문_상태들이_포함됐는지_확인한다() {
        // given
        Long orderTableId = 1L;
        List<String> orderStatuses = List.of();

        // when
        boolean result = jdbcTemplateOrderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, orderStatuses);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void 주문_테이블_id들이_포함되고_주문_상태들이_포함됐는지_확인한다() {
        // given
        List<Long> orderTableId = List.of(1L);
        List<String> orderStatuses = List.of();

        // when
        boolean result = jdbcTemplateOrderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableId, orderStatuses);

        // then
        assertThat(result).isFalse();
    }
}
