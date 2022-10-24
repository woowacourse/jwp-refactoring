package acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableGroupAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("테이블 그룹을 생성한다.")
    void createTableGroup() {
        // given
        테이블_생성(2, true);
        테이블_생성(4, true);
        List<OrderTable> orderTables = 테이블_목록_조회();

        // when
        TableGroup result = 테이블_그룹_생성(orderTables);

        // then
        assertThat(result.getId()).isNotNull();
    }

    @Test
    @DisplayName("테이블 그룹을 생성한다.")
    void deleteTableGroup() {
        // given
        테이블_생성(2, true);
        테이블_생성(4, true);
        List<OrderTable> orderTables = 테이블_목록_조회();
        TableGroup tableGroup = 테이블_그룹_생성(orderTables);

        // when & then
        assertThatCode(() -> 테이블_그룹_삭제(tableGroup.getId()));
    }
}
