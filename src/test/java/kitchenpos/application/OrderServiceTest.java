package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.fixture.MenuFixture.세트_메뉴_1개씩;
import static kitchenpos.fixture.OrderTableFixture.빈_테이블_생성;
import static kitchenpos.fixture.OrderTableFixture.존재하지_않는_주문_테이블_생성;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블_생성;
import static kitchenpos.fixture.ProductFixture.치킨_8000원;
import static kitchenpos.fixture.ProductFixture.피자_8000원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import kitchenpos.IntegrationTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.fixture.RequestParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest extends IntegrationTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private TableService orderTableService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("주문 등록 시 전달받은 정보를 새 id로 저장한다.")
    void 주문_등록_성공_저장() {
        // given
        final OrderTable orderTable = orderTableService.create(주문_테이블_생성());
        final Product chicken = productService.create(치킨_8000원());
        final MenuGroup menuGroup = menuGroupService.create(new MenuGroup("양식"));

        // when
        final Menu menu = menuService.create(
                세트_메뉴_1개씩("치킨_할인", BigDecimal.valueOf(8000), menuGroup, List.of(chicken))
        );
        final Order saved = orderService.create(RequestParser.of(orderTable, List.of(menu)));

        // then
        final List<Order> savedOrders = orderService.list();
        assertThat(savedOrders)
                .map(Order::getId)
                .filteredOn(id -> Objects.equals(id, saved.getId()))
                .hasSize(1);
        assertThat(savedOrders.get(0).getOrderLineItems())
                .hasSize(1);
    }

    @Test
    @DisplayName("주문 등록 시 주문 항목의 개수는 최소 1개 이상이다.")
    void 주문_등록_실패_주문_항목_개수_미달() {
        // given
        final OrderTable orderTable = orderTableService.create(주문_테이블_생성());

        // expected
        assertThatThrownBy(
                () -> orderService.create(RequestParser.of(orderTable, Collections.emptyList())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 주문 항목으로 주문을 등록할 수 없다.")
    void 주문_등록_실패_존재하지_않는_주문_항목() {
        // given
        final OrderTable orderTable = orderTableService.create(주문_테이블_생성());
        final Product chicken = productService.create(치킨_8000원());
        final Product fakePizza = 피자_8000원();
        final MenuGroup menuGroup = menuGroupService.create(new MenuGroup("양식"));

        // when
        final Menu actualMenu = menuService.create(
                세트_메뉴_1개씩("치킨_할인", BigDecimal.valueOf(8000), menuGroup, List.of(chicken))
        );
        final Menu fakeMenu = new Menu("x", BigDecimal.ONE, menuGroup, List.of(new MenuProduct(fakePizza, 1)));

        // then
        assertThatThrownBy(() -> orderService.create(RequestParser.of(orderTable, List.of(actualMenu, fakeMenu))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 주문 테이블에서 주문을 등록할 수 없다.")
    void 주문_등록_실패_존재하지_않는_주문_테이블() {
        // given
        final OrderTable fakeOrderTable = 존재하지_않는_주문_테이블_생성();
        final Product chicken = productService.create(치킨_8000원());
        final MenuGroup menuGroup = menuGroupService.create(new MenuGroup("양식"));
        final Menu menu = menuService.create(
                세트_메뉴_1개씩("치킨_할인", BigDecimal.valueOf(8000), menuGroup, List.of(chicken))
        );

        // expected
        assertThatThrownBy(() -> orderService.create(RequestParser.of(fakeOrderTable, List.of(menu))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 테이블에서 주문을 등록할 수 없다.")
    void 주문_등록_실패_빈_테이블() {
        // given
        final OrderTable emptyTable = orderTableService.create(빈_테이블_생성());
        final Product chicken = productService.create(치킨_8000원());
        final MenuGroup menuGroup = menuGroupService.create(new MenuGroup("양식"));
        final Menu menu = menuService.create(
                세트_메뉴_1개씩("치킨_할인", BigDecimal.valueOf(8000), menuGroup, List.of(chicken))
        );

        // expected
        assertThatThrownBy(() -> orderService.create(RequestParser.of(emptyTable, List.of(menu))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문의 상태를 변경할 수 있다.")
    void 주문_상태_변경_성공() {
        // given
        final OrderTable orderTable = orderTableService.create(주문_테이블_생성());
        final Product chicken = productService.create(치킨_8000원());
        final MenuGroup menuGroup = menuGroupService.create(new MenuGroup("양식"));
        final Menu menu = menuService.create(
                세트_메뉴_1개씩("치킨_할인", BigDecimal.valueOf(8000), menuGroup, List.of(chicken))
        );
        final Order order = orderService.create(RequestParser.of(orderTable, List.of(menu)));

        // when
        final Order orderForMeal = orderService.changeOrderStatus(order.getId(), MEAL.name());

        // then
        assertThat(orderForMeal).usingRecursiveComparison()
                .ignoringFields("orderStatus")
                .isEqualTo(order);
        assertThat(orderForMeal.getOrderStatus()).isEqualTo(MEAL);
    }

    @Test
    @DisplayName("존재하지 않는 주문의 상태를 변경할 수 없다.")
    void 주문_상태_변경_실패_존재하지_않는_주문() {
        assertThatThrownBy(() -> orderService.changeOrderStatus(-1L, MEAL.name()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
