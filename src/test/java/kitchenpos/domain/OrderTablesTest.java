package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("OrderTables 도메인 테스트")
class OrderTablesTest {

    @DisplayName("주문 테이블의 수가 최솟값보다 커야 한다")
    @Test
    void numberOfOrderTableIsLowerMinimum() {
        assertThatThrownBy(() -> new TableGroup(Collections.emptyList()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("테이블의 수가 2개 이상이어야 합니다.");
    }

}
