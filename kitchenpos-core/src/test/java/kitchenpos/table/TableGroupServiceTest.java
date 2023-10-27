package kitchenpos.table;

import kitchenpos.ServiceTestHelper;
import kitchenpos.order.Order;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest extends ServiceTestHelper {

    @Test
    void 테이블을_그룹으로_묶는다() {
        // given
        final TableGroup tableGroup = 테이블_그룹화(List.of(빈_테이블1, 빈_테이블2));

        // when
        final List<OrderTable> orderTables = tableGroup.getOrderTables();

        // then
        assertSoftly(softly -> {
            softly.assertThat(orderTables).extracting("id")
                    .contains(빈_테이블1.getId(), 빈_테이블2.getId());
            softly.assertThat(orderTables).extracting("tableGroupId")
                    .contains(tableGroup.getId(), tableGroup.getId());
        });
    }

    @Test
    void 채워진_테이블을_그룹으로_묶을경우_예외가_발생한다() {
        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> 테이블_그룹화(List.of(손님있는_테이블, 손님있는_식사중_테이블)));
    }

    @Test
    void 테이블그룹을_생성할_때_주문테이블이_1개면_예외가_발생한다() {
        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> 테이블_그룹화(List.of(빈_테이블1)));
    }

    @Test
    void 조리중인_테이블의_그룹을_해제할경우_예외가_발생한다() {
        // given
        final TableGroup tableGroup = 테이블_그룹화(List.of(빈_테이블1, 빈_테이블2));

        // when
        주문_요청(빈_테이블1, 이달의음료세트);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> 테이블_그룹해제(tableGroup));
    }

    @Test
    void 식사중인_테이블의_그룹을_해제할경우_예외가_발생한다() {
        // given
        final TableGroup tableGroup = 테이블_그룹화(List.of(빈_테이블1, 빈_테이블2));
        final Order order = 주문_요청(빈_테이블1, 이달의음료세트);

        // when
        주문_식사_상태로_변경(order);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> 테이블_그룹해제(tableGroup));
    }

    @Test
    void 테이블이_그룹으로_묶여지면_채워진다() {
        // given
        final TableGroup tableGroup = 테이블_그룹화(List.of(빈_테이블1, 빈_테이블2));

        // when
        final List<OrderTable> orderTables = tableGroup.getOrderTables();

        // then
        assertThat(orderTables).extracting("empty")
                .contains(false, false);
    }
}
