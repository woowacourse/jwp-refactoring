package kitchenpos.domain;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.ServiceTest;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderValidatorTest extends ServiceTest {

    @Autowired
    private OrderValidator orderValidator;

    @Test
    void 주문_테이블이_비어있는_경우_예외가_발생한다() {
        // given
        final var orderTable = OrderTableFixture.빈테이블_1명();
        단일_주문테이블_저장(orderTable);

        final var menuGroup = MenuGroupFixture.메뉴그룹_신메뉴();
        final var savedMenuGroup = 단일_메뉴그룹_저장(menuGroup);

        final var product1 = ProductFixture.상품_망고_1000원();
        final var product2 = ProductFixture.상품_치킨_15000원();
        복수_상품_저장(product1, product2);

        final var menuProduct1 = MenuProductFixture.메뉴상품_생성(product1, 2L);
        final var menuProduct2 = MenuProductFixture.메뉴상품_생성(product2, 1L);
        final var menu = MenuFixture.메뉴_망고치킨_17000원(savedMenuGroup, menuProduct1, menuProduct2);
        final var savedMenu = 단일_메뉴_저장(menu);

        final var orderLineItems = Collections.singletonList(OrderLineItemFixture.주문항목_망고치킨_2개(savedMenu));
        final var order = new Order(orderTable.getId(), COOKING, LocalDateTime.now(), orderLineItems);

        // when & then
        assertThatThrownBy(() -> orderValidator.validate(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_항목이_비어있는_경우_예외가_발생한다() {
        // given
        final var orderTable = OrderTableFixture.주문테이블_N명(2);
        단일_주문테이블_저장(orderTable);

        final var orderLineItems = Collections.<OrderLineItem>emptyList();
        final var order = new Order(orderTable.getId(), COOKING, LocalDateTime.now(), orderLineItems);

        // when & then
        assertThatThrownBy(() -> orderValidator.validate(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_상태가_이미_계산완료면_예외가_발생한다() {
        // given
        final var orderTable = OrderTableFixture.주문테이블_N명(2);
        final var createdDate = LocalDateTime.now();
        final var orderLineItems = List.of(OrderLineItemFixture.주문항목_망고치킨_2개());
        final var order = new Order(orderTable.getId(), COMPLETION, createdDate, orderLineItems);
        order.changeOrderStatus(COMPLETION);

        // when & then
        final var orderStatus = order.getOrderStatus();
        assertThatThrownBy(() -> orderValidator.validateOrderStatus(orderStatus))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
