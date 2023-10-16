package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.common.ServiceTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest extends ServiceTest {

    private final List<String> EXCLUDE_STATUS = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());

    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Nested
    class create_성공_테스트 {

        @Test
        void 주문_테이블을_생성할_수_있다() {
            // given
            final var beforeOrderTable = new OrderTable(1L, 3, true);
            final var afterOrderTable = new OrderTable(null, 3, true);

            given(orderTableDao.save(any(OrderTable.class))).willAnswer(invocation -> invocation.getArgument(0));

            // when
            final var actual = tableService.create(beforeOrderTable);

            // then
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(afterOrderTable);
        }
    }

    @Nested
    class create_실패_테스트 {
    }

    @Nested
    class list_성공_테스트 {

        @Test
        void 주문_목록이_존재하지_않으면_빈_값을_반환한다() {
            // given
            given(tableService.list()).willReturn(Collections.emptyList());

            // when
            final var actual = tableService.list();

            // then
            assertThat(actual).isEmpty();
        }

        @Test
        void 주문이_하나_이상_존재하면_주문_목록을_반환한다() {
            // given
            final var orderTable = new OrderTable(1L, 3, false);

            given(tableService.list()).willReturn(List.of(orderTable));

            final var expected = List.of(orderTable);

            // when
            final var actual = tableService.list();

            // then
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(expected);
        }
    }

    @Nested
    class list_실패_테스트 {
    }

    @Nested
    class changeEmpty_성공_테스트 {

        @Test
        void 주문_테이블을_빈_테이블로_만들_수_있다() {
            // given
            final var orderTable = new OrderTable(null, 3, true);
            orderTable.setId(1L);

            given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));
            given(orderDao.existsByOrderTableIdAndOrderStatusIn(1L, EXCLUDE_STATUS)).willReturn(Boolean.FALSE);
            given(orderTableDao.save(any(OrderTable.class))).willAnswer(invocation -> invocation.getArgument(0));

            // when
            final var actual = tableService.changeEmpty(1L, orderTable);

            // then
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(orderTable);
        }
    }

    @Nested
    class changeEmpty_실패_테스트 {

        @Test
        void 존재하지_않는_주문_테이블을_사용하면_예외를_반환한다() {
            // given
            final var orderTable = new OrderTable(1L, 3, true);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 존재하지 않는 주문 테이블입니다.");
        }

        @Test
        void 주문_테이블의_그룹_아이디가_존재하면_예외를_반환한다() {
            // given
            final var orderTable = new OrderTable(1L, 3, true);

            given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 해당 주문 테이블은 다른 그룹에 속하는 테이블입니다.");
        }

        @Test
        void 주문_테이블에_이미_주문_상태가_존재하면_예외를_반환한다() {
            // given
            final var orderTable = new OrderTable(null, 3, true);
            orderTable.setId(1L);

            given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));
            given(orderDao.existsByOrderTableIdAndOrderStatusIn(1L, EXCLUDE_STATUS)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 아직 모든 주문이 완료되지 않았습니다.");
        }
    }

    @Nested
    class changeNumberOfGuests_성공_테스트 {

        @Test
        void 손님의_수를_변경할_수_있다() {
            // given
            final var beforeOrderTable = new OrderTable(1L, 3, false);
            final var afterOrderTable = new OrderTable(1L, 2, false);

            given(orderTableDao.findById(1L)).willReturn(Optional.of(beforeOrderTable));
            given(orderTableDao.save(any(OrderTable.class))).willAnswer(invocation -> invocation.getArgument(0));

            // when
            final var actual = tableService.changeNumberOfGuests(1L, afterOrderTable);

            // then
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(afterOrderTable);
        }
    }

    @Nested
    class changeNumberOfGuests_실패_테스트 {

        @Test
        void 손님의_수가_음수일_때_예외를_반환한다() {
            // given
            final var orderTable = new OrderTable(1L, -3, false);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 손님의 수가 음수입니다.");
        }

        @Test
        void 존재하지_않는_테이블을_사용하면_예외를_반환한다() {
            // given
            final var orderTable = new OrderTable(1L, 2, true);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 존재하지 않는 주문 테이블입니다.");
        }

        @Test
        void 주문_테이블의_상태가_비어있으면_예외를_반환한다() {
            // given
            final var orderTable = new OrderTable(1L, 2, true);

            given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 주문 테이블이 비어있습니다.");
        }
    }
}
