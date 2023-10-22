package kitchenpos.domain;

import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TableGroupTest {

    @Test
    void 생성시_주문_그룹이_두_개_미만일_경우_예외가_발생한다() {
        // given
        final OrderTable orderTable = new OrderTable(null, 3, false);

        // when
        assertThatThrownBy(() -> new TableGroup(List.of(orderTable)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("단체 지정시 주문 테이블은 둘 이상이여야합니다");
    }

}
