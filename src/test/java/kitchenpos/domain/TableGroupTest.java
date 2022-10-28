package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("TableGroup 도메인 테스트")
class TableGroupTest {

    @DisplayName("주문 테이블의 수가 2이상이어야 한다")
    @Test
    void numberOfOrderTableIsLowerTwo() {
        assertThatThrownBy(() -> new TableGroup(Collections.emptyList()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("테이블의 수가 2개 이상이어야 합니다.");
    }
}
