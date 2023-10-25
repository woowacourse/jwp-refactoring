package kitchenpos.domain.order;

import static kitchenpos.domain.vo.OrderStatus.COMPLETION;
import static kitchenpos.domain.vo.OrderStatus.COOKING;
import static kitchenpos.domain.vo.OrderStatus.MEAL;
import static kitchenpos.fixture.MenuFixture.세트_메뉴_1개씩;
import static kitchenpos.fixture.OrderFixture.주문_생성_메뉴_당_1개씩;
import static kitchenpos.fixture.OrderFixture.주문_생성_메뉴_당_1개씩_상태_설정;
import static kitchenpos.fixture.ProductFixture.치킨_8000원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("새로 생성한 주문의 상태는 '조리' 상태이다.")
    void 주문_생성_성공_주문_상태_조리() {
        // given
        final Product chicken = 치킨_8000원();
        final MenuGroup menuGroup = new MenuGroup("양식");
        final Menu menu = 세트_메뉴_1개씩("치킨_할인", BigDecimal.valueOf(8000), menuGroup, List.of(chicken));

        // when
        final Order order = 주문_생성_메뉴_당_1개씩(List.of(menu));

        // then
        assertThat(order.getOrderStatus()).isEqualTo(COOKING);
    }

    @Test
    @DisplayName("주문 항목은 최소 1개 이상이어야 한다.")
    void 주문_생성_실패_주문_항목_없음() {
        // expected
        assertThatThrownBy(() -> new Order(1L, Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("결제 완료인 주문의 상태는 변경할 수 없다.")
    void 주문_상태_변경_실패_결제_완료() {
        // given
        final Product chicken = 치킨_8000원();
        final MenuGroup menuGroup = new MenuGroup("양식");
        final Menu menu = 세트_메뉴_1개씩("치킨_할인", BigDecimal.valueOf(8000), menuGroup, List.of(chicken));
        final Order order = 주문_생성_메뉴_당_1개씩_상태_설정(COMPLETION, List.of(menu));

        // expected
        assertThatThrownBy(() -> order.changeOrderStatus(MEAL))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
