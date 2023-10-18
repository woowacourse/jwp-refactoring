package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.fixture.MenuFixture.메뉴_저장;
import static kitchenpos.fixture.OrderFixture.주문_생성;
import static kitchenpos.fixture.OrderTableFixture.빈_테이블_저장;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블_저장;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import kitchenpos.IntegrationTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends IntegrationTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private TableService tableService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("주문 등록 시 전달받은 정보를 새 id로 저장한다.")
    void 주문_등록_성공_저장() {
        // given
        final OrderTable table = OrderTableFixture.주문_테이블_저장(tableService::create);

        // when
        final Order order = 주문_생성(
                table,
                List.of(메뉴_저장(menuService::create, productService::create))
        );
        final Order saved = orderService.create(order);

        // then
        assertThat(orderService.list())
                .map(Order::getId)
                .filteredOn(id -> Objects.equals(id, saved.getId()))
                .hasSize(1);
    }

    @Test
    @DisplayName("새로 등록된 주문의 상태는 '조리' 상태이다.")
    void 주문_등록_성공_주문_상태_조리() {
        // given
        final OrderTable table = OrderTableFixture.주문_테이블_저장(tableService::create);

        // when
        final Order order = 주문_생성(
                table,
                List.of(메뉴_저장(menuService::create, productService::create))
        );
        final Order saved = orderService.create(order);

        // then
        assertThat(orderService.list())
                .filteredOn(found -> Objects.equals(found.getId(), saved.getId()))
                .filteredOn(found -> COOKING.name().equals(found.getOrderStatus()))
                .hasSize(1);
    }

    @Test
    @DisplayName("주문 등록 시 주문 항목의 개수는 최소 1개 이상이다.")
    void 주문_등록_실패_주문_항목_개수_미달() {
        // given
        final OrderTable table = OrderTableFixture.주문_테이블_저장(tableService::create);

        // when
        // then
        assertThatThrownBy(() -> orderService.create(주문_생성(table, MEAL, Collections.emptyList())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 주문 항목으로 주문을 등록할 수 없다.")
    void 주문_등록_실패_존재하지_않는_주문_항목() {
        // given
        final OrderTable table = OrderTableFixture.주문_테이블_저장(tableService::create);
        final Menu actualMenu = 메뉴_저장(menuService::create, productService::create);
        final Menu fakeMenu = new Menu(-1L, "x", BigDecimal.ONE, 1L, List.of(new MenuProduct()));

        // when
        final Order order = 주문_생성(
                table,
                List.of(actualMenu, fakeMenu)
        );

        // then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 주문 테이블에서 주문을 등록할 수 없다.")
    void 주문_등록_실패_존재하지_않는_주문_테이블() {
        // given
        final OrderTable fakeTable = new OrderTable();
        fakeTable.setId(-1L);

        // when
        final Order order = 주문_생성(
                fakeTable,
                List.of(메뉴_저장(menuService::create, productService::create))
        );

        // then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 테이블에서 주문을 등록할 수 없다.")
    void 주문_등록_실패_빈_테이블() {
        // given
        final OrderTable emptyTable = 빈_테이블_저장(tableService::create);

        // when
        final Order order = 주문_생성(
                emptyTable,
                List.of(메뉴_저장(menuService::create, productService::create))
        );

        // then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문의 상태를 변경할 수 있다.")
    void 주문_상태_변경_성공() {
        // given
        final Order order = orderService.create(주문_생성(주문_테이블_저장(tableService::create),
                List.of(메뉴_저장(menuService::create, productService::create))));

        // when
        order.setOrderStatus(MEAL.name());
        final Order orderForMeal = orderService.changeOrderStatus(order.getId(), order);

        // then
        assertThat(orderForMeal).usingRecursiveComparison()
                .ignoringFields("orderStatus")
                .isEqualTo(order);
        assertThat(orderForMeal.getOrderStatus()).isEqualTo(MEAL.name());
    }

    @Test
    @DisplayName("결제 완료인 주문의 상태는 변경할 수 없다.")
    void 주문_상태_변경_실패_결제_완료() {
        // given
        /// TODO: 2023/10/16  Fixture 의미있게 만들수있도록 수정
        final Order order = orderService.create(주문_생성(주문_테이블_저장(tableService::create),
                List.of(메뉴_저장(menuService::create, productService::create))));
        order.setOrderStatus(COMPLETION.name());

        // when
        final Long orderId = order.getId();
        final Order completedOrder = orderService.changeOrderStatus(orderId, order);

        // then
        completedOrder.setOrderStatus(MEAL.name());
        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, completedOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
