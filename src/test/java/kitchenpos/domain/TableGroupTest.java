package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TableGroupTest {

    @Test
    void 주문_테이블이_null인_경우_예외가_발생한다() {
        // given
        final var orderTables = Collections.<OrderTable>emptyList();

        // when & then
        assertThatThrownBy(() -> new TableGroup(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블_크기가_2보다_작은_경우_예외가_발생한다() {
        // given
        final var orderTables = Collections.singletonList(OrderTableFixture.빈테이블_1명());

        // when & then
        assertThatThrownBy(() -> new TableGroup(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_비어있지_않는_경우_예외가_발생한다() {
        // given
        final var orderTables = Collections.singletonList(OrderTableFixture.주문테이블_N명(1));

        // when & then
        assertThatThrownBy(() -> new TableGroup(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_이미_단체_지정된_경우_예외가_발생한다() {
        // given
        final var orderTables = Collections.singletonList(OrderTableFixture.빈테이블_1명_단체지정());

        // when & then
        assertThatThrownBy(() -> new TableGroup(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
