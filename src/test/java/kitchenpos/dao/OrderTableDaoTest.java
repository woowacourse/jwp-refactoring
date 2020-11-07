package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.OrderTableFixture;

@JdbcTest
@Sql("classpath:/truncate.sql")
class OrderTableDaoTest {

    OrderTableDao orderTableDao;

    OrderTable orderTable;

    @BeforeEach
    void setUp(@Autowired DataSource dataSource) {
        orderTableDao = new JdbcTemplateOrderTableDao(dataSource);

        orderTable = OrderTableFixture.createEmptyWithoutId();
    }

    @DisplayName("Insert a order table")
    @Test
    void save() {
        OrderTable saved = orderTableDao.save(orderTable);

        assertThat(saved).isEqualToIgnoringGivenFields(orderTable, "id");
        assertThat(saved).extracting(OrderTable::getId).isEqualTo(1L);
    }

    @DisplayName("Select a order table")
    @Test
    void findById() {
        OrderTable saved = orderTableDao.save(orderTable);

        assertThat(orderTableDao.findById(saved.getId()).get())
            .isEqualToComparingFieldByField(saved);
    }

    @DisplayName("Select all order tables")
    @Test
    void findAll() {
        OrderTable saved1 = orderTableDao.save(orderTable);
        OrderTable saved2 = orderTableDao.save(orderTable);

        assertThat(orderTableDao.findAll())
            .usingRecursiveComparison()
            .isEqualTo(Arrays.asList(saved1, saved2));
    }

    @DisplayName("Select all order tables in ids")
    @Test
    void findAllByIdIn() {
        OrderTable saved1 = orderTableDao.save(orderTable);
        OrderTable saved2 = orderTableDao.save(orderTable);

        assertThat(orderTableDao.findAllByIdIn(Arrays.asList(saved1.getId(), saved2.getId())))
            .usingRecursiveComparison()
            .isEqualTo(Arrays.asList(saved1, saved2));
    }

    @DisplayName("Select all order tables in table group ids")
    @Test
    void findAllByTableGroupId() {
        OrderTable saved1 = orderTableDao.save(OrderTableFixture.createGroupTableWithId(null));
        OrderTable saved2 = orderTableDao.save(OrderTableFixture.createGroupTableWithId(null));

        assertThat(orderTableDao.findAllByTableGroupId(saved1.getTableGroupId()))
            .usingRecursiveComparison()
            .isEqualTo(Arrays.asList(saved1, saved2));
    }
}
