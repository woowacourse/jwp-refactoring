package kitchenpos.application;

import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.table.TableGroup;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest extends ServiceTestHelper {

    @Test
    void 주문_테이블을_생성한다() {
        // given
        final OrderTable savedOrderTable1 = 테이블_등록();
        final OrderTable savedOrderTable2 = 테이블_등록();
        final OrderTable savedOrderTable3 = 테이블_등록();

        // when
        final List<OrderTable> orderTables = 테이블_목록_조회();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(orderTables).usingElementComparatorIgnoringFields()
                    .contains(savedOrderTable1, savedOrderTable2, savedOrderTable3);
            softly.assertThat(savedOrderTable1.getTableGroupId()).isNull();
        });
    }

    @Test
    void 주문_테이블_비움_상태_변경() {
        // when
        OrderTable orderTable = 테이블_채움(빈_테이블1.getId());

        // when
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @Test
    void 주문_테이블_비움상태_변경시_그룹화되어있으면_예외가_발생한다() {
        // given
        TableGroup tableGroup = 테이블_그룹화(빈_테이블1, 빈_테이블2);
        List<OrderTable> orderTables = tableGroup.getOrderTables();
        OrderTable orderTable1 = orderTables.get(0);

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> 테이블_채움(orderTable1.getId()));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> 테이블_비움(orderTable1.getId()));
    }

    @Test
    void 주문_테이블_비움상태_변경시_주문_상태가_조리중이면_예외가_발생한다() {
        // given
        주문_요청(손님있는_테이블, 이달의음료세트);

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(손님있는_테이블.getId(), 손님있는_테이블));
    }

    @Test
    void 주문_테이블_비움상태_변경시_주문_상태가_식사중이면_예외가_발생한다() {
        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(손님있는_식사중_테이블.getId(), 손님있는_식사중_테이블));
    }

    @Test
    void 주문_테이블의_손님수를_변경한다() {
        // given
        OrderTable orderTable = 테이블_손님_수_변경(손님있는_테이블.getId(), 5);

        // when & then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(5);
    }

    @Test
    void 주문_테이블의_손님수를_음수로_지정하면_예외가_발생한다() {
        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() ->  테이블_손님_수_변경(손님있는_테이블.getId(), -1));
    }

    @Test
    void 주문_테이블의_손님수_변경시_테이블이_비어있으면_예외가_발생한다() {
        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() ->  테이블_손님_수_변경(빈_테이블1.getId(), 20));
    }
}
