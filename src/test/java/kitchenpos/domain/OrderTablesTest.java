package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("OrderTables의")
class OrderTablesTest {

    @Nested
    @DisplayName("생성자는")
    class Constructor {

        @Test
        @DisplayName("OrderTable 목록을 받으면, OrderTables를 반환한다.")
        void success() {
            //given
            List<OrderTable> orderTables = Arrays.asList(new OrderTable(1L, null, 0, true),
                    new OrderTable(2L, null, 0, true));

            //when
            OrderTables actual = new OrderTables(orderTables);

            //then
            assertThat(actual.getValue())
                    .containsAll(orderTables);
        }

        @Test
        @DisplayName("빈 OrderTable 목록을 받으면, 예외를 던진다.")
        void fail_emptyList() {
            //given
            List<OrderTable> orderTables = Collections.emptyList();

            //when & then
            assertThatThrownBy(() -> new OrderTables(orderTables))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("2개 이하의 OrderTable 목록을 받으면, 예외를 던진다.")
        void fail_shortList() {
            //given
            List<OrderTable> orderTables = Collections.singletonList(new OrderTable(1L, null, 0, true));

            //when & then
            assertThatThrownBy(() -> new OrderTables(orderTables))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
