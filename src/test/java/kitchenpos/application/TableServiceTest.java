package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.supports.OrderTableFixture;
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
class TableServiceTest {

    @Mock
    OrderDao orderDao;

    @Mock
    OrderTableDao orderTableDao;

    @InjectMocks
    TableService tableService;

    @Nested
    class 테이블_생성 {

        @Test
        void 손님_수와_빈_테이블_여부를_입력받아_생성한다() {
            // given
            Integer numberOfGuests = 0;
            boolean empty = true;
            OrderTableFixture orderTableFixture = OrderTableFixture.fixture().numberOfGuests(numberOfGuests)
                .empty(empty);
            OrderTable orderTable = orderTableFixture.build();
            OrderTable savedOrderTable = orderTableFixture.id(1L).build();

            given(orderTableDao.save(orderTable))
                .willReturn(savedOrderTable);

            // when
            OrderTable actual = tableService.create(orderTable);

            // then
            assertThat(actual).isEqualTo(savedOrderTable);
        }
    }

    @Nested
    class 테이블_리스트_조회 {

        @Test
        void 전체_테이블을_조회한다() {
            // given
            List<OrderTable> orderTables = List.of(
                OrderTableFixture.fixture().id(1L).build(),
                OrderTableFixture.fixture().id(2L).build()
            );

            given(orderTableDao.findAll())
                .willReturn(orderTables);

            // when
            List<OrderTable> actual = tableService.list();

            // then
            assertThat(actual).isEqualTo(orderTables);
        }
    }

    @Nested
    class 빈_테이블_여부_변경 {

        @Test
        void 주문_테이블은_DB에_존재해야한다() {
            // given
            Long orderTableId = 1L;
            OrderTable orderTable = OrderTableFixture.fixture().build();

            given(orderTableDao.findById(orderTableId))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 주문 테이블입니다.");
        }

        @Test
        void 이미_단체지정된_테이블이면_예외이다() {
            // given
            Long orderTableId = 1L;
            OrderTable orderTable = OrderTableFixture.fixture().build();
            OrderTable savedOrderTable = OrderTableFixture.fixture().tableGroupId(1L).build();

            given(orderTableDao.findById(orderTableId))
                .willReturn(Optional.of(savedOrderTable));

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 단체 지정된 테이블입니다.");
        }

        @Test
        void 조리중이거나_식사중인_주문이_있으면_예외이다() {
            // given
            Long orderTableId = 1L;
            OrderTable orderTable = OrderTableFixture.fixture().build();
            OrderTable savedOrderTable = OrderTableFixture.fixture().tableGroupId(null).build();

            given(orderTableDao.findById(orderTableId))
                .willReturn(Optional.of(savedOrderTable));
            given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId,
                List.of(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(true);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("조리중이거나 식사중인 주문이 있습니다.");
        }

        @Test
        void 주문_테이블_id와_빈_테이블_여부를_입력받아_변경한다() {
            // given
            Long orderTableId = 1L;
            boolean empty = true;

            OrderTable orderTable = OrderTableFixture.fixture().build();
            OrderTable savedOrderTable = OrderTableFixture.fixture().tableGroupId(null).build();

            given(orderTableDao.findById(orderTableId))
                .willReturn(Optional.of(savedOrderTable));
            given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId,
                List.of(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(false);
            given(orderTableDao.save(savedOrderTable))
                .willReturn(OrderTableFixture.fixture().empty(empty).tableGroupId(null).build());

            // when
            OrderTable actual = tableService.changeEmpty(orderTableId, orderTable);

            // then
            assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(OrderTableFixture.fixture().empty(empty).tableGroupId(null).build());
        }
    }

    @Nested
    class 손님_수_변경 {

        @Test
        void 손님_수는_음수일_수_없다() {
            // given
            Long orderTableId = 1L;
            OrderTable orderTable = OrderTableFixture.fixture().numberOfGuests(-1).build();

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님 수는 음수일 수 없습니다.");
        }

        @Test
        void 주문_테이블은_DB에_존재해야한다() {
            // given
            Long orderTableId = 1L;
            OrderTable orderTable = OrderTableFixture.fixture().numberOfGuests(0).build();

            given(orderTableDao.findById(orderTableId))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 주문 테이블입니다.");
        }

        @Test
        void 주문_테이블이_빈_테이블이면_예외이다() {
            // given
            Long orderTableId = 1L;
            OrderTableFixture orderTableFixture = OrderTableFixture.fixture().numberOfGuests(0).empty(true);
            OrderTable orderTable = orderTableFixture.build();
            OrderTable savedOrderTable = orderTableFixture.id(1L).build();

            given(orderTableDao.findById(orderTableId))
                .willReturn(Optional.of(savedOrderTable));

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비어있는 주문 테이블입니다.");
        }

        @Test
        void 주문_테이블의_손님_수를_변경한다() {
            // given
            Long orderTableId = 1L;
            int numberOfGuests = 0;
            OrderTableFixture orderTableFixture = OrderTableFixture.fixture().numberOfGuests(numberOfGuests)
                .empty(false);
            OrderTable orderTable = orderTableFixture.build();
            OrderTable savedOrderTable = orderTableFixture.id(1L).build();

            given(orderTableDao.findById(orderTableId))
                .willReturn(Optional.of(savedOrderTable));
            given(orderTableDao.save(savedOrderTable))
                .willReturn(orderTableFixture.id(1L).numberOfGuests(numberOfGuests).build());

            // when
            OrderTable actual = tableService.changeNumberOfGuests(orderTableId, orderTable);

            // then
            assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(orderTableFixture.id(1L).numberOfGuests(numberOfGuests).build());
        }
    }
}
