package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.common.ServiceTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableChangeEmptyRequest;
import kitchenpos.dto.OrderTableChangeNumberRequest;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest extends ServiceTest {

    private final List<String> EXCLUDE_STATUS = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());

    @Nested
    class create_성공_테스트 {

        @Test
        void 주문_테이블을_생성할_수_있다() {
            // given
            final var request = new OrderTableCreateRequest(null, 3, true);

            // when
            final var actual = tableService.create(request);

            // then
            assertThat(actual.getId()).isExactlyInstanceOf(Long.class);
        }
    }

    @Nested
    class create_실패_테스트 {
    }

    @Nested
    class list_성공_테스트 {

        @Test
        void 주문_목록이_존재하지_않으면_빈_값을_반환한다() {
            // given & when
            final var actual = tableService.list();

            // then
            assertThat(actual).isEmpty();
        }

        @Test
        void 주문이_하나_이상_존재하면_주문_목록을_반환한다() {
            // given
            tableGroupDao.save(new TableGroup(LocalDateTime.now(), List.of()));
            final var orderTable = orderTableDao.save(new OrderTable(1L, 3, false));

            final var expected = List.of(OrderTableResponse.toResponse(orderTable));

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
            orderTableDao.save(new OrderTable(null, 3, true));
            final var request = new OrderTableChangeEmptyRequest(null, 3, true);

            // when
            final var actual = tableService.changeEmpty(1L, request);

            // then
            assertThat(actual.isEmpty()).isTrue();
        }
    }

    @Nested
    class changeEmpty_실패_테스트 {

        @Test
        void 존재하지_않는_주문_테이블을_사용하면_예외를_반환한다() {
            // given
            final var request = new OrderTableChangeEmptyRequest(1L, 3, true);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(1L, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 존재하지 않는 주문 테이블입니다.");
        }

        @Test
        void 주문_테이블의_그룹_아이디가_존재하면_예외를_반환한다() {
            // given
            tableGroupDao.save(new TableGroup(LocalDateTime.now(), List.of()));
            orderTableDao.save(new OrderTable(1L, 3, true));
            final var request = new OrderTableChangeEmptyRequest(1L, 3, true);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(1L, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 해당 주문 테이블은 다른 그룹에 속하는 테이블입니다.");
        }

        @Test
        void 주문_테이블에_이미_주문_상태가_존재하면_예외를_반환한다() {
            // given
            tableGroupDao.save(new TableGroup(LocalDateTime.now(), List.of()));
            orderTableDao.save(new OrderTable(null, 3, true));
            orderDao.save(new Order(1L, "MEAL", LocalDateTime.now(), List.of()));
            final var request = new OrderTableChangeEmptyRequest(null, 3, true);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(1L, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 아직 모든 주문이 완료되지 않았습니다.");
        }
    }

    @Nested
    class changeNumberOfGuests_성공_테스트 {

        @Test
        void 손님의_수를_변경할_수_있다() {
            // given
            tableGroupDao.save(new TableGroup(LocalDateTime.now(), List.of()));
            orderTableDao.save(new OrderTable(1L, 3, false));
            final var request = new OrderTableChangeNumberRequest(1L, 2, false);

            // when
            final var actual = tableService.changeNumberOfGuests(1L, request);

            // then
            assertThat(actual.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
        }
    }

    @Nested
    class changeNumberOfGuests_실패_테스트 {

        @Test
        void 손님의_수가_음수일_때_예외를_반환한다() {
            // given
            final var request = new OrderTableChangeNumberRequest(1L, -3, false);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 손님의 수가 음수입니다.");
        }

        @Test
        void 존재하지_않는_테이블을_사용하면_예외를_반환한다() {
            // given
            tableGroupDao.save(new TableGroup(LocalDateTime.now(), List.of()));
            orderTableDao.save(new OrderTable(1L, 2, true));
            final var request = new OrderTableChangeNumberRequest(1L, 3, true);

                    // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(2L, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 존재하지 않는 주문 테이블입니다.");
        }

        @Test
        void 주문_테이블의_상태가_비어있으면_예외를_반환한다() {
            // given
            tableGroupDao.save(new TableGroup(LocalDateTime.now(), List.of()));
            orderTableDao.save(new OrderTable(1L, 2, true));
            final var request = new OrderTableChangeNumberRequest(1L, 2, true);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 주문 테이블이 비어있습니다.");
        }
    }
}
