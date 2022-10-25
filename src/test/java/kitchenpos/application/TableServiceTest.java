package kitchenpos.application;

import static kitchenpos.fixture.TableFixture.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("TableService 클래스의")
class TableServiceTest extends ServiceTest {

    @Test
    @DisplayName("create 메서드는 OrderTable을 생성한다.")
    void create() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);

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
        saveOrderTable(0, true);
        saveOrderTable(0, true);
        saveOrderTable(0, true);
        List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables).hasSize(3);
    }

    @Nested
    @DisplayName("changeEmpty 메서드는")
    class ChangeEmpty {

        @Test
        @DisplayName("OrderTable의 empty 여부를 업데이트한다.")
        void success() {
            // given
            OrderTable orderTable = saveOrderTable(0, true);

            // when
            OrderTable updatedOrderTable = tableService.changeEmpty(orderTable.getId(),
                    createOrderTable(0, false));

            // then
            assertThat(updatedOrderTable.isEmpty()).isFalse();
        }

        @Test
        @DisplayName("orderTable이 존재하지 않는 경우 예외를 던진다.")
        void orderTableId_NotExist_ExceptionThrown() {
            // given
            OrderTable orderTable = saveOrderTable(0, true);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(9L, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("orderTable에 해당하는 tableGroupId가 null이 아닌 경우 예외를 던진다.")
        void tableGroupId_NotNull_ExceptionThrown() {
            // given
            OrderTable orderTable1 = saveOrderTable(2, true);
            OrderTable orderTable2 = saveOrderTable(4, true);
            saveTableGroup(orderTable1, orderTable2);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), createOrderTable(2, false)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @EnumSource(
                value = OrderStatus.class,
                names = {"COMPLETION"},
                mode = EnumSource.Mode.EXCLUDE)
        @DisplayName("orderTableId에 해당하는 order의 orderStatus가 COMPLETION이 아닌 경우 예외를 던진다.")
        void orderStatus_NotCompletion_ExceptionThrown(OrderStatus orderStatus) {
            // given
            OrderTable orderTable = saveOrderTable(2, false);
            MenuGroup menuGroup = saveMenuGroup("반마리치킨");
            Product product = saveProduct("크림치킨", BigDecimal.valueOf(15000.00));
            Menu menu1 = saveMenu("크림치킨", menuGroup, product);
            Menu menu2 = saveMenu("크림어니언치킨", menuGroup, product);
            Order savedOrder = saveOrder(orderTable, menu1, menu2);
            updateOrder(savedOrder, orderStatus.name());

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), createOrderTable(2, false)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("changeNumberOfGuests 메서드는")
    class ChangeNumberOfGuests {

        @Test
        @DisplayName("테이블 인원 수를 변경한다.")
        void success() {
            // given
            OrderTable orderTable = saveOrderTable(2, false);

            // when
            OrderTable updatedOrderTable = tableService.changeNumberOfGuests(orderTable.getId(),
                    createOrderTable(4, false));

            // then
            Optional<OrderTable> actual = orderTableDao.findById(updatedOrderTable.getId());
            assertThat(actual).isPresent();
            assertThat(actual.get().getNumberOfGuests()).isEqualTo(4);
        }

        @Test
        @DisplayName("테이블 인원 수가 0 미만인 경우 예외를 던진다.")
        void numberOfGuest_IsNegative_ExceptionThrown() {
            // given
            OrderTable savedOrderTable = saveOrderTable(0, true);

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
            OrderTable savedOrderTable = saveOrderTable(0, true);

            // when & then
            assertThatThrownBy(
                    () -> tableService.changeNumberOfGuests(savedOrderTable.getId(), createOrderTable(2, false)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
