package kitchenpos.ui.tablegroup;

import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static kitchenpos.fixture.OrderTableFixture.주문_테이블_생성;
import static kitchenpos.fixture.TableGroupFixture.단체_지정_생성;
import static kitchenpos.fixture.TableGroupFixture.단체_지정_생성_요청;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TableGroupRestControllerAcceptanceTest extends TableGroupRestControllerAcceptanceTestFixture {

    private OrderTable 주문_테이블_A;
    private OrderTable 주문_테이블_B;

    @BeforeEach
    void setup() {
        주문_테이블_A = 주문_테이블을_생성한다(주문_테이블_생성(null, 10, true));
        주문_테이블_B = 주문_테이블을_생성한다(주문_테이블_생성(null, 20, true));
    }

    @Test
    void 단체를_생성한다() {
        // given
        var 단체 = 단체_지정_생성(List.of(주문_테이블_A, 주문_테이블_B));

        // when
        var 단체_지정_결과 = 단체를_지정한다("/api/table-groups", 단체_지정_생성_요청(단체));

        // then
        단체가_성공적으로_지정된다(단체_지정_결과, 단체);
    }

    @Test
    void 단체를_제거한다() {
        // given
        var 단체 = 단체_데이터_생성(단체_지정_생성(List.of(주문_테이블_A, 주문_테이블_B)));

        // when
        var 제거_결과 = 단체를_제거한다("/api/table-groups/" + 단체.getId());

        // then
        단체가_성공적으로_제거된다(제거_결과);
    }
}
