package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class TableGroupTest {

    @Nested
    @DisplayName("create()")
    class CreateMethod {

    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("테이블 정보가 비어있거나 null인 경우 예외가 발생한다.")
    void nullAndEmptyOrderTables(List<OrderTable> orderTables) {
        assertThatThrownBy(() -> new TableGroup(orderTables))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("2개 이상의 테이블을 그룹으로 추가할 수 있습니다.");
    }

    @Test
    @DisplayName("테이블 정보가 2개 미만인 경우 예외가 발생한다.")
    void oneTable() {
        // given
        List<OrderTable> orderTables = new ArrayList<OrderTable>() {{
            add(new OrderTable(10, false));
        }};

        // when, then
        assertThatThrownBy(() -> new TableGroup(orderTables))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("2개 이상의 테이블을 그룹으로 추가할 수 있습니다.");
    }

}
