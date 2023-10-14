package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.annotation.MockTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@MockTest
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @Test
    void 테이블_저장() {
        // when
        OrderTable orderTable = new OrderTable();
        tableService.create(orderTable);

        // then
        verify(orderTableDao).save(any(OrderTable.class));
    }

    @Test
    void 모든_테이블_조회() {
        // when
        tableService.list();

        // then
        verify(orderTableDao).findAll();
    }

    @Nested
    class 주문_상태_변경 {

        @Test
        void 주문_테이블이_DB에_저장되어_있지_않으면_예외를_발생한다() {
            // given
            long orderTableId = 1L;
            OrderTable orderTable = new OrderTable();
            when(orderTableDao.findById(orderTableId)).thenReturn(Optional.empty());

            // then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블의_TableGroupId가_Null이_아니면_예외를_발생한다() {
            // given
            long orderTableId = 1L;
            OrderTable orderTable = new OrderTable();
            orderTable.setId(1L);
            orderTable.setTableGroupId(1L);
            when(orderTableDao.findById(orderTableId)).thenReturn(Optional.of(orderTable));

            // then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블에_담긴_주문의_상태가_COOKING_또는_MEAL인_주문이_없으면_예외를_발생한다() {
            // given
            long orderTableId = 1L;
            OrderTable orderTable = new OrderTable();
            orderTable.setId(1L);

            when(orderTableDao.findById(orderTableId)).thenReturn(Optional.of(orderTable));
            when(orderDao.existsByOrderTableIdAndOrderStatusIn(
                    orderTableId,
                    Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))
            ).thenReturn(true);

            // then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블의_주문_가능_상태를_빈_테이블로_변경한다() {
            // given
            long orderTableId = 1L;
            OrderTable orderTable = new OrderTable();
            orderTable.setId(1L);

            when(orderTableDao.findById(orderTableId)).thenReturn(Optional.of(orderTable));
            when(orderDao.existsByOrderTableIdAndOrderStatusIn(
                    orderTableId,
                    Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))
            ).thenReturn(false);

            when(orderTableDao.save(any(OrderTable.class))).thenReturn(orderTable);

            // when
            OrderTable result = tableService.changeEmpty(orderTableId, orderTable);

            // then
            assertThat(result.isEmpty()).isEqualTo(orderTable.isEmpty());
        }
    }

    @Nested
    class 테이블_손님_수_변경 {

        @Test
        void 요청된_손님_수가_0_미만이면_예외가_발생한다() {
            // given
            long orderTableId = 1L;
            OrderTable orderTable = new OrderTable();
            orderTable.setNumberOfGuests(-1);

            // then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청된_주문_테이블이_DB에_저장되어_있지_않으면_예외를_발생한다() {
            // given
            long orderTableId = 1L;
            OrderTable orderTable = new OrderTable();
            orderTable.setNumberOfGuests(10);

            when(orderTableDao.findById(orderTableId)).thenReturn(Optional.empty());

            // then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 변경하려는_주문_테이블이_빈_테이블이면_예외를_발생한다() {
            // given
            long orderTableId = 1L;
            OrderTable orderTable = new OrderTable();
            orderTable.setId(orderTableId);
            orderTable.setNumberOfGuests(10);
            orderTable.setEmpty(true);

            when(orderTableDao.findById(orderTableId)).thenReturn(Optional.of(orderTable));

            // then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블의_손님_수를_변경한다() {
            // given
            long orderTableId = 1L;
            OrderTable before = new OrderTable();
            before.setId(orderTableId);
            before.setNumberOfGuests(1);
            when(orderTableDao.findById(orderTableId)).thenReturn(Optional.of(before));

            OrderTable after = new OrderTable();
            after.setId(orderTableId);
            after.setNumberOfGuests(10);
            when(orderTableDao.save(any(OrderTable.class))).thenReturn(after);

            // when
            OrderTable result = tableService.changeNumberOfGuests(orderTableId, before);

            // then
            assertThat(result.getNumberOfGuests()).isNotEqualTo(before.getNumberOfGuests());
            assertThat(result.getNumberOfGuests()).isEqualTo(after.getNumberOfGuests());
        }
    }
}
