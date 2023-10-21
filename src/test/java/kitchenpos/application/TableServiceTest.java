package kitchenpos.application;

import kitchenpos.ServiceTest;
import kitchenpos.order.domain.Order;
import kitchenpos.table.application.TableService;
import kitchenpos.table.application.dto.OrderTableCreateRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class TableServiceTest extends ServiceTest {

    @Autowired
    TableService tableService;

    @DisplayName("주문 테이블 생성 테스트")
    @Nested
    class TableCreateTest {

        @DisplayName("주문 테이블을 생성한다.")
        @Test
        void orderTableCreate() {
            //given
            final OrderTableCreateRequest request = new OrderTableCreateRequest(3, false);

            //when
            final Long id = tableService.create(request);

            //then
            assertSoftly(softly -> {
                softly.assertThat(id).isNotNull();
            });
        }
    }

    @DisplayName("주문 테이블 조회 테스트")
    @Nested
    class OrderTableFindTest {

        @DisplayName("전체 주문 테이블을 조회한다.")
        @Test
        void orderTableFindAll() {
            //given
            OrderTable orderTable = new OrderTable(null, 0, true);
            orderTable = testFixtureBuilder.buildOrderTable(orderTable);

            //when
            final List<OrderTable> actual = tableService.list();

            //then
            final Long orderTableId = orderTable.getId();
            assertSoftly(softly -> {
                softly.assertThat(actual.size()).isEqualTo(1);
                softly.assertThat(actual.get(0).getId()).isEqualTo(orderTableId);
            });
        }
    }


    @DisplayName("주문 테이블 비어있는지 변경 테스트")
    @Nested
    class OrderTableChangeEmptyTest {

        @DisplayName("주문 테이블이 비어있는지를 변경한다.")
        @ParameterizedTest
        @ValueSource(booleans = {true, false})
        void orderTableChangeEmpty(final boolean bool) {
            //given
            OrderTable orderTable = new OrderTable(null, 0, bool);
            orderTable = testFixtureBuilder.buildOrderTable(orderTable);

            //when
            final Long id = tableService.changeEmpty(orderTable.getId(), !bool);

            //then
            final OrderTable savedOrderTable = testFixtureBuilder.getEntitySupporter().getOrderTableRepository().findById(id).orElseThrow(IllegalArgumentException::new);
            assertSoftly(softly -> {
                softly.assertThat(savedOrderTable.isEmpty()).isEqualTo(!bool);
            });
        }

        @DisplayName("주문 테이블이 비어있는지를 변경할 때 주문 테이블이 없으면 실패한다.")
        @Test
        void orderTableChangeEmptyFailWhenNotExistTableGroup() {
            //given
            final long notExistOrderTableId = -1L;

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(notExistOrderTableId, true))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블이 비어있는지를 변경할 때 단체 지정 id가 null이 아니면 실패한다.")
        @Test
        void orderTableChangeEmptyFailWhenTableGroupIdIsNotNull() {
            //given
            OrderTable orderTable1 = new OrderTable(null, 0, true);
            orderTable1 = testFixtureBuilder.buildOrderTable(orderTable1);

            OrderTable orderTable2 = new OrderTable(null, 0, true);
            orderTable2 = testFixtureBuilder.buildOrderTable(orderTable2);

            final List<OrderTable> orderTables = List.of(orderTable1, orderTable2);
            TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);
            testFixtureBuilder.buildTableGroup(tableGroup);
            orderTables.forEach(orderTable -> testFixtureBuilder.buildOrderTable(orderTable));

            // when & then
            final Long orderTableId = orderTable1.getId();
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, true))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블이 비어있는지를 변경할 때 주문 상태가 조리나 식사면 실패한다.")
        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "MEAL"})
        void orderTableChangeEmptyFailWhenOrderStatusNotCompletion(final String orderStatus) {
            //given
            OrderTable notCompletionOrdertable = new OrderTable(null, 3, false);
            notCompletionOrdertable = testFixtureBuilder.buildOrderTable(notCompletionOrdertable);

            final Order notCompletionOrder = new Order(notCompletionOrdertable, orderStatus, LocalDateTime.now(), Collections.emptyList());
            testFixtureBuilder.buildOrder(notCompletionOrder);

            final OrderTable changingStatus = new OrderTable();
            changingStatus.changeEmpty(!notCompletionOrdertable.isEmpty());

            // when & then
            final Long orderTableId = notCompletionOrdertable.getId();
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, true))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

    @DisplayName("주문 테이블 고객 수 변경")
    @Nested
    class orderTableChangeNumberOfGuestsTest {

        @DisplayName("주문 테이블의 고객 수를 변경한다.")
        @Test
        void orderTableChangeNumberOfGuests() {
            //given
            final int beforeGuests = 10;
            OrderTable orderTable = new OrderTable(null, beforeGuests, false);
            orderTable = testFixtureBuilder.buildOrderTable(orderTable);

            final int afterGuests = beforeGuests + 100;
            //when
            final Long id = tableService.changeNumberOfGuests(orderTable.getId(), afterGuests);

            //then
            final OrderTable savedOrderTable = testFixtureBuilder.getEntitySupporter().getOrderTableRepository().findById(id).orElseThrow(IllegalArgumentException::new);
            assertSoftly(softly -> {
                softly.assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(afterGuests);
            });
        }

        @DisplayName("변경할 고객 수가 음수면 실패한다.")
        @Test
        void orderTableChangeNumberOfGuestsFailWhenChangingGuestsLessThenZero() {
            //given
            OrderTable orderTable = new OrderTable(null, 3, false);
            orderTable = testFixtureBuilder.buildOrderTable(orderTable);

            // when & then
            final Long orderTableId = orderTable.getId();
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, -1))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("변경할 주문 테이블이 존재하지 않으면 실패한다.")
        @Test
        void orderTableChangeNumberOfGuestsFailWhenNotExistOrderTable() {
            //given
            final Long notExistOrderTableId = -1L;

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(notExistOrderTableId, 5))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블이 비어있으면 실패한다.")
        @Test
        void orderTableChangeNumberOfGuestsFailWhenOrderTableIsEmpty() {
            //given
            OrderTable orderTable = new OrderTable(null, 3, true);
            orderTable = testFixtureBuilder.buildOrderTable(orderTable);

            // when & then
            final Long orderTableId = orderTable.getId();
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, 5))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
