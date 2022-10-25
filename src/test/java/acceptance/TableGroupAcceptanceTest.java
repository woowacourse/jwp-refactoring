package acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("테이블 그룹 인수테스트에서")
public class TableGroupAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("테이블 그룹을 생성한다.")
    void createTableGroup() {
        테이블을_생성한다(2, true);
        테이블을_생성한다(4, true);

        TableGroup tableGroup = 테이블_그룹을_생성한다(테이블_목록을_조회한다());

        assertThat(tableGroup.getId()).isNotNull();
    }

    @Test
    @DisplayName("테이블 그룹을 해제한다.")
    void deleteTableGroup() {
        테이블을_생성한다(2, true);
        테이블을_생성한다(4, true);
        TableGroup tableGroup = 테이블_그룹을_생성한다(테이블_목록을_조회한다());

        assertThatCode(() -> 테이블_그룹을_해제한다(tableGroup.getId()));
    }
}
