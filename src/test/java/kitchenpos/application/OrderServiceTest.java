package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.fixture.MenuFixture.메뉴_저장;
import static kitchenpos.fixture.OrderFixture.주문_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import kitchenpos.IntegrationTest;
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

}
