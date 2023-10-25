package kitchenpos.application;

import static java.lang.Long.MAX_VALUE;
import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.fixture.MenuFixture.메뉴_생성;
import static kitchenpos.fixture.MenuGroupFixture.추천_메뉴_그룹;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품;
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
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.ui.request.OrderCreateRequest;
import kitchenpos.ui.request.OrderUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class OrdersServiceTest extends ServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Test
    void 주문한_메뉴가_1개_미만인_경우_저장에_실패한다() {
        // given
        OrderTable savedOrderTable = 단체_지정이_없는_주문_테이블_생성(1, false);
        OrderCreateRequest request = new OrderCreateRequest(
                savedOrderTable.getId(),
                Collections.emptyList()
        );

        // expect
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문한_메뉴_중_존재하지_않는_메뉴가_있으면_저장에_실패한다() {
        // given
        OrderTable savedOrderTable = orderTableRepository.save(
                단체_지정이_없는_주문_테이블_생성(1, false)
        );
        OrderLineItem orderLineItem = 존재하지_않는_메뉴를_가진_주문_항목_생성();
        OrderCreateRequest request = new OrderCreateRequest(
                savedOrderTable.getId(),
                List.of(orderLineItemToCreateRequest(orderLineItem))
        );

        // expect
        assertThatThrownBy(() -> orderService.create(request))
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
        OrderCreateRequest request = new OrderCreateRequest(
                Long.MAX_VALUE,
                List.of(orderLineItemToCreateRequest(orderLineItem))
        );

        // expect
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문이_불가능한_상태라면_주문이_불가능하다() {
        // given
        OrderTable savedOrderTable = orderTableRepository.save(단체_지정이_없는_주문_테이블_생성(1, true));
        Product savedProduct = productRepository.save(후추_치킨_10000원());
        MenuGroup savedMenuGroup = menuGroupRepository.save(추천_메뉴_그룹());
        MenuProduct menuProduct = 메뉴_상품(savedProduct, 2);
        Menu savedMenu = menuRepository.save(메뉴_생성(20000L, savedMenuGroup, menuProduct));
        OrderLineItem orderLineItem = 메뉴을_가진_주문_항목_생성(savedMenu, 2);
        OrderCreateRequest request = new OrderCreateRequest(
                savedOrderTable.getId(),
                List.of(orderLineItemToCreateRequest(orderLineItem))
        );

        // expect
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_성공적으로_저장한다() {
        // given
        OrderTable savedOrderTable = orderTableRepository.save(단체_지정이_없는_주문_테이블_생성(1, false));

        // when
        Order savedOrders = 주문을_저장하고_반환받는다(savedOrderTable);

        // then
        assertAll(
                () -> assertThat(savedOrders.getId()).isNotNull(),
                () -> assertThat(savedOrders.getOrderStatus()).isEqualTo(COOKING),
                () -> assertThat(savedOrders.getOrderedTime()).isNotNull(),
                () -> assertThat(savedOrders.getOrderLineItems().get(0).getOrder())
                        .isEqualTo(savedOrders),
                () -> assertThat(savedOrders.getOrderLineItems().get(0).getSeq()).isNotNull()
        );
    }

    @Test
    void 전체_주문_목록을_반환받는다() {
        // given
        OrderTable savedOrderTable = orderTableRepository.save(단체_지정이_없는_주문_테이블_생성(1, false));
        List<Order> expected = List.of(
                주문을_저장하고_반환받는다(savedOrderTable)
        );

        // when
        List<Order> actual = orderService.list();

        // then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void 존재하지_않는_주문을_변경할_수_없다() {
        // given
        OrderUpdateRequest request = new OrderUpdateRequest(OrderStatus.MEAL.name());

        // expect
        assertThatThrownBy(() -> orderService.changeOrderStatus(MAX_VALUE, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 이미_식사를_완료한_주문을_변경을_할_수_없다() {
        // given
        OrderTable savedOrderTable = orderTableRepository.save(단체_지정이_없는_주문_테이블_생성(1, false));
        Order order = 주문을_저장하고_반환받는다(savedOrderTable);
        order.changeOrderStatus(OrderStatus.COMPLETION);
        orderRepository.save(order);
        OrderUpdateRequest request = new OrderUpdateRequest(OrderStatus.MEAL.name());

        // expect
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 성공적으로_주문을_원하는_상태로_바꾼다() {
        // given
        OrderTable savedOrderTable = orderTableRepository.save(단체_지정이_없는_주문_테이블_생성(1, false));
        Order savedOrders = 주문을_저장하고_반환받는다(savedOrderTable);
        OrderUpdateRequest request = new OrderUpdateRequest(OrderStatus.COMPLETION.name());

        // when
        orderService.changeOrderStatus(savedOrders.getId(), request);

        // then
        Order actual = orderService.list()
                .stream()
                .filter(order -> order.getId().equals(savedOrders.getId()))
                .findFirst()
                .get();
        assertAll(
                () -> assertThat(actual.getOrderStatus()).isEqualTo(COMPLETION),
                () -> assertThat(actual).usingRecursiveComparison()
                        .ignoringFieldsOfTypes(OrderStatus.class)
                        .isEqualTo(savedOrders)
        );
    }

}
