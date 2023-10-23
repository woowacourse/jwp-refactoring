package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Orders;
import kitchenpos.dto.OrderTableChangeEmptyRequest;
import kitchenpos.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.exception.CannotChangeEmptyTableNumberOfGuestsException;
import kitchenpos.exception.InvalidRequestParameterException;
import kitchenpos.exception.UnCompletedOrderExistsException;
import kitchenpos.exception.OrderTableNotFoundException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@Transactional
@SpringBootTest
class TableServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private TableService tableService;

    @Test
    void 테이블을_정상_생성한다() {
        // given
        int numberOfGuests = 1;
        boolean empty = true;
        OrderTableCreateRequest request = new OrderTableCreateRequest(numberOfGuests, empty);

        // when
        OrderTableResponse response = tableService.create(request);

        // then
        SoftAssertions.assertSoftly(softly -> {
            assertThat(response.getId()).isNotNull();
            assertThat(response.getTableGroupId()).isNull();
            assertThat(response.getNumberOfGuests()).isEqualTo(numberOfGuests);
            assertThat(response.getEmpty()).isEqualTo(empty);
        });
    }

    @Test
    void 테이블을_전체_조회한다() {
        assertThat(tableService.list()).isInstanceOf(List.class);
    }

    @Nested
    class 비었는지_여부_변경_기능_테스트 {

        @Test
        void 테이블의_비었는지_여부를_변경한다() {
            // given
            OrderTable orderTable = new OrderTable(1, true);
            em.persist(orderTable);
            em.flush();
            em.clear();
            OrderTableChangeEmptyRequest request = new OrderTableChangeEmptyRequest(false);

            // when
            OrderTableResponse response = tableService.changeEmpty(orderTable.getId(), request);

            // then
            assertThat(response.getEmpty()).isFalse();
        }

        @Test
        void 변경하려는_테이블_id가_존재하지_않으면_예외를_반환한다() {
            // given
            OrderTableChangeEmptyRequest request = new OrderTableChangeEmptyRequest(false);

            // when, then
            assertThrows(OrderTableNotFoundException.class,
                    () -> tableService.changeEmpty(-1L, request));
        }

        @Disabled
        @Test
        void 변경하려는_테이블이_단체_테이블에_속해있다면_예외를_반환한다() {
            // TODO
        }

        @Test
        void 변경하려는_테이블의_주문_상태가_변경_불가하면_예외를_반환한다() {
            // given
            OrderTable orderTable = new OrderTable(1, false);
            Orders orders = new Orders(orderTable, OrderStatus.COOKING,
                    LocalDateTime.now());
            em.persist(orderTable);
            em.persist(orders);
            em.flush();
            em.clear();
            OrderTableChangeEmptyRequest request = new OrderTableChangeEmptyRequest(true);

            // when, then
            assertThrows(UnCompletedOrderExistsException.class,
                    () -> tableService.changeEmpty(orderTable.getId(), request));
        }
    }

    @Nested
    class 방문한_손님_수_변경_기능_테스트 {

        @Test
        void 방문한_손님_수를_변경한다() {
            // given
            int newNumberOfGuests = 4;
            OrderTable orderTable = new OrderTable(1, false);
            em.persist(orderTable);
            em.flush();
            em.clear();
            OrderTableChangeNumberOfGuestsRequest request = new OrderTableChangeNumberOfGuestsRequest(
                    newNumberOfGuests);

            // when
            OrderTableResponse response = tableService.changeNumberOfGuests(orderTable.getId(),
                    request);

            // then
            SoftAssertions.assertSoftly(softly -> {
                assertThat(response.getId()).isEqualTo(orderTable.getId());
                assertThat(response.getNumberOfGuests()).isEqualTo(newNumberOfGuests);
            });
        }

        @Test
        void 손님_수가_음수인_경우_예외를_반환한다() {
            // given
            OrderTableChangeNumberOfGuestsRequest request = new OrderTableChangeNumberOfGuestsRequest(
                    -1);

            // when, then
            assertThrows(InvalidRequestParameterException.class,
                    () -> tableService.changeNumberOfGuests(1L, request));
        }

        @Test
        void 손님_수를_변경하려는_테이블이_빈_테이블인_경우_예외를_반환한다() {
            // given
            OrderTable orderTable = new OrderTable(0, true);
            em.persist(orderTable);
            em.flush();
            em.clear();
            OrderTableChangeNumberOfGuestsRequest request = new OrderTableChangeNumberOfGuestsRequest(
                    1);

            // when, then
            assertThrows(CannotChangeEmptyTableNumberOfGuestsException.class,
                    () -> tableService.changeNumberOfGuests(orderTable.getId(), request));
        }
    }
}
