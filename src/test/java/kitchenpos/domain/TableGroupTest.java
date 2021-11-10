package kitchenpos.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.fixture.OrderTableFixture.단일_손님0_테이블1;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("TableGroup 단위 테스트")
class TableGroupTest {

    @Test
    @DisplayName("그룹을 지정하려면 둘 이상의 테이블이 필요하다")
    void addOrderTables() {
        // given
        TableGroup group = new TableGroup();
        List<OrderTable> orderTables = Arrays.asList(
                new OrderTable(0, true),
                new OrderTable(0, true),
                new OrderTable(0, true)
        );

        // when
        group.addOrderTables(orderTables);
        List<OrderTable> results = group.getOrderTables();

        // then
        assertEquals(3, results.size());
    }

    @Test
    @DisplayName("테이블 그룹에 포함하려는 테이블 목록이 둘 미만이면 그룹을 생성할 수 없다.")
    void lessThanTwo() {
        // given
        TableGroup group = new TableGroup();

        // when & then
        assertThatThrownBy(() -> group.addOrderTables(Collections.singletonList(단일_손님0_테이블1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그룹을 지정하려면 둘 이상의 테이블이 필요합니다.");
    }
}
