package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTablesTest {

    @DisplayName("주문 테이블 목록 객체 생성")
    @Nested
    class CreateNestedTest {

        @DisplayName("[SUCCESS] 생성한다.")
        @Test
        void success_create() {
            assertThatCode(() -> new OrderTables(new ArrayList<>()))
                    .doesNotThrowAnyException();
        }

        @DisplayName("[SUCCESS] 비어있는 주문 테이블 목록 객체를 생성한다.")
        @Test
        void success_create_empty() {
            assertThatCode(() -> OrderTables.empty())
                    .doesNotThrowAnyException();
        }
    }

    @DisplayName("주문 테이블 목록 추가")
    @Nested
    class AddOrderTablesNestedClass {

        @DisplayName("[EXCEPTION] 주문 테이블 목록에 추가된 주문 테이블 중 하나라도 단체 지정되어 있을 경우 예외가 발생한다.")
        @Test
        void throwException_when_orderTables_anyMatch_tableGroupIsExists() {
            // given
            final OrderTable orderTable = new OrderTable(TableGroup.emptyOrderTables(), 5, false);
            final OrderTables orderTables = new OrderTables(List.of(orderTable));

            // expect
            assertThatThrownBy(() -> OrderTables.empty().addOrderTables(orderTables))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("단체 지정")
    @Nested
    class AssignTableGroupNestedClass {

        @DisplayName("[SUCCESS] 비어있는 주문 테이블 목록 객체를 생성한다.")
        @Test
        void success_assignTableGroup() {
            // given
            final List<OrderTable> orderTableItems = List.of(
                    new OrderTable(null, 5, true),
                    new OrderTable(null, 5, true)
            );

            final OrderTables actual = new OrderTables(orderTableItems);

            // when
            final TableGroup expected = TableGroup.emptyOrderTables();
            actual.assignTableGroup(expected);

            // then
            assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(new OrderTables(List.of(
                            new OrderTable(expected, 5, true),
                            new OrderTable(expected, 5, true)
                    )));
        }
    }
}
