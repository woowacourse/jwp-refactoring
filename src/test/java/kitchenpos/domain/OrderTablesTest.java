package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


class OrderTablesTest {

    @DisplayName("일급컬렉션 객체 생성: OrderTable의 일급 컬렉션 객체를 생성한다.")
    @Test
    void create() {
        // given
        List<OrderTable> orderTableList = Arrays.asList(
                new OrderTable(1L, null, 0, false),
                new OrderTable(2L, null, 0, false)
        );

        // when
        OrderTables orderTables = new OrderTables(orderTableList);

        // then
        assertThat(orderTables.getValues()).hasSize(orderTableList.size());
    }

    @DisplayName("그룹화 검증: 테이블 그룹화를 위해 필요한 테이블은 2개 이상이다.")
    @Test
    void validateGroupingNumbers() {
        // given
        List<OrderTable> orderTableList = Arrays.asList(
                new OrderTable(1L, null, 0, false),
                new OrderTable(2L, null, 0, false)
        );
        OrderTables orderTables = new OrderTables(orderTableList);
        OrderTables orderTables2 = new OrderTables(Collections.emptyList());

        // when
        assertDoesNotThrow(orderTables::validateGroupingNumbers);
    }

    @DisplayName("그룹화 검증: 테이블 그룹화하려는 테이블 수가 0이거나 2 이하라면 예외가 발생한다.")
    @Test
    void validateGroupingNumbersThrowCase() {
        // given
        List<OrderTable> orderTableList = Collections.singletonList(
                new OrderTable(1L, null, 0, false)
        );
        OrderTables orderTables1 = new OrderTables(orderTableList);
        OrderTables orderTables2 = new OrderTables(Collections.emptyList());

        // when
        assertThatThrownBy(orderTables1::validateGroupingNumbers)
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(orderTables2::validateGroupingNumbers)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("getOrderTableIds(): 테이블들의 id를 추출해 리스트로 반환한다.")
    @Test
    void getOrderTableIds() {
        // given
        List<OrderTable> orderTableList = Arrays.asList(
                new OrderTable(1L, null, 0, false),
                new OrderTable(2L, null, 0, false)
        );
        OrderTables orderTables = new OrderTables(orderTableList);

        // when
        List<Long> orderTableIds = orderTables.getOrderTableIds();

        // then
        assertThat(orderTableIds).hasSize(2);
        assertThat(orderTableIds).contains(1L, 2L);
    }

    @DisplayName("두 일급컬렉션의 크기가 다른지 여부를 반환한다.")
    @Test
    void isDifferentSize() {
        // given
        List<OrderTable> orderTableList = Arrays.asList(
                new OrderTable(1L, null, 0, false),
                new OrderTable(2L, null, 0, false)
        );
        OrderTables twoOrderTables1 = new OrderTables(orderTableList);
        OrderTables twoOrderTables2 = new OrderTables(orderTableList);
        OrderTables emptyOrderTables = new OrderTables(Collections.emptyList());

        // when
        boolean result1 = twoOrderTables1.isDifferentSize(twoOrderTables2);
        boolean result2 = twoOrderTables1.isDifferentSize(emptyOrderTables);

        // then
        assertThat(result1).isFalse();
        assertThat(result2).isTrue();
    }

    @DisplayName("테이블들이 그룹화가 가능한 상태인지 검증한다.")
    @Test
    void validateGroupingTables() {
        // given
        List<OrderTable> orderTableList = Arrays.asList(
                new OrderTable(1L, null, 0, true),
                new OrderTable(2L, null, 0, true)
        );
        OrderTables orderTables = new OrderTables(orderTableList);

        // when
        assertDoesNotThrow(orderTables::validateGroupingTables);
    }

    @DisplayName("테이블들 중 그룹화가 불가능한 테이블이 있으면 예외가 발생한다.")
    @Test
    void validateGroupingTablesThrowCase() {
        // given
        List<OrderTable> orderTableList = Arrays.asList(
                new OrderTable(1L, null, 4, false),
                new OrderTable(2L, null, 0, true)
        );
        OrderTables orderTables = new OrderTables(orderTableList);

        // when
        assertThatThrownBy(orderTables::validateGroupingTables)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블들을 그룹화 하거나 해제할 수 있다.")
    @Test
    void groupingTables() {
        // given
        OrderTables orderTables = new OrderTables(Arrays.asList(
                new OrderTable(1L, null, 0, true),
                new OrderTable(2L, null, 0, true)
        ));
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);

        // when
        orderTables.groupingTables(tableGroup.getId());

        // then
        assertThat(orderTables.getValues()).extracting("tableGroupId").contains(1L);

        // when
        orderTables.ungroupTables();

        // then
        assertThat(orderTables.getValues()).extracting("tableGroupId").containsNull();
    }
}