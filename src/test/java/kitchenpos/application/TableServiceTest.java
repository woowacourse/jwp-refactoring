package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @Nested
    class 테이블_등록 {

        @Test
        void 테이블을_등록할_수_있다() {
            // given
            final var orderTable = OrderTableFixture.주문테이블_1명();
            given(orderTableDao.save(any()))
                    .willReturn(orderTable);

            // when
            final var actual = tableService.create(orderTable);

            // then
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(orderTable);
        }
    }

    @Nested
    class 테이블_목록_조회 {

        @Test
        void 테이블_목록을_조회할_수_있다() {
            // given
            final var orderTable = Collections.singletonList(OrderTableFixture.주문테이블_1명());
            given(orderTableDao.findAll())
                    .willReturn(orderTable);

            // when
            final var actual = tableService.list();

            // then
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(orderTable);
        }
    }

    @Nested
    class 테이블_상태_변경 {

        @Test
        void 주문_테이블을_빈_테이블로_변경할_수_있다() {
            // given
            final var orderTable = OrderTableFixture.주문테이블_1명();
            given(orderTableDao.findById(any()))
                    .willReturn(Optional.of(orderTable));
            given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any()))
                    .willReturn(false);

            final var expected = OrderTableFixture.빈테이블_1명();
            given(orderTableDao.save(any()))
                    .willReturn(expected);

            // when
            final var actual = tableService.changeEmpty(orderTable.getId(), orderTable);

            // then
            assertThat(actual.isEmpty()).isTrue();
        }

        @Test
        void 주문_테이블이_존재하지_않으면_예외가_발생한다() {
            // given
            final var orderTable = OrderTableFixture.주문테이블_1명();
            given(orderTableDao.findById(any()))
                    .willReturn(Optional.empty());

            // when & then
            final var id = orderTable.getId();
            assertThatThrownBy(() -> tableService.changeEmpty(id, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 단체_지정되어_있는_테이블이면_예외가_발생한다() {
            // given
            final var orderTable = OrderTableFixture.주문테이블_1명();
            orderTable.setTableGroupId(1L);
            given(orderTableDao.findById(any()))
                    .willReturn(Optional.of(orderTable));

            // when & then
            final var id = orderTable.getId();
            assertThatThrownBy(() -> tableService.changeEmpty(id, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_상태가_조리_또는_식사면_예외가_발생한다() {
            // given
            final var orderTable = OrderTableFixture.주문테이블_1명();
            given(orderTableDao.findById(any()))
                    .willReturn(Optional.of(orderTable));
            given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any()))
                    .willReturn(true);

            // when & then
            final var id = orderTable.getId();
            assertThatThrownBy(() -> tableService.changeEmpty(id, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 방문한_손님_수_변경 {

        @Test
        void 주문_테이블의_방문한_손님_수를_변경할_수_있다() {
            // given
            final var orderTable = OrderTableFixture.주문테이블_1명();
            given(orderTableDao.findById(any()))
                    .willReturn(Optional.of(orderTable));

            final var expected = OrderTableFixture.주문테이블_5명();
            given(orderTableDao.save(any()))
                    .willReturn(expected);

            // when
            final var actual = tableService.changeNumberOfGuests(orderTable.getId(), expected);

            // then
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(expected);
        }

        @Test
        void 방문한_손님_수가_0보다_작으면_예외가_발생한다() {
            // given
            final var orderTable = OrderTableFixture.주문테이블_1명();
            orderTable.setNumberOfGuests(-1);

            // when & then
            final var id = orderTable.getId();
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(id, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_존재하지_않으면_예외가_발생한다() {
            // given
            final var orderTable = OrderTableFixture.주문테이블_1명();
            given(orderTableDao.findById(any()))
                    .willReturn(Optional.empty());

            // when & then
            final var id = orderTable.getId();
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(id, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 빈_테이블이면_예외가_발생한다() {
            // given
            final var orderTable = OrderTableFixture.빈테이블_1명();
            given(orderTableDao.findById(any()))
                    .willReturn(Optional.of(orderTable));

            // when & then
            final var id = orderTable.getId();
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(id, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
