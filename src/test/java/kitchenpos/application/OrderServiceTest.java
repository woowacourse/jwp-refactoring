package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest extends ServiceTestHelper{

    @Test
    void 주문을_등록한다() {
        final Order order = 주문_요청(손님있는_테이블, 이달의음료세트);

        final List<OrderLineItem> savedOrderLineItems = order.getOrderLineItems();

        assertAll(
                () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(savedOrderLineItems).extracting("menuId")
                        .containsExactly(이달의음료세트.getId())
        );
    }

    @Test
    void 주문_항목이_없는_주문을_등록하면_예외가_발생한다() {
        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> 주문_요청(손님있는_테이블));
    }

    @Test
    void 주문_항목의_메뉴가_존재하지_않는_메뉴면_예외가_발생한다() {
        // given
        final Menu notExistMenu = new Menu(-1L, "존재하지않는메뉴", BigDecimal.valueOf(10000L), -1L);

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> 주문_요청(손님있는_테이블, notExistMenu));
    }

    @Test
    void 주문_항목의_주문테이블이_존재하지_않는_주문테이블이면_예외가_발생한다() {
        // given
        final OrderTable notExistTable = new OrderTable();

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> 주문_요청(notExistTable, 이달의음료세트));
    }

    @Test
    void 주문_상태를_식사중으로_변경한다() {
        // given
        final Order order = 주문_요청(손님있는_테이블, 이달의음료세트);

        // when
        final Order mealOrder = 주문_식사_상태로_변경(order);

        // then
        assertThat(mealOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    void 주문_상태를_완료로_변경한다() {
        // given
        final Order order = 주문_요청(손님있는_테이블, 이달의음료세트);

        // when
        final Order completedOrder = 주문_완료_상태로_변경(order);

        // then
        assertThat(completedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @Test
    void 주문상태가_완료일_때_변경할_경우_예외가_발생한다() {
        // given
        final Order order = 주문_요청(손님있는_테이블, 이달의음료세트);

        // when
        final Order completedOrder = 주문_완료_상태로_변경(order);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> 주문_완료_상태로_변경(completedOrder));
    }
}
