package kitchenpos.dao;

import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@Import(value = JdbcTemplateOrderTableDao.class)
@JdbcTest
class JdbcTemplateOrderTableDaoTest {

    @Autowired
    private JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao;

    @Test
    void 저장한다() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);

        // when
        OrderTable result = jdbcTemplateOrderTableDao.save(orderTable);

        // then
        assertThat(result.getId()).isEqualTo(9L);
    }

    @Test
    void 아이디를_기준으로_찾는다() {
        // given
        Long id = 1L;

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
        List<OrderTable> result = jdbcTemplateOrderTableDao.findAll();

        // then
        assertThat(result).hasSize(8);
    }

    @Test
    void id가_포함된_order_table을_모두_조회한다() {
        // given
        List<Long> ids = List.of(1L, 2L);

        // when
        List<OrderTable> result = jdbcTemplateOrderTableDao.findAllByIdIn(ids);

        // then
        assertThat(result).hasSize(2);
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
