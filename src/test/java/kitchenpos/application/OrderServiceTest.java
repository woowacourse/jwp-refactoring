package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.메뉴_생성;
import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹_생성;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품_생성;
import static kitchenpos.fixture.OrderFixture.주문_생성;
import static kitchenpos.fixture.OrderLineItemFixture.주문_항목_생성;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.config.ServiceTest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.fixture.OrderFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest {

    @Autowired
    MenuGroupDao menuGroupDao;

    @Autowired
    ProductDao productDao;

    @Autowired
    OrderTableDao orderTableDao;

    @Autowired
    MenuDao menuDao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    OrderService orderService;

    @Test
    void create_메서드는_order를_전달하면_order를_저장하고_반환한다() {
        // given
        final MenuGroup persistMenuGroup = menuGroupDao.save(메뉴_그룹_생성());
        final Product persistProduct = productDao.save(new Product("상품", BigDecimal.TEN));
        final MenuProduct menuProduct = 메뉴_상품_생성(persistProduct.getId());
        final Menu persistMenu = menuDao.save(메뉴_생성(persistMenuGroup.getId(), Arrays.asList(menuProduct)));
        final OrderLineItem persistOrderLineItem = 주문_항목_생성(persistMenu.getId());
        final OrderTable persistOrderTable = orderTableDao.save(주문_테이블_생성());
        final Order order = 주문_생성(persistOrderTable.getId(), Arrays.asList(persistOrderLineItem));

        // when
        final Order actual = orderService.create(order);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isPositive(),
                () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name())
        );
        assertThat(actual.getId()).isPositive();
    }

    @Test
    void create_메서드는_order의_orderLineItem이_없다면_예외가_발생한다() {
        // given
        final OrderTable persistOrderTable = orderTableDao.save(주문_테이블_생성());
        final Order invalidOrder = 주문_생성(persistOrderTable.getId(), Collections.emptyList());

        // when & then
        assertThatThrownBy(() -> orderService.create(invalidOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_메서드는_order의_orderLineItem의_menu가_없다면_예외가_발생한다() {
        // given
        final OrderLineItem persistOrderLineItem = 주문_항목_생성(-999L);
        final OrderTable persistOrderTable = orderTableDao.save(주문_테이블_생성());
        final Order invalidOrder = 주문_생성(persistOrderTable.getId(), Arrays.asList(persistOrderLineItem));

        // when & then
        assertThatThrownBy(() -> orderService.create(invalidOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_메서드는_order의_orderTable이_없다면_예외가_발생한다() {
        // given
        final MenuGroup persistMenuGroup = menuGroupDao.save(메뉴_그룹_생성());
        final Product persistProduct = productDao.save(new Product("상품", BigDecimal.TEN));
        final MenuProduct menuProduct = 메뉴_상품_생성(persistProduct.getId());
        final Menu persistMenu = menuDao.save(메뉴_생성(persistMenuGroup.getId(), Arrays.asList(menuProduct)));
        final OrderLineItem persistOrderLineItem = 주문_항목_생성(persistMenu.getId());
        final Order invalidOrder = 주문_생성(-999L, Arrays.asList(persistOrderLineItem));

        // when & then
        assertThatThrownBy(() -> orderService.create(invalidOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list_메서드는_등록한_모든_order를_반환한다() {
        // given
        final MenuGroup persistMenuGroup = menuGroupDao.save(메뉴_그룹_생성());
        final Product persistProduct = productDao.save(new Product("상품", BigDecimal.TEN));
        final MenuProduct menuProduct = 메뉴_상품_생성(persistProduct.getId());
        final Menu persistMenu = menuDao.save(메뉴_생성(persistMenuGroup.getId(), Arrays.asList(menuProduct)));
        final OrderLineItem persistOrderLineItem = 주문_항목_생성(persistMenu.getId());
        final OrderTable persistOrderTable = orderTableDao.save(주문_테이블_생성());
        final Order expected = orderDao.save(
                주문_생성(persistOrderTable.getId(), Arrays.asList(persistOrderLineItem), "MEAL")
        );

        // when
        final List<Order> actual = orderService.list();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0).getId()).isEqualTo(expected.getId())
        );
    }

    @ParameterizedTest(name = "orderStatus를 {0}으로 변경한다")
    @ValueSource(strings = {"COOKING", "MEAL", "COMPLETION"})
    void changeOrderStatus_메서드는_전달한_orderId의_상태가_COMPLETION이_아닌_order라면_전달한_상태로_변경한다(final String orderStatus) {
        // given
        final MenuGroup persistMenuGroup = menuGroupDao.save(메뉴_그룹_생성());
        final Product persistProduct = productDao.save(new Product("상품", BigDecimal.TEN));
        final MenuProduct menuProduct = 메뉴_상품_생성(persistProduct.getId());
        final Menu persistMenu = menuDao.save(메뉴_생성(persistMenuGroup.getId(), Arrays.asList(menuProduct)));
        final OrderLineItem persistOrderLineItem = 주문_항목_생성(persistMenu.getId());
        final OrderTable persistOrderTable = orderTableDao.save(주문_테이블_생성());
        final Order persistOrder = orderDao.save(
                주문_생성(persistOrderTable.getId(), Arrays.asList(persistOrderLineItem), "MEAL")
        );

        // when
        final Order statusOrder = OrderFixture.주문_생성(orderStatus);
        final Order actual = orderService.changeOrderStatus(persistOrder.getId(), statusOrder);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo(orderStatus);
    }

    @ParameterizedTest(name = "orderStatus가 {0}일 때 예외가 발생한다")
    @ValueSource(strings = {"COOKING", "MEAL", "COMPLETION"})
    void changeOrderStatus_메서드는_전달한_orderId의_상태가_COMPLETION이라면_예외가_발생한다(final String orderStatus) {
        // given
        final MenuGroup persistMenuGroup = menuGroupDao.save(메뉴_그룹_생성());
        final Product persistProduct = productDao.save(new Product("상품", BigDecimal.TEN));
        final MenuProduct menuProduct = 메뉴_상품_생성(persistProduct.getId());
        final Menu persistMenu = menuDao.save(메뉴_생성(persistMenuGroup.getId(), Arrays.asList(menuProduct)));
        final OrderLineItem persistOrderLineItem = 주문_항목_생성(persistMenu.getId());
        final OrderTable persistOrderTable = orderTableDao.save(주문_테이블_생성());
        final Order persistOrder = orderDao.save(
                주문_생성(persistOrderTable.getId(), Arrays.asList(persistOrderLineItem), "COMPLETION")
        );

        // when & then
        final Order statusOrder = OrderFixture.주문_생성(orderStatus);

        assertThatThrownBy(() -> orderService.changeOrderStatus(persistOrder.getId(), statusOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
