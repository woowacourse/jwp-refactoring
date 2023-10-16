package kitchenpos.dao;

import kitchenpos.common.repository.RepositoryTest;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcTemplateOrderTableDaoTest extends RepositoryTest {

    private JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao;

    @BeforeEach
    void setUp() {
        jdbcTemplateOrderTableDao = new JdbcTemplateOrderTableDao(dataSource);
    }

    @Test
    void saveAndFindById() {
        //when
        final OrderTable orderTable = jdbcTemplateOrderTableDao.save(new OrderTable(1L, 1, true));

        //then
        assertThat(jdbcTemplateOrderTableDao.findById(orderTable.getId())).isNotNull();
    }

    @Test
    void findAll() {
        //when
        final List<OrderTable> result = jdbcTemplateOrderTableDao.findAll();

        //then
        assertThat(result).hasSize(8);
    }

    @Test
    void findAllByIdIn() {
        //when
        final List<OrderTable> result = jdbcTemplateOrderTableDao.findAllByIdIn(List.of(1L, 2L, 10L));

        //then
        assertThat(result).hasSize(2);
    }

    @Test
    void findAllByTableGroupId() {
        //given
        jdbcTemplateOrderTableDao.save(new OrderTable(1L, 1, true));
        jdbcTemplateOrderTableDao.save(new OrderTable(1L, 1, true));

        //when
        final List<OrderTable> result = jdbcTemplateOrderTableDao.findAllByTableGroupId(1L);

        //then
        assertThat(result).hasSize(2);
    }
}
