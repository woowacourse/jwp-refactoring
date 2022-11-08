package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @DisplayName("단체 지정을 생성할 때 생성 날짜가 없으면 예외가 발생한다.")
    @Test
    void createFailureWhenCreateDateIsNull() {
        assertThatThrownBy(
                () -> new TableGroup(null))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 생성 날짜가 없으면 안됩니다.");
    }
}
