package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTablesTest {

    @DisplayName("OrderTables는 단체 지정을 위해 2개의 테이블 이상 설정해야한다.")
    @Test
    void constructor_validateSize() {
        // given
        final List<OrderTable> orderTables = List.of(new OrderTable(1, false));

        // when & then
        assertThatThrownBy(() -> new OrderTables(orderTables))
            .isInstanceOf(IllegalArgumentException.class);

        // then

    }

    @DisplayName("size 메서드는 테이블의 수를 반환한다.")
    @Test
    void size() {
        // given
        final List<OrderTable> orderTables = List.of(
            new OrderTable(1, false),
            new OrderTable(1, false),
            new OrderTable(1, false)
        );

        // when
        final OrderTables actual = new OrderTables(orderTables);

        // then
        assertThat(actual.size()).isEqualTo(3);
    }

    @DisplayName("getIds 메서드는 테이블들의 id를 반환한다.")
    @Test
    void getIds() {
        // given
        final OrderTables orderTables = new OrderTables(List.of(
            new OrderTable(1L, null, 1, false),
            new OrderTable(2L, null, 1, false),
            new OrderTable(3L, null, 1, false)
        ));

        // when
        final List<Long> actual = orderTables.getIds();

        // then
        assertThat(actual).containsExactly(1L, 2L, 3L);
    }

    @DisplayName("validateSameSize 메서드는 테이블의 수가 다르면 예외를 발생시킨다.")
    @Test
    void validateSameSize() {
        // given
        final OrderTables orderTables1 = new OrderTables(List.of(
            new OrderTable(1L, null, 1, false),
            new OrderTable(2L, null, 1, false),
            new OrderTable(3L, null, 1, false)
        ));

        final OrderTables orderTables2 = new OrderTables(List.of(
            new OrderTable(1L, null, 1, false),
            new OrderTable(2L, null, 1, false)
        ));

        // when & then
        assertThatThrownBy(() -> orderTables1.validateSameSize(orderTables2))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("validateNotGroupAll 지정한 모든 테이블을 단체 지정할 수 없으면 예외를 발생시킨다.")
    @Test
    void validateNotGroupAll() {
        // given
        final OrderTables orderTables = new OrderTables(List.of(
            new OrderTable(1L, 1L, 1, false),
            new OrderTable(2L, null, 1, false),
            new OrderTable(3L, null, 1, false)
        ));

        // when & then
        assertThatThrownBy(orderTables::validateNotGroupAll)
            .isInstanceOf(IllegalArgumentException.class);
    }
}
