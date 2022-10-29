package kitchenpos.domain;

import static kitchenpos.fixture.TableFixture.getOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Test
    @DisplayName("주문 테이블의 개수가 2개 미만이면 예외가 발생한다.")
    void createWithInvalidSize() {
        assertThatThrownBy(() -> TableGroup.of(1L, LocalDateTime.now(), Arrays.asList(getOrderTable())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 2개 이상이어야 합니다.");
    }

    @Test
    @DisplayName("주문 테이블이 없이 테이블 그룹을 생성하면 예외가 발생한다.")
    void createWithInvalidEmptyOrderTables() {
        assertThatThrownBy(() -> TableGroup.of(1L, LocalDateTime.now(), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블 없이 테이블 그룹을 생성할 수 없습니다.");
    }

    @Test
    @DisplayName("이미 테이블 그룹이 있는 주문 테이블로 테이블 그룹을 생성하면 예외가 발생한다.")
    void createWithTableGroupOrderTables() {
        // given
        final List<OrderTable> orderTables = Arrays.asList(getOrderTable(1L, true), getOrderTable(1L, true));
        assertThatThrownBy(() -> TableGroup.of(1L, LocalDateTime.now(), orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 테이블 그룹을 가진 주문 테이블은 테이블 그룹을 생성할 수 없습니다.");
    }

    @Test
    @DisplayName("비어있지 않은 주문 테이블로 테이블 그룹을 생성하면 예외가 발생한다.")
    void createWithNotEmptyOrderTables() {
        // given
        final List<OrderTable> orderTables = Arrays.asList(getOrderTable(null, false), getOrderTable(null, false));
        assertThatThrownBy(() -> TableGroup.of(1L, LocalDateTime.now(), orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 비어있지 않으면 테이블 그룹을 생성할 수 없습니다.");
    }

    @Test
    @DisplayName("테이블 그룹을 생성하면 테이블 그룹에 속한 테이블은 테이블 그룹 id를 가져야 한다.")
    void createThenOrderTableHaveTableGroupId() {
        final List<OrderTable> orderTables = Arrays.asList(getOrderTable(null, true), getOrderTable(null, true));
        final TableGroup tableGroup = TableGroup.of(1L, LocalDateTime.now(), orderTables);
        final List<OrderTable> actualOrdeTables = tableGroup.getOrderTables();
        assertAll(
                () -> assertThat(actualOrdeTables.get(0).getTableGroupId()).isEqualTo(1L),
                () -> assertThat(actualOrdeTables.get(1).getTableGroupId()).isEqualTo(1L)
        );
    }
}