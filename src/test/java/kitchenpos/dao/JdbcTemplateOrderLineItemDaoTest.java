package kitchenpos.dao;

import kitchenpos.common.repository.RepositoryTest;
import kitchenpos.domain.OrderLineItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcTemplateOrderLineItemDaoTest extends RepositoryTest {

    private JdbcTemplateOrderLineItemDao jdbcTemplateOrderLineItemDao;

    @BeforeEach
    void setUp() {
        jdbcTemplateOrderLineItemDao = new JdbcTemplateOrderLineItemDao(dataSource);
    }

    @Test
    void saveAndFindById() {
        //when
        final OrderLineItem orderLineItem = jdbcTemplateOrderLineItemDao.save(new OrderLineItem(1L, 1L, 1));

        //then
        assertThat(jdbcTemplateOrderLineItemDao.findById(orderLineItem.getSeq())).isNotNull();
    }

    @Test
    void findAll() {
        //when
        final var result = jdbcTemplateOrderLineItemDao.findAll();

        //then
        assertThat(result).isEmpty();
    }

    @Test
    void findAllByOrderId() {
        //given
        jdbcTemplateOrderLineItemDao.save(new OrderLineItem(1L, 1L, 1));

        //when
        final var result = jdbcTemplateOrderLineItemDao.findAllByOrderId(1L);

        //then
        assertThat(result).hasSize(1);
    }
}
