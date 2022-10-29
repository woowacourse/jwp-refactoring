package kitchenpos.acceptance;

import static kitchenpos.acceptance.fixture.TableGroupStepDefinition.테이블_그룹을_생성한다;
import static kitchenpos.acceptance.fixture.TableGroupStepDefinition.테이블_그룹을_해제한다;
import static kitchenpos.acceptance.fixture.TableStepDefinition.테이블을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.support.annotation.AcceptanceTest;
import org.junit.jupiter.api.Test;

@AcceptanceTest
public class TableGroupAcceptanceTest {

    @Test
    void 테이블_그룹을_생성할_수_있다() {
        // given
        long 테이블_1번 = 테이블을_생성한다(0, true);
        long 테이블_2번 = 테이블을_생성한다(0, true);

        // when
        long extract = 테이블_그룹을_생성한다(LocalDateTime.now(), List.of(테이블_1번, 테이블_2번));

        // then
        assertThat(extract).isNotNull();
    }

    @Test
    void 테이블_그룹을_해제할_수_있다() {
        // given
        long 테이블_1번 = 테이블을_생성한다(1, true);
        long 테이블_2번 = 테이블을_생성한다(1, true);
        long 테이블_그룹 = 테이블_그룹을_생성한다(LocalDateTime.now(), List.of(테이블_1번, 테이블_2번));

        // when & then
        assertThatThrownBy(() -> 테이블_그룹을_해제한다(테이블_그룹));
    }
}
