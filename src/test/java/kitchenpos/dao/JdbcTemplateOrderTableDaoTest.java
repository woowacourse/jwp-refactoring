package kitchenpos.dao;

import kitchenpos.domain.OrderTable;
import kitchenpos.helper.JdbcTestHelper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static kitchenpos.fixture.OrderTableFixture.주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JdbcTemplateOrderTableDaoTest extends JdbcTestHelper {

    @Autowired
    private JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao;

    @Test
    void 저장한다() {
        // when
        OrderTable result = jdbcTemplateOrderTableDao.save(주문_테이블_생성(null, 0, true));

        // then
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void 아이디를_기준으로_찾는다() {
        // given
        OrderTable orderTable = jdbcTemplateOrderTableDao.save(주문_테이블_생성(null, 0, true));
        Long id = orderTable.getId();

        // when
        Optional<OrderTable> result = jdbcTemplateOrderTableDao.findById(id);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result).isPresent();
            softly.assertThat(result.get().getId()).isEqualTo(id);
        });
    }

    @Test
    void 모두_조회한다() {
        // when
        jdbcTemplateOrderTableDao.save(주문_테이블_생성(null, 0, true));
        List<OrderTable> result = jdbcTemplateOrderTableDao.findAll();

        // then
        assertThat(result).hasSize(1);
    }

    @Test
    void id가_포함된_order_table을_모두_조회한다() {
        // given
        OrderTable orderTable = jdbcTemplateOrderTableDao.save(주문_테이블_생성(null, 0, true));
        List<Long> ids = List.of(orderTable.getId());

        // when
        List<OrderTable> result = jdbcTemplateOrderTableDao.findAllByIdIn(ids);

        // then
        assertThat(result).hasSize(1);
    }

    @Test
    void 테이블_그룹_id로_모두_찾는다() {
        // given
        Long groupId = 1L;

        // when
        List<OrderTable> result = jdbcTemplateOrderTableDao.findAllByTableGroupId(1L);

        // then
        assertThat(result).isEmpty();
    }
}
