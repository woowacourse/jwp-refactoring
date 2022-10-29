package kitchenpos.acceptance;

import static kitchenpos.acceptance.fixture.TableStepDefinition.테이블을_생성한다;
import static kitchenpos.acceptance.fixture.TableStepDefinition.테이블을_조회한다;
import static kitchenpos.acceptance.fixture.TableStepDefinition.테이블의_상태를_변경한다;
import static kitchenpos.acceptance.fixture.TableStepDefinition.테이블의_손님의_숫자를_변경한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import kitchenpos.application.dto.OrderTableResponse;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;

public class TableAcceptanceTest extends AcceptanceTest {

    @Test
    void 테이블을_조회할_수_있다() {
        // given
        테이블을_생성한다(1, false);
        테이블을_생성한다(2, false);
        테이블을_생성한다(3, false);

        // when
        List<OrderTableResponse> extract = 테이블을_조회한다();

        // then
        assertThat(extract).hasSize(3);
    }

    @Test
    void 테이블을_빈_상태로_변경할_수_있다() {
        // given
        long 테이블 = 테이블을_생성한다(1, false);

        // when & then
        assertDoesNotThrow(() -> 테이블의_상태를_변경한다(테이블));
    }

    @Test
    void 테이블이_빈_상태가_아닐경우_손님의_숫자를_변경할_수_있다() {
        // given
        long 테이블 = 테이블을_생성한다(1, false);

        // when & then
        assertDoesNotThrow(() -> 테이블의_손님의_숫자를_변경한다(테이블, 3));
    }
}
