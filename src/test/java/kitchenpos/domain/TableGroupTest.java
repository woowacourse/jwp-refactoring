package kitchenpos.domain;

import static kitchenpos.support.TestFixtureFactory.새로운_단체_지정;
import static kitchenpos.support.TestFixtureFactory.새로운_주문_테이블;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.exception.TableGroupException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TableGroupTest {

    @Test
    void 단체_지정시_주문_테이블이_2개_이상이어야_한다() {
        OrderTable 주문_테이블1 = 새로운_주문_테이블(null, 0, true);

        assertThatThrownBy(() -> 새로운_단체_지정(List.of(주문_테이블1)))
                .isInstanceOf(TableGroupException.class)
                .hasMessage("주문 테이블의 수가 유효하지 않습니다.");
    }

    @Test
    void 단체_지정시_주문_테이블이_비어있어야_한다() {
        OrderTable 주문_테이블1 = 새로운_주문_테이블(null, 0, true);
        OrderTable 주문_테이블2 = 새로운_주문_테이블(null, 1, false);

        assertThatThrownBy(() -> 새로운_단체_지정(List.of(주문_테이블1, 주문_테이블2)))
                .isInstanceOf(TableGroupException.class)
                .hasMessage("주문 테이블의 상태가 유효하지 않습니다.");
    }

    @Test
    void 단체_지정시_주문_테이블이_단체_지정되어있지_않아야한다 () {
        OrderTable 주문_테이블1 = 새로운_주문_테이블(null, 0, true);
        OrderTable 주문_테이블2 = 새로운_주문_테이블(new TableGroup(), 0, true);

        assertThatThrownBy(() -> 새로운_단체_지정(List.of(주문_테이블1, 주문_테이블2)))
                .isInstanceOf(TableGroupException.class)
                .hasMessage("주문 테이블의 상태가 유효하지 않습니다.");
    }
}
