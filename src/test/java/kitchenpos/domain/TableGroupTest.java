package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Test
    @DisplayName("테이블 개수가 2개 미만인 경우 예외를 발생시킨다.")
    void orderTablesLessThanTwo() {
        assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(), Arrays.asList(new OrderTable(1, true))))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("테이블 그룹의 개수는 2개 이상이어야 합니다.");
    }
}