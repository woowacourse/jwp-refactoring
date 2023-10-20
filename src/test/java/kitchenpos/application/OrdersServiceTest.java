package kitchenpos.application;

import static java.lang.Long.MAX_VALUE;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.fixture.MenuFixture.메뉴_생성;
import static kitchenpos.fixture.MenuGroupFixture.추천_메뉴_그룹;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품;
import static kitchenpos.fixture.OrderFixture.존재하지_않는_주문_테이블을_가진_주문_생성;
import static kitchenpos.fixture.OrderFixture.주문_생성;
import static kitchenpos.fixture.OrderLineItemFixture.메뉴을_가진_주문_항목_생성;
import static kitchenpos.fixture.OrderLineItemFixture.존재하지_않는_메뉴를_가진_주문_항목_생성;
import static kitchenpos.fixture.OrderTableFixture.단체_지정이_없는_주문_테이블_생성;
import static kitchenpos.fixture.ProductFixture.후추_치킨_10000원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Orders;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class OrdersServiceTest extends ServiceIntegrationTest {

    @Test
    void 주문한_메뉴가_1개_미만인_경우_저장에_실패한다() {
        // given
        OrderTable savedOrderTable = 단체_지정이_없는_주문_테이블_생성(1, false);
        Orders orders = 주문_생성(savedOrderTable, Collections.emptyList());

        // expect
        assertThatThrownBy(() -> orderService.create(orders))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문한_메뉴_중_존재하지_않는_메뉴가_있으면_저장에_실패한다() {
        // given
        OrderTable savedOrderTable = 단체_지정이_없는_주문_테이블_생성(1, false);
        OrderLineItem orderLineItem = 존재하지_않는_메뉴를_가진_주문_항목_생성();
        Orders orders = 주문_생성(savedOrderTable, List.of(orderLineItem));

        // expect
        assertThatThrownBy(() -> orderService.create(orders))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_존재하지_않으면_저장에_실패한다() {
        // given
        Product savedProduct = productRepository.save(후추_치킨_10000원());
        MenuGroup savedMenuGroup = menuGroupRepository.save(추천_메뉴_그룹());
        MenuProduct menuProduct = 메뉴_상품(savedProduct, 2);
        Menu savedMenu = menuRepository.save(메뉴_생성(20000L, savedMenuGroup, menuProduct));
        OrderLineItem orderLineItem = 메뉴을_가진_주문_항목_생성(savedMenu, 2);
        Orders orders = 존재하지_않는_주문_테이블을_가진_주문_생성(List.of(orderLineItem));

        // expect
        assertThatThrownBy(() -> orderService.create(orders))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문이_불가능한_상태라면_주문이_불가능하다() {
        // given
        OrderTable savedOrderTable = orderTableDao.save(단체_지정이_없는_주문_테이블_생성(1, true));
        Product savedProduct = productRepository.save(후추_치킨_10000원());
        MenuGroup savedMenuGroup = menuGroupRepository.save(추천_메뉴_그룹());
        MenuProduct menuProduct = 메뉴_상품(savedProduct, 2);
        Menu savedMenu = menuRepository.save(메뉴_생성(20000L, savedMenuGroup, menuProduct));
        OrderLineItem orderLineItem = 메뉴을_가진_주문_항목_생성(savedMenu, 2);
        Orders orders = 주문_생성(savedOrderTable, List.of(orderLineItem));

        // expect
        assertThatThrownBy(() -> orderService.create(orders))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_성공적으로_저장한다() {
        // given
        OrderTable savedOrderTable = orderTableDao.save(단체_지정이_없는_주문_테이블_생성(1, false));

        // when
        Orders savedOrders = 주문을_저장하고_반환받는다(savedOrderTable);

        // then
        assertAll(
                () -> assertThat(savedOrders.getId()).isNotNull(),
                () -> assertThat(savedOrders.getOrderStatus()).isEqualTo(COOKING.name()),
                () -> assertThat(savedOrders.getOrderedTime()).isNotNull(),
                () -> assertThat(savedOrders.getOrderLineItems().get(0).getOrderId())
                        .isEqualTo(savedOrders.getId()),
                () -> assertThat(savedOrders.getOrderLineItems().get(0).getSeq()).isNotNull()
        );
    }

    @Test
    void 전체_주문_목록을_반환받는다() {
        // given
        OrderTable savedOrderTable = orderTableDao.save(단체_지정이_없는_주문_테이블_생성(1, false));
        List<Orders> expected = List.of(
                주문을_저장하고_반환받는다(savedOrderTable)
        );

        // when
        List<Orders> actual = orderService.list();

        // then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void 존재하지_않는_주문을_변경할_수_없다() {
        // given
        OrderTable savedOrderTable = orderTableDao.save(단체_지정이_없는_주문_테이블_생성(1, true));
        Orders orders = 주문_생성(savedOrderTable, Collections.emptyList());

        // expect
        assertThatThrownBy(() -> orderService.changeOrderStatus(MAX_VALUE, orders))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 이미_식사를_완료한_주문을_변경을_할_수_없다() {
        // given
        OrderTable savedOrderTable = orderTableDao.save(단체_지정이_없는_주문_테이블_생성(1, false));
        Orders orders = 주문을_저장하고_반환받는다(savedOrderTable);
        orders.setOrderStatus(OrderStatus.COMPLETION.name());
        orderDao.save(orders);

        // expect
        assertThatThrownBy(() -> orderService.changeOrderStatus(orders.getId(), orders))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 성공적으로_주문을_원하는_상태로_바꾼다() {
        // given
        OrderTable savedOrderTable = orderTableDao.save(단체_지정이_없는_주문_테이블_생성(1, false));
        Orders savedOrders = 주문을_저장하고_반환받는다(savedOrderTable);
        savedOrders.setOrderStatus(OrderStatus.COMPLETION.name());

        // when
        orderService.changeOrderStatus(savedOrders.getId(), savedOrders);

        // then
        Orders changedOrders = orderService.list()
                .stream()
                .filter(order -> order.getId().equals(savedOrders.getId()))
                .findFirst()
                .get();
        assertThat(changedOrders).usingRecursiveComparison()
                        .isEqualTo(savedOrders);
    }

}
