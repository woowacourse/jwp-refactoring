package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.util.Lists.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.OrderTable;

@DisplayName("JdbcTemplateOrderTableDao 테스트")
@Sql("/dao-test.sql")
@JdbcTest
@Import(JdbcTemplateOrderTableDao.class)
class JdbcTemplateOrderTableDaoTest {
    @Autowired
    private JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao;

    @DisplayName("OrderTableDao save 테스트")
    @Test
    void save() {
        // Given
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(3);
        orderTable.setEmpty(false);

        // When
        final OrderTable savedOrderTable = jdbcTemplateOrderTableDao.save(orderTable);

        // Then
        assertAll(
                () -> assertThat(savedOrderTable)
                        .extracting(OrderTable::getId)
                        .isNotNull()
                ,
                () -> assertThat(savedOrderTable)
                        .extracting(OrderTable::getNumberOfGuests)
                        .isEqualTo(orderTable.getNumberOfGuests())
                ,
                () -> assertThat(savedOrderTable)
                        .extracting(OrderTable::isEmpty)
                        .isEqualTo(orderTable.isEmpty())
        );
    }

    @DisplayName("OrderTableDao findById 테스트")
    @Test
    void findById() {
        // When
        final OrderTable orderTable = jdbcTemplateOrderTableDao.findById(1L)
                .orElseThrow(IllegalArgumentException::new);

        // Then
        assertAll(
                () -> assertThat(orderTable)
                        .extracting(OrderTable::getNumberOfGuests)
                        .isEqualTo(0)
                ,
                () -> assertThat(orderTable)
                        .extracting(OrderTable::isEmpty)
                        .isEqualTo(true)
        );
    }

    @DisplayName("OrderTableDao findById Id가 존재하지 않을 경우")
    @Test
    void findById_NotExists() {
        // When
        final Optional<OrderTable> orderTable = jdbcTemplateOrderTableDao.findById(9L);

        // Then
        assertThat(orderTable.isPresent()).isFalse();
    }

    @DisplayName("OrderTableDao findAll 테스트")
    @Test
    void findAll() {
        // When
        final List<OrderTable> orderTables = jdbcTemplateOrderTableDao.findAll();

        // Then
        assertThat(orderTables).hasSize(8);
    }

    @DisplayName("OrderTableDao findAllByIdIn 테스트")
    @Test
    void findAllByIdIn() {
        // When
        final List<OrderTable> allByIdIn = jdbcTemplateOrderTableDao.findAllByIdIn(
                newArrayList(1L, 2L, 3L, 4L));

        // Then
        assertThat(allByIdIn).hasSize(4);
    }

    @DisplayName("OrderTableDao findAllByTableGroupId 테스트")
    @Test
    void findAllByTableGroupId() {
        // When
        final List<OrderTable> allByTableGroupId = jdbcTemplateOrderTableDao.findAllByTableGroupId(
                1L);

        // Then
        assertThat(allByTableGroupId).hasSize(2);
    }
}
