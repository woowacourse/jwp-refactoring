package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @Nested
    class 주문_테이블_생성 {

        @Test
        void 주문_테이블을_생성한다() {
            // given
            OrderTable orderTable = OrderTableFixture.ORDER_TABLE.주문_테이블_1_비어있음();
            given(orderTableDao.save(any(OrderTable.class)))
                    .willReturn(orderTable);

            // when
            OrderTable result = tableService.create(orderTable);

            // then
            assertThat(result)
                    .usingRecursiveComparison()
                    .isEqualTo(orderTable);
        }
    }

    @Nested
    class 주문_테이블_목록_조회 {

        @Test
        void 주문_테이블_목록을_조회한다() {
            // given
            OrderTable orderTable = new OrderTable();
            given(orderTableDao.findAll())
                    .willReturn(List.of(orderTable));

            // when & then
            assertThat(tableService.list())
                    .usingRecursiveComparison()
                    .isEqualTo(List.of(orderTable));
        }
    }

    @Nested
    class 테이블_상태_변경 {

        @Test
        void 빈_테이블_상태를_변경한다() {
            // given
            OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(true);
            given(orderTableDao.findById(anyLong()))
                    .willReturn(Optional.of(orderTable));
            given(orderTableDao.save(any(OrderTable.class)))
                    .willReturn(orderTable);

            // when
            OrderTable result = tableService.changeEmpty(1L, orderTable);

            // then
            assertThat(result)
                    .usingRecursiveComparison()
                    .isEqualTo(orderTable);
        }

        @Test
        void 테이블이_존재하지_않으면_예외() {
            // given
            OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(true);
            given(orderTableDao.findById(anyLong()))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 빈_테이블_상태를_변경할_때_테이블_그룹에_속해있으면_예외() {
            // given
            OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(true);
            orderTable.setTableGroupId(1L);
            given(orderTableDao.findById(anyLong()))
                    .willReturn(Optional.of(orderTable));

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 빈_테이블_상태를_변경할_때_주문_상태가_조리중_또는_식사중이면_예외() {
            // given
            OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(true);
            given(orderTableDao.findById(anyLong()))
                    .willReturn(Optional.of(orderTable));
            given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
                    .willReturn(true);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 방문한_손님_수_변경 {

        @Test
        void 방문한_손님_수를_변경한다() {
            // given
            OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(false);
            orderTable.setNumberOfGuests(0);
            OrderTable expectTable = new OrderTable();
            expectTable.setEmpty(false);
            expectTable.setNumberOfGuests(5);
            given(orderTableDao.findById(anyLong()))
                    .willReturn(Optional.of(orderTable));
            given(orderTableDao.save(any(OrderTable.class)))
                    .willReturn(expectTable);

            // when
            OrderTable result = tableService.changeNumberOfGuests(1L, expectTable);

            // then
            assertThat(result.getNumberOfGuests())
                    .isEqualTo(expectTable.getNumberOfGuests());
        }

        @Test
        void 방문한_손님_수를_변경할_때_손님_수가_음수이면_예외() {
            // given
            OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(false);
            orderTable.setNumberOfGuests(-1);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 방문한_손님_수를_변경할_때_테이블이_비어있으면_예외() {
            // given
            OrderTable orderTable = OrderTableFixture.ORDER_TABLE.주문_테이블_1_비어있음();
            orderTable.setEmpty(true);
            given(orderTableDao.findById(anyLong()))
                    .willReturn(Optional.of(orderTable));

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
