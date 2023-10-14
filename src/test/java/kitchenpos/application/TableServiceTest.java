package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.anyList;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@SuppressWarnings("NonAsciiCharacters")
@ServiceMockTest
class TableServiceTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Test
    void 주문_테이블을_생성한다() {
        OrderTable 주문_테이블_A = OrderTableFixture.주문_테이블_A;
        given(orderTableDao.save(any(OrderTable.class)))
                .willReturn(주문_테이블_A);

        OrderTable response = tableService.create(주문_테이블_A);

        assertThat(response).usingRecursiveComparison().isEqualTo(주문_테이블_A);
    }

    @Test
    void 모든_주문_테이블을_조회한다() {
        OrderTable 주문_테이블_A = OrderTableFixture.주문_테이블_A;
        OrderTable 주문_테이블_B = OrderTableFixture.주문_테이블_B_EMPTY_상태;
        given(orderTableDao.findAll())
                .willReturn(List.of(주문_테이블_A, 주문_테이블_B));

        List<OrderTable> orderTables = orderTableDao.findAll();

        assertThat(orderTables).usingRecursiveComparison().isEqualTo(List.of(주문_테이블_A, 주문_테이블_B));
    }

    @Nested
    class 주문_테이블_EMPTY_상태를_변경할_때 {

        @Test
        void 정상적으로_변경한다() {
            OrderTable 주문_테이블_상태_변경_EMPTY = OrderTableFixture.주문_테이블_J_상태_변경_EMPTY;
            OrderTable 주문_테이블_D_NOT_EMPTY_상태 = OrderTableFixture.주문_테이블_D_NOT_EMPTY_상태;
            given(orderTableDao.findById(anyLong()))
                    .willReturn(Optional.of(주문_테이블_상태_변경_EMPTY));
            given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
                    .willReturn(false);
            given(orderTableDao.save(any(OrderTable.class)))
                    .willReturn(주문_테이블_D_NOT_EMPTY_상태);

            OrderTable response = tableService.changeEmpty(주문_테이블_상태_변경_EMPTY.getId(), 주문_테이블_D_NOT_EMPTY_상태);

            assertThat(response.isEmpty()).isEqualTo(주문_테이블_D_NOT_EMPTY_상태.isEmpty());
        }

        @Test
        void 주문_테이블을_찾을_수_없으면_예외가_발생한다() {
            OrderTable 주문_테이블_D_NOT_EMPTY_상태 = OrderTableFixture.주문_테이블_D_NOT_EMPTY_상태;
            given(orderTableDao.findById(anyLong()))
                    .willReturn(Optional.empty());

            assertThatThrownBy(() -> tableService.changeEmpty(-1L, 주문_테이블_D_NOT_EMPTY_상태))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블의_주문_중_COMPLETION_상태가_아닌_주문이_존재하면_예외가_발생한다() {
            OrderTable 주문_테이블_B_Empty_상태 = OrderTableFixture.주문_테이블_B_EMPTY_상태;
            OrderTable 주문_테이블_D_NOT_EMPTY_상태 = OrderTableFixture.주문_테이블_D_NOT_EMPTY_상태;
            given(orderTableDao.findById(anyLong()))
                    .willReturn(Optional.of(주문_테이블_B_Empty_상태));
            given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
                    .willReturn(true);

            assertThatThrownBy(() -> tableService.changeEmpty(주문_테이블_B_Empty_상태.getId(), 주문_테이블_D_NOT_EMPTY_상태))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 주문_테이블에_손님_수를_변경할_때 {

        @Test
        void 정상적으로_변경한다() {
            OrderTable 주문_테이블_I_손님_10명 = OrderTableFixture.주문_테이블_I_손님_변경_10명;
            OrderTable 주문_테이블_H_손님_5명 = OrderTableFixture.주문_테이블_H_손님_변경_5명;
            given(orderTableDao.findById(anyLong()))
                    .willReturn(Optional.of(주문_테이블_I_손님_10명));
            given(orderTableDao.save(any(OrderTable.class)))
                    .willReturn(주문_테이블_H_손님_5명);

            OrderTable response = tableService.changeNumberOfGuests(주문_테이블_I_손님_10명.getId(), 주문_테이블_H_손님_5명);

            assertThat(response.getNumberOfGuests()).isEqualTo(주문_테이블_H_손님_5명.getNumberOfGuests());
        }

        @Test
        void 변경하는_손님_수가_0명_이면_예외가_발생한다() {
            OrderTable 주문_테이블_A_손님_10명 = OrderTableFixture.주문_테이블_A;
            OrderTable 주문_테이블_F_손님_0명 = OrderTableFixture.주문_테이블_F_손님_0명;

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문_테이블_A_손님_10명.getId(), 주문_테이블_F_손님_0명))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 변경하는_손님_수가_음수_이면_예외가_발생한다() {
            OrderTable 주문_테이블_A_손님_10명 = OrderTableFixture.주문_테이블_A;
            OrderTable 주문_테이블_G_손님_음수 = OrderTableFixture.주문_테이블_G_손님_음수;

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문_테이블_A_손님_10명.getId(), 주문_테이블_G_손님_음수))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_주문_테이블이면_예외가_발생한다() {
            OrderTable 주문_테이블_H_손님_5명 = OrderTableFixture.주문_테이블_H_손님_변경_5명;
            given(orderTableDao.findById(anyLong()))
                    .willReturn(Optional.empty());

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(-1L, 주문_테이블_H_손님_5명))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 변경하는_주문_테이블이_EMTPY_상태이면_예외가_발생한다() {
            OrderTable 주문_테이블_B_Empty_상태 = OrderTableFixture.주문_테이블_B_EMPTY_상태;
            OrderTable 주문_테이블_H_손님_5명 = OrderTableFixture.주문_테이블_H_손님_변경_5명;
            given(orderTableDao.findById(anyLong()))
                    .willReturn(Optional.of(주문_테이블_B_Empty_상태));

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문_테이블_B_Empty_상태.getId(), 주문_테이블_H_손님_5명))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
