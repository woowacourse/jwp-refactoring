package kitchenpos.application;

import static kitchenpos.fixture.TableFixture.createOrderTableRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.product.domain.Product;
import kitchenpos.table.dto.OrderTableRequest;
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
        OrderTableRequest orderTable = new OrderTableRequest(0, true);

        // when
        OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        Optional<OrderTable> actual = orderTableRepository.findById(savedOrderTable.getId());
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
                    createOrderTableRequest(0, false));

            // then
            assertThat(updatedOrderTable.isEmpty()).isFalse();
        }

        @Test
        @DisplayName("orderTable이 존재하지 않는 경우 예외를 던진다.")
        void orderTableId_NotExist_ExceptionThrown() {
            // given
            saveOrderTable(0, true);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(9L, createOrderTableRequest(2, false)))
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
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), createOrderTableRequest(2, false)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @EnumSource(
                value = OrderStatus.class,
                names = {"COMPLETION"},
                mode = EnumSource.Mode.EXCLUDE)
        @DisplayName("orderTableId에 해당하는 order의 orderStatus가 COOKING 또는 MEAL 상태인 경우 예외를 던진다.")
        void orderStatus_NotCompletion_ExceptionThrown(OrderStatus orderStatus) {
            // given
            OrderTable orderTable = saveOrderTable(2, false);
            MenuGroup menuGroup = saveMenuGroup("반마리치킨");
            Product product = saveProduct("크림치킨", BigDecimal.valueOf(15000.00));
            Menu menu1 = saveMenu("크림치킨", menuGroup, product);
            Menu menu2 = saveMenu("크림어니언치킨", menuGroup, product);
            Order savedOrder = saveOrder(orderTable, menu1, menu2);
            entityManager.flush();
            updateOrder(savedOrder, orderStatus.name());

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), createOrderTableRequest(2, false)))
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
                    createOrderTableRequest(4, false));

            // then
            Optional<OrderTable> actual = orderTableRepository.findById(updatedOrderTable.getId());
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
                    () -> tableService.changeNumberOfGuests(savedOrderTable.getId(), createOrderTableRequest(-1, true)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("orderTable이 존재하지 않는 경우 예외를 던진다.")
        void orderTableId_NotExist_ExceptionThrown() {
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(9L, createOrderTableRequest(0, false)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("orderTable이 빈 테이블인 경우 예외를 던진다.")
        void emptyTable_ExceptionThrown() {
            // given
            OrderTable savedOrderTable = saveOrderTable(0, true);

            // when & then
            assertThatThrownBy(
                    () -> tableService.changeNumberOfGuests(savedOrderTable.getId(), createOrderTableRequest(2, false)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
