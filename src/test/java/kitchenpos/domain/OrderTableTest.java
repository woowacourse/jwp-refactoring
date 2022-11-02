package kitchenpos.domain;

import static kitchenpos.support.DomainFixture.빈_테이블_생성;
import static kitchenpos.support.DomainFixture.한개;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.Empty;
import kitchenpos.domain.table.GuestNumber;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableStatus;
import kitchenpos.exception.CustomError;
import kitchenpos.exception.DomainLogicException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @DisplayName("비어있는지 여부 변경 테스트")
    @Nested
    class ChangeEmptyTest {

        @Test
        void 비어있지_않음으로_변경한다() {
            // given
            final var table = new OrderTable(new TableStatus(new Empty(true), new GuestNumber(0)));

            // when
            table.changeEmpty(false);

            // then
            assertThat(table.isEmpty()).isFalse();
        }

        @Test
        void 테이블의_주문_중_계산_완료가_아닌_주문이_있는_경우_예외를_던진다() {
            // given
            final var orderTable = new OrderTable(new TableStatus(new Empty(true), new GuestNumber(0)),
                    List.of(new Order(1L, OrderStatus.COOKING, LocalDateTime.now(), List.of(new OrderLineItem(1L, 한개)))));

            // when & then
            assertThatThrownBy(() -> orderTable.changeEmpty(false))
                    .isInstanceOf(DomainLogicException.class)
                    .extracting("errorCode")
                    .isEqualTo(CustomError.UNCOMPLETED_ORDER_IN_TABLE_ERROR);
        }
    }

    @Test
    void 테이블의_방문자수를_변경한다() {
        // given
        final var table = 빈_테이블_생성();
        table.changeEmpty(false);

        // when
        table.changeGuestNumber(5);

        // then
        assertThat(table.getGuestNumber()).isEqualTo(5);
    }
}
