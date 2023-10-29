package kitchenpos.order.domain;

import static kitchenpos.support.TestFixtureFactory.새로운_주문_테이블;
import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.OrderTable;
import kitchenpos.exception.OrderTableException;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderTableTest {

    @Test
    void 테이블_그룹이_존재하는지_확인한다() {
        OrderTable 주문_테이블 = 새로운_주문_테이블(1L, 3, false);

        Assertions.assertThatThrownBy(() -> 주문_테이블.validateTableGroupIsNull())
                .isInstanceOf(OrderTableException.class)
                .hasMessage("테이블에 테이블 그룹이 존재합니다.");
    }

    @Test
    void 테이블_상태를_빈_테이블로_변경한다() {
        OrderTable 주문_테이블 = 새로운_주문_테이블(null, 3, false);

        주문_테이블.changeEmpty(true);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(주문_테이블.isEmpty()).isTrue();
            softly.assertThat(주문_테이블.getNumberOfGuests()).isZero();
        });
    }

    @Test
    void 테이블_상태를_비어있지_않은_테이블로_변경한다() {
        OrderTable 주문_테이블 = 새로운_주문_테이블(null, 0, true);

        주문_테이블.changeEmpty(false);

        Assertions.assertThat(주문_테이블.isEmpty()).isFalse();
    }

    @Test
    void 방문한_손님_수를_변경한다() {
        OrderTable 주문_테이블 = 새로운_주문_테이블(null, 3, false);

        주문_테이블.changeNumberOfGuests(1);

        Assertions.assertThat(주문_테이블.getNumberOfGuests()).isOne();
    }

    @Test
    void 방문한_손님_수를_변경할_때_방문한_손님_수가_0명_이상이어야_한다() {
        OrderTable 주문_테이블 = 새로운_주문_테이블(null, 3, false);

        Assertions.assertThatThrownBy(() -> 주문_테이블.changeNumberOfGuests(-1))
                .isInstanceOf(OrderTableException.class)
                .hasMessage("방문한 손님 수가 유효하지 않습니다.");
    }

    @Test
    void 방문한_손님_수를_변경할_때_테이블이_비어있지_않아야_한다() {
        OrderTable 주문_테이블 = 새로운_주문_테이블(null, 0, true);

        Assertions.assertThatThrownBy(() -> 주문_테이블.changeNumberOfGuests(3))
                .isInstanceOf(OrderTableException.class)
                .hasMessage("테이블이 비어있습니다.");    }
}
