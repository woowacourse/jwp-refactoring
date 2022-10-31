package kitchenpos.domain;

import static kitchenpos.support.DomainFixture.한개;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.exception.CustomErrorCode;
import kitchenpos.exception.DomainLogicException;
import kitchenpos.domain.Empty;
import kitchenpos.domain.GuestNumber;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderTableGroupTest {

    @DisplayName("단체 지정 생성 테스트")
    @Nested
    class CreateTest {

        @Test
        void 단체_지정하는_테이블의_수가_2보다_작으면_예외를_던진다() {
            // given
            final var table = new OrderTable(new TableStatus(new Empty(true), new GuestNumber(0)));

            // when & then
            assertThatThrownBy(() -> new TableGroup(List.of(table), LocalDateTime.now()))
                    .isInstanceOf(DomainLogicException.class)
                    .extracting("errorCode")
                    .isEqualTo(CustomErrorCode.TABLE_GROUP_MIN_TABLES_ERROR);
        }

        @Test
        void 단체_지정하는_테이블이_비어있지_않은_경우_예외를_던진다() {
            // given
            final var table = new OrderTable(new TableStatus(new Empty(false), new GuestNumber(0)));

            // when & then
            assertThatThrownBy(() -> new TableGroup(List.of(table, table), LocalDateTime.now()))
                    .isInstanceOf(DomainLogicException.class)
                    .extracting("errorCode")
                    .isEqualTo(CustomErrorCode.TABLE_GROUP_TABLE_NOT_EMPTY_ERROR);
        }

        @Test
        void 단체_지정하는_테이블이_이미_단체_지정되어_있는_경우_예외를_던진다() {
            // given
            final var tableA = new OrderTable(new TableStatus(new Empty(true), new GuestNumber(0)));
            final var tableB = new OrderTable(new TableStatus(new Empty(true), new GuestNumber(0)));
            new TableGroup(List.of(tableA, tableB), LocalDateTime.now());

            // when & then
            assertThatThrownBy(() -> new TableGroup(List.of(tableA, tableB), LocalDateTime.now()))
                    .isInstanceOf(DomainLogicException.class)
                    .extracting("errorCode")
                    .isEqualTo(CustomErrorCode.TABLE_ALREADY_GROUPED_ERROR);
        }
    }
    
    @DisplayName("단체 지정 삭제 테스트")
    @Nested
    class UngroupTest {

        @Test
        void 단체_지정을_삭제한다() {
            // given
            final var tableA = new OrderTable(new TableStatus(new Empty(true), new GuestNumber(0)));
            final var tableB = new OrderTable(new TableStatus(new Empty(true), new GuestNumber(0)));
            final var group = new TableGroup(List.of(tableA, tableB), LocalDateTime.now());

            // when
            group.ungroup();

            // then
            assertAll(
                    () -> assertThat(tableA.isGrouped()).isFalse(),
                    () -> assertThat(tableB.isGrouped()).isFalse()
            );
        }

        @Test
        void 계산이_완료되지않은_주문이_있는_경우_예외를_던진다() {
            // given
            final var tableA = new OrderTable(null, null, new TableStatus(new Empty(true), new GuestNumber(0)), List.of(new Order(
                    OrderStatus.COOKING, LocalDateTime.now(), List.of(new OrderLineItem(1L, 한개)))));
            final var tableB = new OrderTable(null, null, new TableStatus(new Empty(true), new GuestNumber(0)), List.of(new Order(OrderStatus.COMPLETION, LocalDateTime.now(), List.of(new OrderLineItem(1L, 한개)))));
            final var group = new TableGroup(List.of(tableA, tableB), LocalDateTime.now());

            // when & then
            assertThatThrownBy(group::ungroup)
                    .isInstanceOf(DomainLogicException.class)
                    .extracting("errorCode")
                    .isEqualTo(CustomErrorCode.TABLE_GROUP_UNGROUP_NOT_COMPLETED_ORDER);
        }
    }
}
