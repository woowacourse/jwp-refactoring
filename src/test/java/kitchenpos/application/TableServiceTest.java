package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
@DisplayName("TableService 테스트")
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {

        @Test
        @DisplayName("OrderTable을 반환한다.")
        void it_return_orderTable() {
            // given
            OrderTable orderTable = new OrderTable();
            orderTable.setNumberOfGuests(2);
            orderTable.setEmpty(false);
            given(orderTableDao.save(orderTable)).willReturn(orderTable);

            // when
            OrderTable createdOrderTable = tableService.create(orderTable);

            // then
            assertThat(createdOrderTable).isEqualTo(orderTable);
            then(orderTableDao)
                    .should()
                    .save(orderTable);
        }

    }

    @Nested
    @DisplayName("changeEmpty 메소드는")
    class Describe_changeEmpty {

        @Nested
        @DisplayName("조회된 OrderTable의 tableGroupId가 null이 아니면")
        class Context_with_tableGruoupId_not_null {

            @Test
            @DisplayName("예외가 발생한다.")
            void it_throws_exception() {
                // given
                OrderTable orderTable = new OrderTable();
                orderTable.setId(1L);
                orderTable.setTableGroupId(1L);
                orderTable.setNumberOfGuests(2);
                orderTable.setEmpty(false);

                OrderTable emptyOrderTable = new OrderTable();
                emptyOrderTable.setId(1L);
                emptyOrderTable.setTableGroupId(1L);
                emptyOrderTable.setNumberOfGuests(2);
                emptyOrderTable.setEmpty(true);

                given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(orderTable));

                // when, then
                assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), emptyOrderTable))
                        .isInstanceOf(IllegalArgumentException.class);
                then(orderTableDao)
                        .should()
                        .findById(orderTable.getId());
                then(orderDao)
                        .should(never())
                        .existsByOrderTableIdAndOrderStatusIn(
                                orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()));
                then(orderTableDao)
                        .should(never())
                        .save(orderTable);
            }
        }

        @Nested
        @DisplayName("OrderId에 해당되는 Order의 OrderStatus가 COOKING이나 MEAL이라면")
        class Context_with_orderStatus_cooking_or_meal {

            @Test
            @DisplayName("예외가 발생한다.")
            void it_throws_exception() {
                // given
                OrderTable orderTable = new OrderTable();
                orderTable.setId(1L);
                orderTable.setNumberOfGuests(2);
                orderTable.setEmpty(false);

                OrderTable emptyOrderTable = new OrderTable();
                emptyOrderTable.setId(1L);
                emptyOrderTable.setNumberOfGuests(2);
                emptyOrderTable.setEmpty(true);

                given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(orderTable));
                given(orderDao.existsByOrderTableIdAndOrderStatusIn(
                        orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

                // when, then
                assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), emptyOrderTable))
                        .isInstanceOf(IllegalArgumentException.class);
                then(orderTableDao)
                        .should()
                        .findById(orderTable.getId());
                then(orderDao)
                        .should()
                        .existsByOrderTableIdAndOrderStatusIn(
                                orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()));
                then(orderTableDao)
                        .should(never())
                        .save(orderTable);
            }

        }

        @Nested
        @DisplayName("정상적인 경우라면")
        class Context_with_correct_case {

            @Test
            @DisplayName("OrderTable을 반환한다.")
            void it_return_orderTable() {
                // given
                OrderTable orderTable = new OrderTable();
                orderTable.setId(1L);
                orderTable.setNumberOfGuests(2);
                orderTable.setEmpty(false);

                OrderTable emptyOrderTable = new OrderTable();
                emptyOrderTable.setId(1L);
                emptyOrderTable.setNumberOfGuests(2);
                emptyOrderTable.setEmpty(true);

                given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(orderTable));
                given(orderDao.existsByOrderTableIdAndOrderStatusIn(
                        orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
                given(orderTableDao.save(orderTable)).willReturn(emptyOrderTable);

                // when
                OrderTable changeEmptyOrderTable = tableService.changeEmpty(orderTable.getId(), emptyOrderTable);

                // then
                assertThat(changeEmptyOrderTable).isEqualTo(emptyOrderTable);
                then(orderTableDao)
                        .should()
                        .findById(orderTable.getId());
                then(orderDao)
                        .should()
                        .existsByOrderTableIdAndOrderStatusIn(
                                orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()));
                then(orderTableDao)
                        .should()
                        .save(orderTable);
            }
        }

    }

    @Nested
    @DisplayName("changeNumberOfGuests 메소드는")
    class Describe_changeNumberOfGuests {
        @Nested
        @DisplayName("입력받은 Order의 numberOfGuests 값이 음수라면")
        class Context_with_numberOfGuests_negative {

            @ParameterizedTest
            @ValueSource(ints = {-5, -100})
            @DisplayName("예외가 발생한다.")
            void it_throws_exception(int numberOfGuests) {
                // given
                OrderTable orderTable = new OrderTable();
                orderTable.setId(1L);
                orderTable.setNumberOfGuests(numberOfGuests);
                orderTable.setEmpty(false);

                // when, then
                assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
                        .isInstanceOf(IllegalArgumentException.class);
                then(orderTableDao)
                        .should(never())
                        .findById(orderTable.getId());
                then(orderTableDao)
                        .should(never())
                        .save(orderTable);
            }
        }

        @Nested
        @DisplayName("조회된 OrderTable이 빈 테이블이라면")
        class Context_with_orderTable_empty {

            @Test
            @DisplayName("예외가 발생한다.")
            void it_throws_exception() {
                // given
                OrderTable orderTable = new OrderTable();
                orderTable.setId(1L);
                orderTable.setNumberOfGuests(0);
                orderTable.setEmpty(true);

                OrderTable anotherOrderTable = new OrderTable();
                anotherOrderTable.setId(1L);
                anotherOrderTable.setNumberOfGuests(4);
                anotherOrderTable.setEmpty(false);

                given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(orderTable));

                // when, then
                assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), anotherOrderTable))
                        .isInstanceOf(IllegalArgumentException.class);
                then(orderTableDao)
                        .should()
                        .findById(orderTable.getId());
                then(orderTableDao)
                        .should(never())
                        .save(orderTable);
            }

        }

        @Nested
        @DisplayName("정상적인 경우라면")
        class Context_with_correct_case {

            @Test
            @DisplayName("OrderTable을 반환한다.")
            void it_return_orderTable() {
                // given
                OrderTable orderTable = new OrderTable();
                orderTable.setId(1L);
                orderTable.setNumberOfGuests(2);
                orderTable.setEmpty(false);

                OrderTable anotherOrderTable = new OrderTable();
                anotherOrderTable.setId(1L);
                anotherOrderTable.setNumberOfGuests(4);
                anotherOrderTable.setEmpty(false);

                given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(orderTable));
                given(orderTableDao.save(orderTable)).willReturn(anotherOrderTable);

                // when
                OrderTable changedOrderTable = tableService.changeEmpty(orderTable.getId(), anotherOrderTable);

                // then
                assertThat(changedOrderTable).isEqualTo(anotherOrderTable);
                then(orderTableDao)
                        .should()
                        .findById(orderTable.getId());
                then(orderTableDao)
                        .should()
                        .save(orderTable);
            }
        }
    }

    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {

        @Test
        @DisplayName("OrderTable 리스트를 반환한다.")
        void it_return_orderTable_list() {
            // given
            OrderTable orderTable = new OrderTable();
            orderTable.setId(1L);
            orderTable.setNumberOfGuests(3);
            orderTable.setEmpty(false);

            OrderTable anotherOrderTable = new OrderTable();
            anotherOrderTable.setId(2L);
            anotherOrderTable.setNumberOfGuests(4);
            anotherOrderTable.setEmpty(false);

            given(orderTableDao.findAll()).willReturn(Arrays.asList(orderTable, anotherOrderTable));

            // when
            List<OrderTable> orderTables = tableService.list();

            // then
            assertThat(orderTables).containsExactly(orderTable, anotherOrderTable);
            assertThat(orderTables).hasSize(2);
            then(orderTableDao)
                    .should()
                    .findAll();
        }
    }
}