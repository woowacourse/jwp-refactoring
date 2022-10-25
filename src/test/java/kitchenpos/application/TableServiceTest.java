package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.support.ClassConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("TableService의")
class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @MockBean
    private OrderTableDao orderTableDao;
    @MockBean
    private OrderDao orderDao;

    @Nested
    @DisplayName("changeEmpty 메서드는")
    class ChangeEmpty {

        private static final long ORDER_TABLE_ID = 1L;
        private static final long TABLE_GROUP_ID = 1L;

        private OrderTable savedOrderTable;
        private OrderTable orderTable;

        @BeforeEach
        void setUp() {
            savedOrderTable = ClassConstructor.orderTable(ORDER_TABLE_ID, null, 2, false);
            orderTable = ClassConstructor.orderTable(ORDER_TABLE_ID, null, 0, true);

            given(orderTableDao.findById(ORDER_TABLE_ID))
                    .willReturn(Optional.of(savedOrderTable));
            given(orderDao.existsByOrderTableIdAndOrderStatusIn(ORDER_TABLE_ID,
                    Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                    .willReturn(false);
            given(orderTableDao.save(savedOrderTable))
                    .willReturn(savedOrderTable);

        }

        @Test
        @DisplayName("주문 테이블의 비움 상태를 변경할 수 있다.")
        void success() {
            //when
            OrderTable actual = tableService.changeEmpty(ORDER_TABLE_ID, orderTable);

            //then
            assertThat(actual.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("주문 테이블이 존재하지 않으면, 예외를 던진다.")
        void fail_noExistTable() {
            //given
            given(orderTableDao.findById(ORDER_TABLE_ID))
                    .willReturn(Optional.empty());

            //when & then
            assertThatThrownBy(() -> tableService.changeEmpty(ORDER_TABLE_ID, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문 테이블이 그룹에 속해있으면, 예외를 던진다.")
        void fail_existTableGroup() {
            //given
            savedOrderTable.setTableGroupId(TABLE_GROUP_ID);

            //when & then
            assertThatThrownBy(() -> tableService.changeEmpty(ORDER_TABLE_ID, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문 테이블이 조리중이나 식사중이면, 예외를 던진다.")
        void fail_tableStatusIsCookingOrMeal() {
            //given
            given(orderDao.existsByOrderTableIdAndOrderStatusIn(ORDER_TABLE_ID,
                    Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                    .willReturn(true);

            //when & then
            assertThatThrownBy(() -> tableService.changeEmpty(ORDER_TABLE_ID, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("changeNumberOfGuests 메서드는")
    class changeNumberOfGuests {
        private static final long ORDER_TABLE_ID = 1L;

        private OrderTable savedOrderTable;
        private OrderTable orderTable;

        @BeforeEach
        void setUp() {
            savedOrderTable = ClassConstructor.orderTable(ORDER_TABLE_ID, null, 2, false);
            orderTable = ClassConstructor.orderTable(ORDER_TABLE_ID, null, 3, false);

            given(orderTableDao.findById(ORDER_TABLE_ID))
                    .willReturn(Optional.of(savedOrderTable));
            given(orderTableDao.save(savedOrderTable))
                    .willReturn(savedOrderTable);

        }

        @Test
        @DisplayName("주문 테이블의 인원을 변경할 수 있다.")
        void success() {
            //when
            OrderTable actual = tableService.changeNumberOfGuests(ORDER_TABLE_ID, orderTable);

            //then
            assertThat(actual.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
        }

        @Test
        @DisplayName("변경할 인원이 0보다 작으면, 예외를 던진다.")
        void fail_negativeNumber() {
            //given
            orderTable.setNumberOfGuests(-1);

            //when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(ORDER_TABLE_ID, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문 테이블이 존재하지 않으면, 예외를 던진다.")
        void fail_noExistOrderTable() {
            //given
            given(orderTableDao.findById(ORDER_TABLE_ID))
                    .willReturn(Optional.empty());

            //when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(ORDER_TABLE_ID, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문 테이블이 비어있으면, 예외를 던진다")
        void fail_emptyOrderTable() {
            //given
            savedOrderTable.setEmpty(true);

            //when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(ORDER_TABLE_ID, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
