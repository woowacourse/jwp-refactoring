package kitchenpos.domain;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.fixture.MenuFixture.치킨_피자_세트_치킨_8000_1개_피자_8000_1개;
import static kitchenpos.fixture.OrderFixture.주문_생성_메뉴_당_1개씩_상태_설정;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {


    @Test
    @DisplayName("식사 상태인 테이블을 비울 수 없다.")
    void 주문_테이블_비우기_실패_식사_상태() {
        // given
        final OrderTable orderTable = 주문_테이블_생성();

        // when
        /// TODO: 2023/10/20 auditing 
        final Order order = new Order(orderTable, LocalDateTime.now());
        order.startMeal();

        // then
        assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    @DisplayName("조리 상태인 테이블을 비울 수 없다.")
    void 주문_테이블_비우기_실패_조리_상태() {
        // given
        final OrderTable orderTable = 주문_테이블_생성();

        // when
        주문_생성_메뉴_당_1개씩_상태_설정(orderTable, COOKING, List.of(치킨_피자_세트_치킨_8000_1개_피자_8000_1개(new MenuGroup("양식"))));

        // then
        assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
