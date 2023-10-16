package kitchenpos.application;

import kitchenpos.ServiceTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
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
            final OrderTable orderTable = new OrderTable();
            orderTable.setNumberOfGuests(0);
            orderTable.setEmpty(true);

            //when
            final OrderTable actual = tableService.create(orderTable);

            //then
            assertSoftly(softly -> {
                softly.assertThat(actual.getId()).isNotNull();
                softly.assertThat(actual.getTableGroupId()).isNull();
                softly.assertThat(actual.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
                softly.assertThat(actual.isEmpty()).isEqualTo(orderTable.isEmpty());
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
            OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(true);
            orderTable.setNumberOfGuests(0);
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
        void orderTableChangeEmpty(final boolean isEmpty) {
            //given
            OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(isEmpty);
            orderTable.setTableGroupId(null);
            orderTable.setNumberOfGuests(0);
            orderTable = testFixtureBuilder.buildOrderTable(orderTable);

            final OrderTable changingStatus = new OrderTable();
            changingStatus.setEmpty(!isEmpty);

            //when
            final OrderTable actual = tableService.changeEmpty(orderTable.getId(), changingStatus);

            //then
            assertSoftly(softly -> {
                softly.assertThat(actual.isEmpty()).isEqualTo(changingStatus.isEmpty());
            });
        }

        @DisplayName("주문 테이블이 비어있는지를 변경할 때 주문 테이블이 없으면 실패한다.")
        @Test
        void orderTableChangeEmptyFailWhenNotExistTableGroup() {
            //given
            final long notExistOrderTableId = -1L;
            final OrderTable changingStatus = new OrderTable();
            changingStatus.setEmpty(true);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(notExistOrderTableId, changingStatus))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블이 비어있는지를 변경할 때 단체 지정 id가 null이 아니면 실패한다.")
        @Test
        void orderTableChangeEmptyFailWhenTableGroupIdIsNotNull() {
            //given
            TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(Collections.emptyList());
            tableGroup.setCreatedDate(LocalDateTime.now());
            tableGroup = testFixtureBuilder.buildTableGroup(tableGroup);

            OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(true);
            orderTable.setTableGroupId(tableGroup.getId());
            orderTable.setNumberOfGuests(0);
            orderTable = testFixtureBuilder.buildOrderTable(orderTable);

            final OrderTable changingStatus = new OrderTable();
            changingStatus.setEmpty(!orderTable.isEmpty());

            // when & then
            final Long orderTableId = orderTable.getId();
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, changingStatus))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블이 비어있는지를 변경할 때 주문 상태가 조리나 식사면 실패한다.")
        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "MEAL"})
        void orderTableChangeEmptyFailWhenOrderStatusNotCompletion(final String orderStatus) {
            //given
            OrderTable notCompletionOrdertable = new OrderTable();
            notCompletionOrdertable.setEmpty(false);
            notCompletionOrdertable.setTableGroupId(null);
            notCompletionOrdertable.setNumberOfGuests(3);
            notCompletionOrdertable = testFixtureBuilder.buildOrderTable(notCompletionOrdertable);

            final Order notCompletionOrder = new Order();
            notCompletionOrder.setOrderStatus(orderStatus);
            notCompletionOrder.setOrderedTime(LocalDateTime.now());
            notCompletionOrder.setOrderTableId(notCompletionOrdertable.getId());
            testFixtureBuilder.buildOrder(notCompletionOrder);

            final OrderTable changingStatus = new OrderTable();
            changingStatus.setEmpty(!notCompletionOrdertable.isEmpty());

            // when & then
            final Long orderTableId = notCompletionOrdertable.getId();
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, changingStatus))
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
            OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(false);
            final int beforeGuests = 10;
            orderTable.setNumberOfGuests(beforeGuests);
            orderTable = testFixtureBuilder.buildOrderTable(orderTable);

            final OrderTable afterGuests = new OrderTable();
            afterGuests.setNumberOfGuests(beforeGuests + 100);

            //when
            final OrderTable actual = tableService.changeNumberOfGuests(orderTable.getId(), afterGuests);

            //then
            assertSoftly(softly -> {
                softly.assertThat(actual.getNumberOfGuests()).isEqualTo(afterGuests.getNumberOfGuests());
            });
        }

        @DisplayName("변경할 고객 수가 음수면 실패한다.")
        @Test
        void orderTableChangeNumberOfGuestsFailWhenChangingGuestsLessThenZero() {
            //given
            OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(false);
            orderTable.setNumberOfGuests(3);
            orderTable = testFixtureBuilder.buildOrderTable(orderTable);

            final OrderTable guestsLessThenZero = new OrderTable();
            guestsLessThenZero.setNumberOfGuests(-1);

            // when & then
            final Long orderTableId = orderTable.getId();
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, guestsLessThenZero))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("변경할 주문 테이블이 존재하지 않으면 실패한다.")
        @Test
        void orderTableChangeNumberOfGuestsFailWhenNotExistOrderTable() {
            //given
            OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(true);
            orderTable.setNumberOfGuests(3);
            orderTable.setId(-1L);

            final OrderTable changingGuests = new OrderTable();
            changingGuests.setNumberOfGuests(5);

            // when & then
            final Long orderTableId = orderTable.getId();
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, changingGuests))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블이 비어있으면 실패한다.")
        @Test
        void orderTableChangeNumberOfGuestsFailWhenOrderTableIsEmpty() {
            //given
            OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(true);
            orderTable.setNumberOfGuests(3);
            orderTable = testFixtureBuilder.buildOrderTable(orderTable);

            final OrderTable changingGuests = new OrderTable();
            changingGuests.setNumberOfGuests(5);

            // when & then
            final Long orderTableId = orderTable.getId();
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, changingGuests))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
