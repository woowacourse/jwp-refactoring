package kitchenpos.domain;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.fixture.MenuFixture.세트_메뉴_1개씩;
import static kitchenpos.fixture.OrderFixture.주문_생성_메뉴_당_1개씩;
import static kitchenpos.fixture.OrderFixture.주문_생성_메뉴_당_1개씩_상태_설정;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블_생성;
import static kitchenpos.fixture.ProductFixture.치킨_8000원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("새로 생성 주문의 상태는 '조리' 상태이다.")
    void 주문_생성_성공_주문_상태_조리() {
        // given
        final OrderTable orderTable = 주문_테이블_생성();
        final Product chicken = 치킨_8000원();
        final MenuGroup menuGroup = new MenuGroup("양식");
        final Menu menu = 세트_메뉴_1개씩("치킨_할인", BigDecimal.valueOf(8000), menuGroup, List.of(chicken));

        // when
        final Order order = 주문_생성_메뉴_당_1개씩(orderTable, List.of(menu));

        // then
        assertThat(order.getOrderStatus()).isEqualTo(COOKING);
    }

    @Test
    @DisplayName("결제 완료인 주문의 상태는 변경할 수 없다.")
    void 주문_상태_변경_실패_결제_완료() {
        // given
        final OrderTable orderTable = 주문_테이블_생성();
        final Product chicken = 치킨_8000원();
        final MenuGroup menuGroup = new MenuGroup("양식");
        final Menu menu = 세트_메뉴_1개씩("치킨_할인", BigDecimal.valueOf(8000), menuGroup, List.of(chicken));
        final Order order = 주문_생성_메뉴_당_1개씩_상태_설정(orderTable, COMPLETION, List.of(menu));

        // expected
        assertThatThrownBy(() -> order.setOrderStatus(MEAL))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
