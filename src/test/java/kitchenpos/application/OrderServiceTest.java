package kitchenpos.application;

import static java.lang.Long.MAX_VALUE;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.fixture.MenuFixture.메뉴_생성;
import static kitchenpos.fixture.MenuGroupFixture.추천_메뉴_그룹;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품;
import static kitchenpos.fixture.OrderFixture.존재하지_않는_OrderTable을_가진_주문_생성;
import static kitchenpos.fixture.OrderFixture.주문_생성;
import static kitchenpos.fixture.OrderLineItemFixture.메뉴만을_가진_OrderLineItem_생성;
import static kitchenpos.fixture.OrderLineItemFixture.존재하지_않는_메뉴를_가진_OrderLineItem_생성;
import static kitchenpos.fixture.OrderTableFixture.테이블_그룹이_없는_주문_테이블_생성;
import static kitchenpos.fixture.ProductFixture.후추_치킨_10000원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest extends ServiceIntegrationTest {

    @Test
    void 주문한_메뉴가_1개_미만인_경우_저장에_실패한다() {
        // given
        OrderTable savedOrderTable = 테이블_그룹이_없는_주문_테이블_생성(1, false);
        Order order = 주문_생성(savedOrderTable, Collections.emptyList());

        // when then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문한_메뉴_중_존재하지_않는_메뉴가_있으면_저장에_실패한다() {
        // given
        OrderTable savedOrderTable = 테이블_그룹이_없는_주문_테이블_생성(1, false);
        OrderLineItem orderLineItem = 존재하지_않는_메뉴를_가진_OrderLineItem_생성();
        Order order = 주문_생성(savedOrderTable, List.of(orderLineItem));

        // when then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void OrderTable이_존재하지_않으면_저장에_실패한다() {
        // given
        Product savedProduct = productDao.save(후추_치킨_10000원());
        MenuGroup savedMenuGroup = menuGroupDao.save(추천_메뉴_그룹());
        MenuProduct menuProduct = 메뉴_상품(savedProduct, 2);
        Menu savedMenu = menuDao.save(메뉴_생성(BigDecimal.valueOf(20000), savedMenuGroup, menuProduct));
        OrderLineItem orderLineItem = 메뉴만을_가진_OrderLineItem_생성(savedMenu, 2);
        Order order = 존재하지_않는_OrderTable을_가진_주문_생성(List.of(orderLineItem));

        // when then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문이_불가능한_상태라면_주문이_불가능하다() {
        // given
        OrderTable savedOrderTable = orderTableDao.save(테이블_그룹이_없는_주문_테이블_생성(1, true));
        Product savedProduct = productDao.save(후추_치킨_10000원());
        MenuGroup savedMenuGroup = menuGroupDao.save(추천_메뉴_그룹());
        MenuProduct menuProduct = 메뉴_상품(savedProduct, 2);
        Menu savedMenu = menuDao.save(메뉴_생성(BigDecimal.valueOf(20000), savedMenuGroup, menuProduct));
        OrderLineItem orderLineItem = 메뉴만을_가진_OrderLineItem_생성(savedMenu, 2);
        Order order = 주문_생성(savedOrderTable, List.of(orderLineItem));

        // when then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void Order를_성공적으로_저장한다() {
        // when
        OrderTable savedOrderTable = orderTableDao.save(테이블_그룹이_없는_주문_테이블_생성(1, false));
        Order savedOrder = 주문을_저장하고_반환받는다(savedOrderTable);

        // then
        assertAll(
                () -> assertThat(savedOrder.getId()).isNotNull(),
                () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(COOKING.name()),
                () -> assertThat(savedOrder.getOrderedTime()).isNotNull(),
                () -> assertThat(savedOrder.getOrderLineItems().get(0).getOrderId())
                        .isEqualTo(savedOrder.getId()),
                () -> assertThat(savedOrder.getOrderLineItems().get(0).getSeq()).isNotNull()
        );
    }

    @Test
    void 전체_Order_목록을_반환받는다() {
        // given
        OrderTable savedOrderTable = orderTableDao.save(테이블_그룹이_없는_주문_테이블_생성(1, false));
        List<Order> savedOrder =List.of(
                주문을_저장하고_반환받는다(savedOrderTable)
        );

        // when
        List<Order> savedOrdersExcludeExistingData = orderService.list()
                .stream()
                .filter(order ->
                        containsObjects(
                                savedOrder,
                                orderInSavedOrder -> orderInSavedOrder.getId().equals(order.getId())
                        )
                ).collect(Collectors.toList());

        // then
        assertThat(savedOrdersExcludeExistingData).usingRecursiveComparison()
                .isEqualTo(savedOrder);
    }

    @Test
    void 존재하지_않는_Order를_변경할_수_없다() {
        // given
        OrderTable savedOrderTable = orderTableDao.save(테이블_그룹이_없는_주문_테이블_생성(1, true));
        Order order = 주문_생성(savedOrderTable, Collections.emptyList());

        // when then
        assertThatThrownBy(() -> orderService.changeOrderStatus(MAX_VALUE, order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 이미_식사를_완료한_주문을_변경을_할_수_없다() {
        // given
        OrderTable savedOrderTable = orderTableDao.save(테이블_그룹이_없는_주문_테이블_생성(1, false));
        Order order = 주문을_저장하고_반환받는다(savedOrderTable);
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        orderDao.save(order);

        // when
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 성공적으로_Order를_원하는_상태로_바꾼다() {
        // given
        OrderTable savedOrderTable = orderTableDao.save(테이블_그룹이_없는_주문_테이블_생성(1, false));
        Order savedOrder = 주문을_저장하고_반환받는다(savedOrderTable);
        savedOrder.setOrderStatus(OrderStatus.COMPLETION.name());

        // when
        orderService.changeOrderStatus(savedOrder.getId(), savedOrder);

        // then
        Order changedOrder = orderService.list()
                .stream()
                .filter(order -> order.getId().equals(savedOrder.getId()))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);

        assertThat(changedOrder).usingRecursiveComparison()
                        .isEqualTo(savedOrder);
    }

}
