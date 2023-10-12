package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @Test
    void 테이블을_정상_생성한다() {
        assertDoesNotThrow(() -> tableService.create(new OrderTable()));
    }

    @Test
    void 테이블을_전체_조회한다() {
        assertThat(tableService.list()).isInstanceOf(List.class);
    }

    @Nested
    class 비었는지_여부_변경_기능_테스트 {

        @Test
        void 테이블의_비었는지_여부를_정상_변경한다() {
            // given
            OrderTable givenOrderTable = new OrderTable();
            givenOrderTable.setId(1L);
            givenOrderTable.setEmpty(true);

            OrderTable savedORderTable = new OrderTable();
            savedORderTable.setTableGroupId(null);

            given(orderTableDao.findById(anyLong())).willReturn(Optional.of(savedORderTable));
            given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(
                    false);
            given(orderTableDao.save(any(OrderTable.class))).willReturn(savedORderTable);

            // when, then
            assertThat(tableService.changeEmpty(1L, givenOrderTable).isEmpty()).isTrue();
        }

        @Test
        void 변경하려는_테이블_id가_존재하지_않으면_예외를_반환한다() {
            // given
            given(orderTableDao.findById(anyLong())).willReturn(Optional.empty());

            // when, then
            assertThrows(IllegalArgumentException.class,
                    () -> tableService.changeEmpty(1L, new OrderTable()));
        }

        @Test
        void 변경하려는_테이블이_단체_테이블에_속해있다면_예외를_반환한다() {
            // given
            OrderTable savedOrderTable = new OrderTable();
            savedOrderTable.setTableGroupId(1L);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.of(savedOrderTable));

            // when, then
            assertThrows(IllegalArgumentException.class,
                    () -> tableService.changeEmpty(1L, new OrderTable()));
        }
    }

    @Nested
    class 방문한_손님_수_변경_기능_테스트 {

        @Test
        void 방문한_손님_수를_정상_변경한다() {
            // given
            OrderTable orderTable = new OrderTable();
            orderTable.setId(1L);
            orderTable.setNumberOfGuests(2);

            OrderTable savedOrderTable = new OrderTable();
            savedOrderTable.setEmpty(false);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.of(savedOrderTable));
            given(orderTableDao.save(any(OrderTable.class))).willReturn(savedOrderTable);

            // when
            OrderTable resultOrderTable = tableService.changeNumberOfGuests(1L, orderTable);

            // then
            assertThat(resultOrderTable.getNumberOfGuests()).isEqualTo(
                    orderTable.getNumberOfGuests());
        }

        @Test
        void 손님_수가_음수인_경우_예외를_반환한다() {
            // given
            OrderTable orderTable = new OrderTable();
            orderTable.setNumberOfGuests(-1);

            // when, then
            assertThrows(IllegalArgumentException.class,
                    () -> tableService.changeNumberOfGuests(1L, orderTable));
        }

        @Test
        void 손님_수를_변경하려는_테이블이_빈_테이블인_경우_예외를_반환한다() {
            // given
            OrderTable orderTable = new OrderTable();
            orderTable.setId(1L);
            orderTable.setNumberOfGuests(2);

            OrderTable savedOrderTable = new OrderTable();
            savedOrderTable.setEmpty(true);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.of(savedOrderTable));

            // when, then
            assertThrows(IllegalArgumentException.class,
                    () -> tableService.changeNumberOfGuests(1L, orderTable));
        }
    }
}
