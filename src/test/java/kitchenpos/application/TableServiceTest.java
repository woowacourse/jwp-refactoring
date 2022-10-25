package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.data.util.Pair;

@DisplayName("TableService의")
class TableServiceTest extends ServiceTest {

    @Nested
    @DisplayName("create 메서드는")
    class Create {

        @ParameterizedTest
        @CsvSource(value = {"0,true", "1,false", "1,true"})
        @DisplayName("주문 테이블을 등록할 수 있다.")
        void create_validOrderTable_success(final int numberOfGuests, final boolean empty) {
            // given
            final OrderTable expected = new OrderTable();
            expected.setNumberOfGuests(numberOfGuests);
            expected.setEmpty(empty);

            // when
            final OrderTable actual = tableService.create(expected);

            // then
            softly.assertThat(actual.getTableGroupId()).isNull();
            softly.assertThat(actual.getNumberOfGuests()).isEqualTo(expected.getNumberOfGuests());
            softly.assertThat(actual.isEmpty()).isEqualTo(expected.isEmpty());
            softly.assertAll();
        }
    }

    @Nested
    @DisplayName("list 메서드는")
    class ListTest {

        @Test
        @DisplayName("주문 테이블 목록을 조회할 수 있다.")
        void list_savedOrderTables_success() {
            // given
            final OrderTable orderTable1 = saveOrderTable(777, true);
            final OrderTable orderTable2 = saveOrderTable(999, false);

            // when
            final List<OrderTable> actual = tableService.list();

            // then
            assertThat(actual).extracting(OrderTable::getNumberOfGuests, OrderTable::isEmpty)
                    .containsExactly(
                            tuple(orderTable1.getNumberOfGuests(), orderTable1.isEmpty()),
                            tuple(orderTable2.getNumberOfGuests(), orderTable2.isEmpty())
                    );
        }
    }

    @Nested
    @DisplayName("changeEmpty 메서드는")
    class ChangeEmpty {

        @Test
        @DisplayName("주문 등록 가능 여부를 변경한다.")
        void changeEmpty_validOrderTable_success() {
            // given
            final Long orderTableId = saveOrderTable(10, false).getId();
            final OrderTable orderTableToUpdate = new OrderTable();
            orderTableToUpdate.setEmpty(true);

            // when
            final OrderTable actual = tableService.changeEmpty(orderTableId, orderTableToUpdate);

            // then
            assertThat(actual.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("주문 테이블이 존재하지 않으면 주문 등록 가능 여부를 변경할 수 없다.")
        void changeEmpty_orderTableNotExist_exception() {
            // given
            final OrderTable orderTableToUpdate = new OrderTable();
            orderTableToUpdate.setEmpty(true);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(999L, orderTableToUpdate))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest(name = "주문 상태: {0}")
        @DisplayName("주문 테이블에 주문이 있다면 주문 상태는 계산 완료인 경우에만 주문 등록 가능 여부를 변경할 수 없다.")
        @ValueSource(strings = {"COOKING", "MEAL"})
        void changeEmpty_orderStatusIsCompletion_exception(final String orderStatus) {
            // given
            final Product product = saveProduct("감자튀김");
            final MenuGroup menuGroup = saveMenuGroup("감자");
            final Menu menu = saveMenu("감자세트", BigDecimal.ONE, menuGroup, Pair.of(product, 1L));
            final OrderTable orderTable = saveOrderTable(10, false);
            saveOrder(orderTable, orderStatus, Pair.of(menu, 1L));

            final OrderTable orderTableToUpdate = new OrderTable();
            orderTableToUpdate.setEmpty(true);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTableToUpdate))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("단체 지정된 주문 테이블은 주문 등록 가능 여부를 변경할 수 없다.")
        void changeEmpty_inTableGroup_exception() {
            // given
            final OrderTable orderTable = saveOrderTable(10, true);
            final OrderTable otherOrderTable = saveOrderTable(7, false);

            saveTableGroup(orderTable, otherOrderTable);

            final OrderTable orderTableToUpdate = new OrderTable();
            orderTableToUpdate.setEmpty(true);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTableToUpdate))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("changeNumberOfGuests 메서드는")
    class ChangeNumberOfGuests {

        @Test
        @DisplayName("방문한 손님 수를 변경한다.")
        void changeNumberOfGuests_validNumberOfGuests_success() {
            // given
            final Long orderTableId = saveOrderTable(1, false).getId();

            final OrderTable orderTableToUpdate = new OrderTable();
            orderTableToUpdate.setNumberOfGuests(10);

            // when
            final OrderTable actual = tableService.changeNumberOfGuests(orderTableId, orderTableToUpdate);

            // then
            assertThat(actual.getNumberOfGuests()).isEqualTo(orderTableToUpdate.getNumberOfGuests());
        }

        @Test
        @DisplayName("방문한 손님 수는 음수일 수 없다.")
        void changeNumberOfGuests_numberOfGuestsIsNegative_exception() {
            // given
            final Long orderTableId = saveOrderTable(1, false).getId();

            final OrderTable orderTableToUpdate = new OrderTable();
            orderTableToUpdate.setNumberOfGuests(-1);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTableToUpdate))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("존재하지 않는 주문 테이블은 변경할 수 없다.")
        void changeNumberOfGuests_notExistOrderTable_exception() {
            // given
            final OrderTable orderTableToUpdate = new OrderTable();
            orderTableToUpdate.setNumberOfGuests(10);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(999L, orderTableToUpdate))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("빈 주문 테이블의 방문한 손님 수는 변경할 수 없다.")
        void changeNumberOfGuests_emptyTable_exception() {
            // given
            final Long orderTableId = saveOrderTable(1, true).getId();

            final OrderTable orderTableToUpdate = new OrderTable();
            orderTableToUpdate.setNumberOfGuests(10);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTableToUpdate))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
