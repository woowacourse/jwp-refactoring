package kitchenpos.dao;

import kitchenpos.domain.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class JdbcTemplateOrderDaoTest {

    @Autowired
    private DataSource dataSource;

    private JdbcTemplateOrderDao jdbcTemplateOrderDao;

    @BeforeEach
    void setUp() {
        jdbcTemplateOrderDao = new JdbcTemplateOrderDao(dataSource);
    }

    @Test
    void saveAndFindById() {
        //when
        jdbcTemplateOrderDao.save(new Order(1L, "COOKING", LocalDateTime.now(), null));

        //then
        assertThat(jdbcTemplateOrderDao.findById(1L)).isNotNull();
    }

    @Test
    void findAll() {
        //when
        final List<Order> result = jdbcTemplateOrderDao.findAll();

        //then
        assertThat(result).hasSize(1);
    }

    @Test
    void existsByOrderTableIdAndOrderStatusIn() {
        //given
        jdbcTemplateOrderDao.save(new Order(1L, "COOKING", LocalDateTime.now(), null));

        //when
        final boolean result = jdbcTemplateOrderDao.existsByOrderTableIdAndOrderStatusIn(1L, List.of("COOKING"));

        //then
        assertThat(result).isTrue();
    }

    @Test
    void existsByOrderTableIdAndOrderStatusInIfNotMatchOrderStatus() {
        //given
        jdbcTemplateOrderDao.save(new Order(1L, "COOKING", LocalDateTime.now(), null));

        //when
        final boolean result = jdbcTemplateOrderDao.existsByOrderTableIdAndOrderStatusIn(1L, List.of("MEAL"));

        //then
        assertThat(result).isFalse();
    }

    @Test
    void existsByOrderTableIdInAndOrderStatusIn() {
        //given
        jdbcTemplateOrderDao.save(new Order(1L, "COOKING", LocalDateTime.now(), null));

        //when
        final boolean result = jdbcTemplateOrderDao.existsByOrderTableIdInAndOrderStatusIn(List.of(1L), List.of("COOKING"));

        //then
        assertThat(result).isTrue();
    }

    @Test
    void existsByOrderTableIdInAndOrderStatusInIfNotMatchOrderTableId() {
        //given
        jdbcTemplateOrderDao.save(new Order(1L, "COOKING", LocalDateTime.now(), null));

        //when
        final boolean result = jdbcTemplateOrderDao.existsByOrderTableIdInAndOrderStatusIn(List.of(3L), List.of("COOKING"));

        //then
        assertThat(result).isFalse();
    }
}
