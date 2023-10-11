package kitchenpos.dao;

import kitchenpos.domain.OrderLineItem;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
class JdbcTemplateOrderLineItemDaoTest {

    @Autowired
    private DataSource dataSource;

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
        assertThat(result).hasSize(0);
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
