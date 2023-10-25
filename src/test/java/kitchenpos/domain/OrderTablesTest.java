package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

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

        @DisplayName("[SUCCESS] 주문 테이블을 추가한다.")
        @Test
        void success_addOrderTables() {
            // given
            final OrderTables actual = OrderTables.empty();

            final OrderTable orderTable = OrderTable.withoutTableGroup(5, false);

            // when
            actual.addOrderTables(new OrderTables(List.of(orderTable)));

            // then
            final List<OrderTable> expected = actual.getOrderTableItems();

            assertThat(actual.getOrderTableItems()).isEqualTo(expected);
        }
    }

    @DisplayName("단체 지정")
    @Nested
    class AssignTableGroupNestedClass {

        @DisplayName("[SUCCESS] 주문 테이블 목록 객체를 단체로 지정한다.")
        @Test
        void success_assignTableGroup() {
            // given
            final TableGroup tableGroup = TableGroup.withOrderTables(List.of(
                    OrderTable.withoutTableGroup(5, true),
                    OrderTable.withoutTableGroup(5, true)
            ));

            // when
            final OrderTables actual = new OrderTables(List.of(
                    OrderTable.withoutTableGroup(5, true)
            ));
            actual.assignTableGroup(tableGroup);

            // then
            final boolean isAllGrouped = actual.getOrderTableItems().stream().allMatch(OrderTable::isGrouped);

            assertThat(isAllGrouped).isTrue();
        }
    }
}
