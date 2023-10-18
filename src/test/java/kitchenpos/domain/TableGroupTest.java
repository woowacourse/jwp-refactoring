package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class TableGroupTest {

    @DisplayName("[SUCCESS] 생성한다.")
    @Test
    void success_create() {
        assertThatCode(() -> new TableGroup(LocalDateTime.now(), new ArrayList<>()))
                .doesNotThrowAnyException();
    }

    @DisplayName("[SUCCESS] 주문 테이블을 단체 지정에 주입할 때 주문 테이블의 비어있는 상태를 false 로 수정한다.")
    @Test
    void success_addOrderTablesAndChangeEmptyFull() {
        // given
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), new ArrayList<>());
        final OrderTable orderTable = new OrderTable(null, 10, true);

        // when
        tableGroup.addOrderTablesAndChangeEmptyFull(List.of(orderTable));

        // then
        assertSoftly(softly -> {
            softly.assertThat(orderTable.getTableGroup()).isEqualTo(tableGroup);
            softly.assertThat(orderTable.isEmpty()).isFalse();
        });
    }
}
