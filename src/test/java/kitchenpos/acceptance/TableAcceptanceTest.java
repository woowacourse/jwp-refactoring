package kitchenpos.acceptance;

import static kitchenpos.acceptance.fixture.TableStepDefinition.테이블을_생성한다;
import static kitchenpos.acceptance.fixture.TableStepDefinition.테이블을_조회한다;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;

public class TableAcceptanceTest extends AcceptanceTest {

    @Test
    void 테이블을_조회할_수_있다() {
        // given
        테이블을_생성한다(1L, 1);
        테이블을_생성한다(2L, 2);
        테이블을_생성한다(3L, 3);

        // when
        List<OrderTable> extract = 테이블을_조회한다();

        // then
        assertThat(extract).hasSize(3);
    }
}
