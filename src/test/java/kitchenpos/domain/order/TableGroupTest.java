package kitchenpos.domain.order;

import static kitchenpos.fixture.OrderTableFixture.주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupTest {

    @Test
    @DisplayName("단체에 지정할 테이블은 2개 이상이어야 한다.")
    void 단체_테이블_지정_실패_주문_테이블_개수_미달() {
        assertThatThrownBy(() -> new TableGroup().addOrderTables(List.of(주문_테이블_생성())))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
