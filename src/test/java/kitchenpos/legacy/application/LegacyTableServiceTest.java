package kitchenpos.legacy.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyList;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.eq;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import kitchenpos.legacy.dao.OrderDao;
import kitchenpos.legacy.dao.OrderTableDao;
import kitchenpos.legacy.domain.OrderTable;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class LegacyTableServiceTest {

    @InjectMocks
    LegacyTableService tableService;

    @Mock
    OrderDao orderDao;

    @Mock
    OrderTableDao orderTableDao;

    @Nested
    class create {

        @Test
        void 성공() {
            // given
            OrderTable orderTable = new OrderTable();
            OrderTable savedOrderTable = new OrderTable();
            savedOrderTable.setId(1L);
            given(orderTableDao.save(any(OrderTable.class)))
                .willReturn(savedOrderTable);

            // when
            OrderTable actual = tableService.create(orderTable);

            // then
            assertThat(actual.getId()).isEqualTo(1L);
        }
    }

    @Nested
    class findAll {

        @Test
        void 성공() {
            // given
            given(orderTableDao.findAll())
                .willReturn(List.of(new OrderTable(), new OrderTable()));

            // when
            List<OrderTable> actual = tableService.findAll();

            // then
            assertThat(actual).hasSize(2);
        }
    }

    @Nested
    class changeEmpty {

        @Test
        void 주문_테이블_식별자로_테이블을_찾을_수_없으면_예외() {
            // given
            Long orderTableId = 1L;
            OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(true);

            // when
            given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.empty());

            // then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 조회한_테이블이_테이블_그룹에_등록되어_있으면_예외() {
            // given
            Long orderTableId = 1L;
            OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(true);
            OrderTable savedOrderTable = new OrderTable();
            given(orderTableDao.findById(1L))
                .willReturn(Optional.of(savedOrderTable));

            // when
            savedOrderTable.setTableGroupId(1L);

            // then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 조회한_테이블의_주문_상태가_COMPLETION이_아니면_예외() {
            // given
            Long orderTableId = 1L;
            OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(true);
            OrderTable savedOrderTable = new OrderTable();
            given(orderTableDao.findById(1L))
                .willReturn(Optional.of(savedOrderTable));

            // when
            given(orderDao.existsByOrderTableIdAndOrderStatusIn(eq(1L), anyList()))
                .willReturn(true);

            // then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 성공() {
            // given
            Long orderTableId = 1L;
            OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(true);
            OrderTable savedOrderTable = new OrderTable();
            savedOrderTable.setEmpty(false);
            given(orderTableDao.findById(1L))
                .willReturn(Optional.of(savedOrderTable));
            given(orderDao.existsByOrderTableIdAndOrderStatusIn(eq(1L), anyList()))
                .willReturn(false);
            given(orderTableDao.save(any(OrderTable.class)))
                .willReturn(savedOrderTable);

            // when
            OrderTable actual = tableService.changeEmpty(orderTableId, orderTable);

            // then
            assertThat(actual.isEmpty()).isTrue();
        }
    }

    @Nested
    class changeNumberOfGuests {

        @Test
        void 방문한_손님_수가_음수이면_예외() {
            // given
            Long orderTableId = 1L;
            OrderTable orderTable = new OrderTable();

            // when
            orderTable.setNumberOfGuests(-1);

            // then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 조회한_테이블이_빈_테이블이면_예외() {
            // given
            Long orderTableId = 1L;
            OrderTable orderTable = new OrderTable();
            orderTable.setNumberOfGuests(1);
            OrderTable savedOrderTable = new OrderTable();
            given(orderTableDao.findById(1L))
                .willReturn(Optional.of(savedOrderTable));

            // when
            savedOrderTable.setEmpty(true);

            // then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 방문한_손님_수가_0이어도_통과() {
            Long orderTableId = 1L;
            OrderTable orderTable = new OrderTable();
            OrderTable savedOrderTable = new OrderTable();
            given(orderTableDao.findById(1L))
                .willReturn(Optional.of(savedOrderTable));

            // when
            orderTable.setNumberOfGuests(0);

            // then
            assertThatNoException().isThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTable));
        }

        @Test
        void 성공() {
            // given
            Long orderTableId = 1L;
            OrderTable orderTable = new OrderTable();
            orderTable.setNumberOfGuests(5);
            OrderTable savedOrderTable = new OrderTable();
            savedOrderTable.setEmpty(false);
            given(orderTableDao.findById(1L))
                .willReturn(Optional.of(savedOrderTable));
            given(orderTableDao.save(any(OrderTable.class)))
                .willReturn(savedOrderTable);

            // when
            OrderTable actual = tableService.changeNumberOfGuests(orderTableId, orderTable);

            // then
            assertThat(actual.getNumberOfGuests()).isEqualTo(5);
        }
    }
}
