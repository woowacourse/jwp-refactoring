package kitchenpos.dto.request;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupRequestTest {

    @DisplayName("테이블 Id 목록이 null이면 예외 처리한다.")
    @Test
    void validateTableGroup() {
        // when & then
        assertThatThrownBy(() -> new TableGroupRequest(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블의 수가 올바르지 않습니다.");
    }

    @DisplayName("테이블 Id 목록의 길이가 2 미만이면 예외 처리한다.")
    @Test
    void validateTableGroupWhenSizeUnderTwo() {
        // when & then
        assertThatThrownBy(() -> new TableGroupRequest(List.of(1L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블의 수가 올바르지 않습니다.");
    }
}
