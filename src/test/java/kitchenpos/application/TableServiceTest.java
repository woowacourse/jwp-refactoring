package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.product.domain.Product;
import kitchenpos.table.ui.dto.ChangeEmptyRequest;
import kitchenpos.table.ui.dto.ChangeNumberOfGuestsRequest;
import kitchenpos.table.ui.dto.OrderTableCreateRequest;
import kitchenpos.table.ui.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("TableService의")
class TableServiceTest extends ServiceTest {

    @Nested
    @DisplayName("create 메서드는")
    class Create {

        @ParameterizedTest
        @CsvSource(value = {"0,true", "1,false", "1,true"})
        @DisplayName("주문 테이블을 등록할 수 있다.")
        void create_validOrderTable_success(final int numberOfGuests, final boolean empty) {
            // given
            final OrderTableCreateRequest request = new OrderTableCreateRequest(numberOfGuests, empty);

            // when
            final OrderTableResponse actual = tableService.create(request);

            // then
            softly.assertThat(actual.getTableGroupId()).isNull();
            softly.assertThat(actual.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
            softly.assertThat(actual.isEmpty()).isEqualTo(request.isEmpty());
            softly.assertAll();
        }
    }

    @Nested
    @DisplayName("list 메서드는")
    class ListTest {

        @Test
        @DisplayName("주문 테이블 목록을 조회할 수 있다.")
        void list_savedOrderTables_success() {
            // given
            final OrderTable orderTable1 = saveOrderTable(777, true);
            final OrderTable orderTable2 = saveOrderTable(999, false);

            // when
            final List<OrderTableResponse> actual = tableService.list();

            // then
            assertThat(actual).extracting(OrderTableResponse::getNumberOfGuests, OrderTableResponse::isEmpty)
                    .containsExactly(
                            tuple(orderTable1.getNumberOfGuests(), orderTable1.isEmpty()),
                            tuple(orderTable2.getNumberOfGuests(), orderTable2.isEmpty())
                    );
        }
    }

    @Nested
    @DisplayName("changeEmpty 메서드는")
    class ChangeEmpty {

        @Test
        @DisplayName("주문 등록 가능 여부를 변경한다.")
        void changeEmpty_validOrderTable_success() {
            // given
            final Long orderTableId = saveOrderTable(10, false).getId();
            final ChangeEmptyRequest request = new ChangeEmptyRequest(true);
            // when
            final OrderTableResponse actual = tableService.changeEmpty(orderTableId, request);

            // then
            assertThat(actual.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("주문 테이블이 존재하지 않으면 주문 등록 가능 여부를 변경할 수 없다.")
        void changeEmpty_orderTableNotExist_exception() {
            // given
            final ChangeEmptyRequest request = new ChangeEmptyRequest(true);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(999L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest(name = "주문 상태: {0}")
        @DisplayName("주문 테이블에 주문이 있다면 주문 상태가 계산 완료인 경우에만 주문 등록 가능 여부를 변경할 수 있다.")
        @ValueSource(strings = {"COOKING", "MEAL"})
        void changeEmpty_orderStatusIsCompletion_exception(final String orderStatus) {
            // given
            final Product product = saveProduct("감자튀김");
            final MenuGroup menuGroup = saveMenuGroup("감자");
            final Menu menu = saveMenu("감자세트", BigDecimal.ONE, menuGroup, new MenuProduct(product.getId(), 1L));
            final OrderTable orderTable = saveOrderTable(10, false);
            saveOrder(orderTable, orderStatus, new OrderLineItem(1L, OrderMenu.from(menu)));

            final ChangeEmptyRequest request = new ChangeEmptyRequest(true);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("단체 지정된 주문 테이블은 주문 등록 가능 여부를 변경할 수 없다.")
        void changeEmpty_inTableGroup_exception() {
            // given
            final OrderTable orderTable = saveOrderTable(10, true);
            final OrderTable otherOrderTable = saveOrderTable(7, false);

            saveTableGroup(orderTable, otherOrderTable);

            final ChangeEmptyRequest request = new ChangeEmptyRequest(true);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("changeNumberOfGuests 메서드는")
    class ChangeNumberOfGuests {

        @Test
        @DisplayName("방문한 손님 수를 변경한다.")
        void changeNumberOfGuests_validNumberOfGuests_success() {
            // given
            final Long orderTableId = saveOrderTable(1, false).getId();

            final ChangeNumberOfGuestsRequest request = new ChangeNumberOfGuestsRequest(10);

            // when
            final OrderTableResponse actual = tableService.changeNumberOfGuests(orderTableId, request);

            // then
            assertThat(actual.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
        }

        @Test
        @DisplayName("방문한 손님 수는 음수일 수 없다.")
        void changeNumberOfGuests_numberOfGuestsIsNegative_exception() {
            // given
            final Long orderTableId = saveOrderTable(1, false).getId();

            final ChangeNumberOfGuestsRequest request = new ChangeNumberOfGuestsRequest(-1);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("존재하지 않는 주문 테이블은 변경할 수 없다.")
        void changeNumberOfGuests_notExistOrderTable_exception() {
            // given
            final ChangeNumberOfGuestsRequest request = new ChangeNumberOfGuestsRequest(10);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(999L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("빈 주문 테이블의 방문한 손님 수는 변경할 수 없다.")
        void changeNumberOfGuests_emptyTable_exception() {
            // given
            final Long orderTableId = saveOrderTable(1, true).getId();

            final ChangeNumberOfGuestsRequest request = new ChangeNumberOfGuestsRequest(10);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
