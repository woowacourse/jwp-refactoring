package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableGroupTest {

    @Test
    @DisplayName("두 개 미만의 테이블을 단체 지정하려면 예외를 반환한다.")
    void group_underTwoTables_throwException() {
        // given
        final TableGroup tableGroup = new TableGroup();

        // when & then
        assertThatThrownBy(() -> tableGroup.group(List.of(new OrderTable(3, false))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 테이블을 단체 지정하려면 예외를 반환한다.")
    void group_emptyTable_throwException() {
        // given
        final TableGroup tableGroup = new TableGroup();

        // when & then
        assertThatThrownBy(() -> tableGroup.group(
                List.of(new OrderTable(3, false),
                        new OrderTable(3, true))))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
