package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("TableService 단위 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @DisplayName("create 메서드는")
    @Nested
    class Describe_create {

        @DisplayName("OrderTable을 저장 및 반환한다.")
        @Test
        void it_saves_and_returns_order_table() {
            // given
            OrderTable orderTable = new OrderTable();
            OrderTable expected = new OrderTable();
            expected.setId(1L);
            given(orderTableDao.save(orderTable)).willReturn(expected);

            // when
            OrderTable response = tableService.create(orderTable);

            // then
            assertThat(response).usingRecursiveComparison()
                .isEqualTo(expected);

            verify(orderTableDao, times(1)).save(orderTable);
        }
    }

    @DisplayName("list 메서드는")
    @Nested
    class Describe_list {

        @DisplayName("OrderTable을 목록을 반환한다.")
        @Test
        void it_returns_order_table_list() {
            // given
            OrderTable orderTable = new OrderTable();
            OrderTable orderTable2 = new OrderTable();
            given(orderTableDao.findAll()).willReturn(Arrays.asList(orderTable, orderTable2));

            // when
            List<OrderTable> list = tableService.list();

            // then
            assertThat(list).hasSize(2);

            verify(orderTableDao, times(1)).findAll();
        }
    }

    @DisplayName("changeEmpty 메서드는")
    @Nested
    class Describe_changeEmpty {

        @DisplayName("ID에 해당하는 OrderTable이 없다면")
        @Nested
        class Context_order_table_not_found {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                OrderTable orderTable = new OrderTable();
                given(orderTableDao.findById(1L)).willReturn(Optional.empty());

                // when, then
                assertThatCode(() -> tableService.changeEmpty(1L, orderTable))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("OrderTable이 존재하지 않습니다.");

                verify(orderTableDao, times(1)).findById(1L);
            }
        }

        @DisplayName("OrderTable이 이미 특정 TableGroup에 속한 경우")
        @Nested
        class Context_order_table_has_group {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                OrderTable orderTable = new OrderTable();
                OrderTable savedOrderTable = new OrderTable();
                savedOrderTable.setTableGroupId(1L);
                given(orderTableDao.findById(1L)).willReturn(Optional.of(savedOrderTable));

                // when, then
                assertThatCode(() -> tableService.changeEmpty(1L, orderTable))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("OrderTable이 속한 TableGroup이 존재합니다.");

                verify(orderTableDao, times(1)).findById(1L);
            }
        }

        @DisplayName("OrderTable에 속한 Order가 조리중 혹은 식사중이라면")
        @Nested
        class Context_order_status_cooking_or_meal {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                OrderTable orderTable = new OrderTable();
                OrderTable savedOrderTable = new OrderTable();
                List<String> orderStatuses = Arrays
                    .asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());
                given(orderTableDao.findById(1L)).willReturn(Optional.of(savedOrderTable));
                given(orderDao.existsByOrderTableIdAndOrderStatusIn(1L, orderStatuses)).willReturn(true);

                // when, then
                assertThatCode(() -> tableService.changeEmpty(1L, orderTable))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("OrderTable에 속한 Order 중 일부가 조리 혹은 식사 중입니다.");

                verify(orderTableDao, times(1)).findById(1L);
                verify(orderDao, times(1)).existsByOrderTableIdAndOrderStatusIn(1L, orderStatuses);
            }
        }

        @DisplayName("정상적인 경우")
        @Nested
        class Context_valid_condition {

            @DisplayName("OrderTable의 상태를 변경하고 반환한다.")
            @Test
            void it_changes_and_returns_order_table_status() {
                // given
                OrderTable orderTable = new OrderTable();
                orderTable.setEmpty(true);
                OrderTable savedOrderTable = new OrderTable();
                List<String> orderStatuses = Arrays
                    .asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());
                given(orderTableDao.findById(1L)).willReturn(Optional.of(savedOrderTable));
                given(orderDao.existsByOrderTableIdAndOrderStatusIn(1L, orderStatuses)).willReturn(false);
                given(orderTableDao.save(savedOrderTable)).willReturn(savedOrderTable);

                // when
                OrderTable response = tableService.changeEmpty(1L, orderTable);

                assertThat(response.isEmpty()).isTrue();

                verify(orderTableDao, times(1)).findById(1L);
                verify(orderDao, times(1)).existsByOrderTableIdAndOrderStatusIn(1L, orderStatuses);
                verify(orderTableDao, times(1)).save(savedOrderTable);
            }
        }
    }

    @DisplayName("changeNumberOfGuest 메서드는")
    @Nested
    class Describe_changeNumberOfGuest {

        @DisplayName("변경하려는 손님 숫자가 음수인 경우")
        @Nested
        class Context_number_of_guests_negative {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                OrderTable orderTable = new OrderTable();
                orderTable.setNumberOfGuests(-1);

                // when, then
                assertThatCode(() -> tableService.changeNumberOfGuests(1L, orderTable))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("변경하려는 손님 수는 음수일 수 없습니다.");
            }
        }

        @DisplayName("OrderTable을 조회할 수 없는 경우")
        @Nested
        class Context_order_table_not_found {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                OrderTable orderTable = new OrderTable();
                orderTable.setNumberOfGuests(0);
                given(orderTableDao.findById(1L)).willReturn(Optional.empty());

                // when, then
                assertThatCode(() -> tableService.changeNumberOfGuests(1L, orderTable))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("OrderTable이 존재하지 않습니다.");

                verify(orderTableDao, times(1)).findById(1L);
            }
        }

        @DisplayName("조회된 OrderTable이 비어있는 경우")
        @Nested
        class Context_saved_order_table_is_empty {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                OrderTable orderTable = new OrderTable();
                OrderTable savedOrderTable = new OrderTable();
                orderTable.setNumberOfGuests(0);
                savedOrderTable.setEmpty(true);
                given(orderTableDao.findById(1L)).willReturn(Optional.of(savedOrderTable));

                // when, then
                assertThatCode(() -> tableService.changeNumberOfGuests(1L, orderTable))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("OrderTable이 비어있는 상태입니다.");

                verify(orderTableDao, times(1)).findById(1L);
            }
        }

        @DisplayName("정상적인 경우")
        @Nested
        class Context_valid_condition {

            @DisplayName("OrderTable의 손님 수를 변경한다.")
            @Test
            void it_changes_and_returns_order_table() {
                // given
                OrderTable orderTable = new OrderTable();
                OrderTable savedOrderTable = new OrderTable();
                orderTable.setNumberOfGuests(369);
                savedOrderTable.setEmpty(false);
                savedOrderTable.setNumberOfGuests(10);
                given(orderTableDao.findById(1L)).willReturn(Optional.of(savedOrderTable));
                given(orderTableDao.save(savedOrderTable)).willReturn(savedOrderTable);

                // when
                OrderTable response = tableService.changeNumberOfGuests(1L, orderTable);

                // then
                assertThat(response.getNumberOfGuests()).isEqualTo(369);

                verify(orderTableDao, times(1)).findById(1L);
                verify(orderTableDao, times(1)).save(savedOrderTable);
            }
        }
    }
}
