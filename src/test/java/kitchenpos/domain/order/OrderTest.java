package kitchenpos.domain.order;

import static kitchenpos.fixture.MenuFixture.메뉴;
import static kitchenpos.fixture.MenuFixture.메뉴_상품;
import static kitchenpos.fixture.MenuFixture.메뉴_상품들;
import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.fixture.OrderFixture.주문_상품;
import static kitchenpos.fixture.OrderFixture.주문_상품들;
import static kitchenpos.fixture.OrderTableFixture.손님_정보;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블;
import static kitchenpos.fixture.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.menu_group.MenuGroup;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.exception.OrderException.EmptyTableException;
import kitchenpos.exception.OrderException.NoOrderLineItemsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class OrderTest {

    private OrderLineItems orderLineItems;

    @BeforeEach
    void setup() {
        final MenuProducts menuProducts = 메뉴_상품들(메뉴_상품(상품("상품", BigDecimal.valueOf(10000L)), 1L));
        final MenuGroup menuGroup = 메뉴_그룹("메뉴_그룹");
        final Menu menu = 메뉴("메뉴", 1000L, menuProducts, menuGroup);
        this.orderLineItems = 주문_상품들(주문_상품(menu, 1L));
    }

    @Test
    void 주문을_한_주문_테이블이_빈_테이블이라면_예외가_발생한다() {
        // given
        final OrderTable orderTable = 주문_테이블(손님_정보(0, true));

        // expected
        assertThatThrownBy(() -> new Order(orderTable, orderLineItems))
                .isInstanceOf(EmptyTableException.class);
    }

    @Test
    void 주문에_주문_상품이_없다면_예외가_발생한다() {
        // given
        final OrderTable orderTable = 주문_테이블(손님_정보(10, false));

        // expected
        assertThatThrownBy(() -> new Order(orderTable, null))
                .isInstanceOf(NoOrderLineItemsException.class);
    }
}
