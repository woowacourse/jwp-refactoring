package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("TableService 클래스의")
class TableServiceTest extends ServiceTest {

    @Test
    @DisplayName("create 메서드는 OrderTable을 생성한다.")
    void create() {
        // given
        OrderTable orderTable = createOrderTable(0, true);

        // when
        OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        Optional<OrderTable> actual = orderTableDao.findById(savedOrderTable.getId());
        assertThat(actual).isPresent();
    }

    @Test
    @DisplayName("list 메서드는 모든 OrderTable을 조회한다.")
    void list() {
        // when
        List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables).hasSize(0);
    }

    @Nested
    @DisplayName("changeEmpty 메서드는")
    class ChangeEmpty {

        @Test
        @DisplayName("OrderTable의 empty 여부를 업데이트한다.")
        void success() {
            // given
            OrderTable orderTable = createOrderTable(0, true);
            OrderTable savedOrderTable = tableService.create(orderTable);

            // when
            OrderTable updatedOrderTable = tableService.changeEmpty(savedOrderTable.getId(),
                    createOrderTable(0, false));

            // then
            assertThat(updatedOrderTable.isEmpty()).isFalse();
        }

        @Test
        @DisplayName("orderTable이 존재하지 않는 경우 예외를 던진다.")
        void orderTableId_NotExist_ExceptionThrown() {
            // given
            OrderTable orderTable = createOrderTable(0, true);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(9L, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Disabled("tableGroup 테스트 추가 후 구현하기")
        @Test
        @DisplayName("orderTable에 해당하는 tableGroupId가 null이 아닌 경우 예외를 던진다.")
        void tableGroupId_NotNull_ExceptionThrown() {
        }

        @Disabled("order 테스트 추가 후 구현하기")
        @Test
        @DisplayName("orderTableId에 해당하는 order의 orderStatus가 COMPLETION이 아닌 경우 예외를 던진다.")
        void orderStatus_NotCompletion_ExceptionThrown() {
        }
    }

    @Nested
    @DisplayName("changeNumberOfGuests 메서드는")
    class ChangeNumberOfGuests {

        @Test
        void success() {
            // given
            OrderTable orderTable = createOrderTable(2, true);

            // when
            OrderTable savedOrderTable = tableService.create(orderTable);

            // then
            Optional<OrderTable> actual = orderTableDao.findById(savedOrderTable.getId());
            assertThat(actual).isPresent();
            assertThat(actual.get().getNumberOfGuests()).isEqualTo(2);
        }

        @Test
        @DisplayName("테이블 인원 수가 0 미만인 경우 예외를 던진다.")
        void numberOfGuest_IsNegative_ExceptionThrown() {
            // given
            OrderTable savedOrderTable = tableService.create(createOrderTable(0, true));

            // when & then
            assertThatThrownBy(
                    () -> tableService.changeNumberOfGuests(savedOrderTable.getId(), createOrderTable(-1, true)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("orderTable이 존재하지 않는 경우 예외를 던진다.")
        void orderTableId_NotExist_ExceptionThrown() {
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(9L, createOrderTable(0, false)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("orderTable이 빈 테이블인 경우 예외를 던진다.")
        void emptyTable_ExceptionThrown() {
            // given
            OrderTable savedOrderTable = tableService.create(createOrderTable(0, true));

            // when & then
            assertThatThrownBy(
                    () -> tableService.changeNumberOfGuests(savedOrderTable.getId(), createOrderTable(2, false)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
