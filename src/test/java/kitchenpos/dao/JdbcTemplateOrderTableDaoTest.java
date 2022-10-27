package kitchenpos.dao;

import static kitchenpos.support.OrderTableFixtures.ORDER_TABLE1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderTable;
import kitchenpos.support.OrderTableFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

@JdbcTest
@Import(JdbcTemplateOrderTableDao.class)
public class JdbcTemplateOrderTableDaoTest {

    @Autowired
    private JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao;

    @DisplayName("OrderTable을 저장한다.")
    @Test
    void save() {
        // given
        final OrderTable orderTable = ORDER_TABLE1.createWithIdNull();

        // when
        final OrderTable savedOrderTable = jdbcTemplateOrderTableDao.save(orderTable);

        // then
        assertThat(savedOrderTable.getId()).isNotNull();
    }

    @DisplayName("OrderTable을 하나 조회한다.")
    @Test
    void findById() {
        // given
        final OrderTable expected = ORDER_TABLE1.create();

        // when
        final Optional<OrderTable> orderTable = jdbcTemplateOrderTableDao.findById(1L);

        // then
        assertAll(
                () -> assertThat(orderTable.isPresent()).isTrue(),
                () -> assertThat(orderTable.get()).usingRecursiveComparison().isEqualTo(expected)
        );
    }

    @DisplayName("OrderTable을 모두 조회한다.")
    @Test
    void findAll() {
        // given
        final List<OrderTable> expected = OrderTableFixtures.createAll();

        // when
        final List<OrderTable> orderTables = jdbcTemplateOrderTableDao.findAll();

        // then
        assertThat(orderTables).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }

    @DisplayName("OrderTable을 id들에 해당하는 것을 조회한다.")
    @Test
    void findALlByIdIn() {
        // given
        final List<OrderTable> expected = OrderTableFixtures.createAll();

        // when
        final List<OrderTable> orderTables = jdbcTemplateOrderTableDao.findAllByIdIn(
                Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L));

        // then
        assertThat(orderTables).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
