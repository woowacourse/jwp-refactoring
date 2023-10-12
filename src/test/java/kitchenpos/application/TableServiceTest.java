package kitchenpos.application;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;
    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    @DisplayName("주문 테이블을 생성한다.")
    void createTest() {
        // given
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);

        // when
        final OrderTable expect = tableService.create(orderTable);

        // then
        final OrderTable actual = orderTableDao.findById(expect.getId()).get();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expect);
    }

    @Test
    @DisplayName("주문 테이블 목록을 조회한다.")
    void listTest() {
        // given
        final List<OrderTable> preSavedOrderTables = orderTableDao.findAll();

        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);
        orderTableDao.save(orderTable);

        final List<OrderTable> expect = preSavedOrderTables;
        expect.add(orderTable);

        // when & then
        final List<OrderTable> actual = tableService.list();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expect);
    }

    @Nested
    @DisplayName("주문 테이블 인원수 변경 테스트")
    class ChangeNumberOfGuestsTest {

        @ParameterizedTest
        @CsvSource(value = {"0", "1", "100"})
        @DisplayName("유효한 주문 테이블의 인원수를 0 이상으로 업데이트한다.")
        void changeEmptyTest(final int expectNumberOfGuests) {
            // given
            final OrderTable orderTable = new OrderTable();
            orderTable.setNumberOfGuests(0);

            final OrderTable savedOrderTable = orderTableDao.save(orderTable);

            // when
            savedOrderTable.setNumberOfGuests(expectNumberOfGuests);

            tableService.changeNumberOfGuests(savedOrderTable.getId(), savedOrderTable);

            // then
            final OrderTable actual = orderTableDao.findById(savedOrderTable.getId()).get();

            assertEquals(expectNumberOfGuests, actual.getNumberOfGuests());
        }

        @ParameterizedTest
        @CsvSource(value = {"-1", "-2", "-100"})
        @DisplayName("유효한 주문 테이블의 0 미만으로 업데이트하면 IllegalArgumentException이 발생한다.")
        void changeNumberOfGuestsWithNegativeNumberOfGuests(final int expectNumberOfGuests) {
            // given
            final OrderTable orderTable = new OrderTable();
            orderTable.setNumberOfGuests(0);

            final OrderTable savedOrderTable = orderTableDao.save(orderTable);

            // when & then
            savedOrderTable.setNumberOfGuests(expectNumberOfGuests);

            assertThrowsExactly(IllegalArgumentException.class,
                    () -> tableService.changeNumberOfGuests(savedOrderTable.getId(), savedOrderTable));
        }

        @Test
        @DisplayName("유효하지 않은 주문 테이블의 인원수를 0 이상으로 업데이트하면 IllegalArgumentException이 발생한다.")
        void changeNumberOfGuestsWithInvalidOrderTable() {
            // given
            final OrderTable orderTable = new OrderTable();
            orderTable.setNumberOfGuests(0);

            // when & then
            assertThrowsExactly(IllegalArgumentException.class,
                    () -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable));
        }
    }
}
