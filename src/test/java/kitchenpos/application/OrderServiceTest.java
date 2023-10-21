package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.fixture.MenuFixture.세트_메뉴_1개씩;
import static kitchenpos.fixture.OrderFixture.주문_생성_메뉴_당_1개씩;
import static kitchenpos.fixture.OrderTableFixture.빈_테이블_생성;
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
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderLineItemRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends IntegrationTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderLineItemRepository orderLineItemRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("주문 등록 시 전달받은 정보를 새 id로 저장한다.")
    void 주문_등록_성공_저장() {
        // given
        final OrderTable orderTable = orderTableRepository.save(주문_테이블_생성());
        final Product chicken = productRepository.save(치킨_8000원());
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("양식"));

        // when
        final Menu menu = menuRepository.save(
                세트_메뉴_1개씩("치킨_할인", BigDecimal.valueOf(8000), menuGroup, List.of(chicken))
        );
        final Order saved = orderService.create(주문_생성_메뉴_당_1개씩(orderTable, List.of(menu)));

        // then
        assertThat(orderService.list())
                .map(Order::getId)
                .filteredOn(id -> Objects.equals(id, saved.getId()))
                .hasSize(1);
        assertThat(orderLineItemRepository.findAll())
                .map(OrderLineItem::getOrder)
                .map(Order::getId)
                .filteredOn(id -> Objects.equals(id, saved.getId()))
                .hasSize(1);
    }

    @Test
    @DisplayName("주문 등록 시 주문 항목의 개수는 최소 1개 이상이다.")
    void 주문_등록_실패_주문_항목_개수_미달() {
        // given
        final OrderTable orderTable = orderTableRepository.save(주문_테이블_생성());

        // when
        // then
        assertThatThrownBy(
                () -> orderService.create(주문_생성_메뉴_당_1개씩(orderTable, Collections.emptyList())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 주문 항목으로 주문을 등록할 수 없다.")
    void 주문_등록_실패_존재하지_않는_주문_항목() {
        // given
        final OrderTable orderTable = orderTableRepository.save(주문_테이블_생성());
        final Product chicken = productRepository.save(치킨_8000원());
        final Product fakePizza = 피자_8000원();
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("양식"));

        // when
        final Menu actualMenu = menuRepository.save(
                세트_메뉴_1개씩("치킨_할인", BigDecimal.valueOf(8000), menuGroup, List.of(chicken))
        );
        final Menu fakeMenu = new Menu("x", BigDecimal.ONE, menuGroup, List.of(new MenuProduct(fakePizza, 1)));
        final Order order = 주문_생성_메뉴_당_1개씩(orderTable, List.of(actualMenu, fakeMenu));

        // then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 주문 테이블에서 주문을 등록할 수 없다.")
    void 주문_등록_실패_존재하지_않는_주문_테이블() {
        // given
        final OrderTable fakeOrderTable = 주문_테이블_생성();
        fakeOrderTable.setId(-1L);
        final Product chicken = productRepository.save(치킨_8000원());
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("양식"));
        final Menu menu = menuRepository.save(
                세트_메뉴_1개씩("치킨_할인", BigDecimal.valueOf(8000), menuGroup, List.of(chicken))
        );

        // when
        final Order order = 주문_생성_메뉴_당_1개씩(fakeOrderTable, List.of(menu));

        // then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 테이블에서 주문을 등록할 수 없다.")
    void 주문_등록_실패_빈_테이블() {
        // given
        final OrderTable emptyTable = orderTableRepository.save(빈_테이블_생성());
        final Product chicken = productRepository.save(치킨_8000원());
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("양식"));
        final Menu menu = menuRepository.save(
                세트_메뉴_1개씩("치킨_할인", BigDecimal.valueOf(8000), menuGroup, List.of(chicken))
        );

        // when
        final Order order = 주문_생성_메뉴_당_1개씩(emptyTable, List.of(menu));

        // then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문의 상태를 변경할 수 있다.")
    void 주문_상태_변경_성공() {
        // given
        final OrderTable orderTable = orderTableRepository.save(주문_테이블_생성());
        final Product chicken = productRepository.save(치킨_8000원());
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("양식"));
        final Menu menu = menuRepository.save(
                세트_메뉴_1개씩("치킨_할인", BigDecimal.valueOf(8000), menuGroup, List.of(chicken))
        );
        final Order order = orderService.create(주문_생성_메뉴_당_1개씩(orderTable, List.of(menu)));

        // when
        order.changeOrderStatus(MEAL);
        final Order orderForMeal = orderService.changeOrderStatus(order.getId(), order);

        // then
        assertThat(orderForMeal).usingRecursiveComparison()
                .ignoringFields("orderStatus")
                .isEqualTo(order);
        assertThat(orderForMeal.getOrderStatus()).isEqualTo(MEAL);
    }

}
