package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.fixture.domain.MenuFixture.createMenu;
import static kitchenpos.fixture.domain.MenuGroupFixture.메뉴그룹A;
import static kitchenpos.fixture.domain.MenuGroupFixture.메뉴그룹B;
import static kitchenpos.fixture.domain.OrderTableFixture.createOrderTable;
import static kitchenpos.fixture.domain.ProductFixture.짜장면;
import static kitchenpos.fixture.domain.ProductFixture.탕수육;
import static kitchenpos.fixture.dto.MenuDtoFixture.createMenuRequest;
import static kitchenpos.fixture.dto.OrderDtoFixture.createOrderRequest;
import static kitchenpos.fixture.dto.OrderDtoFixture.forUpdateStatus;
import static kitchenpos.fixture.dto.OrderTableDtoFixture.forUpdateEmpty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.dto.response.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderServiceTest extends ServiceTest {

    @Test
    @DisplayName("주문을 등록한다.")
    void create() {
        // given
        final OrderTable table = 테이블등록(createOrderTable(3, false));
        final MenuResponse menu = 메뉴등록(createMenuRequest("탕수육_메뉴", 10_000, 메뉴그룹등록(메뉴그룹A), 상품등록(탕수육)));

        final OrderRequest request = createOrderRequest(table, menu);

        // when
        final OrderResponse actual = orderService.create(request);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name())
        );
    }

    @Test
    @DisplayName("craete : 주문 항목에 기재된 메뉴가 존재하지 않을 경우 예외가 발생한다.")
    void create_noMenu_throwException() {
        // given
        final OrderTable table = 테이블등록(createOrderTable(3, false));
        final Menu notRegisteredMenu = createMenu("탕수육_메뉴", 10_000, 메뉴그룹등록(메뉴그룹A), 상품등록(탕수육));

        final OrderRequest order = createOrderRequest(table, notRegisteredMenu);

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("craete : 주문 테이블이 비어있을 경우 등록할 수 없다")
    void create_emptyTable_throwException() {
        // given
        final OrderTable table = 테이블등록(createOrderTable(3, false));
        final MenuResponse menu = 메뉴등록(createMenuRequest("탕수육_메뉴", 10_000, 메뉴그룹등록(메뉴그룹A), 상품등록(탕수육)));

        tableService.changeEmpty(table.getId(), forUpdateEmpty(true));

        final OrderRequest order = createOrderRequest(table, menu);

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 목록을 조회한다.")
    void list() {
        // given
        final OrderTable table1 = 테이블등록(createOrderTable(3, false));
        final MenuResponse menu1 = 메뉴등록(createMenuRequest("탕수육_메뉴", 10_000, 메뉴그룹등록(메뉴그룹A), 상품등록(탕수육)));

        주문등록(createOrderRequest(table1, menu1));

        final OrderTable table2 = 테이블등록(createOrderTable(3, false));
        final MenuResponse menu2 = 메뉴등록(createMenuRequest("짜장면_메뉴", 8_000, 메뉴그룹등록(메뉴그룹B), 상품등록(짜장면)));

        주문등록(createOrderRequest(table2, menu2));

        // when
        final List<OrderResponse> actual = orderService.list();

        // then
        assertThat(actual).hasSize(2);
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatus() {
        // given
        final OrderTable table = 테이블등록(createOrderTable(3, false));
        final MenuResponse menu = 메뉴등록(createMenuRequest("탕수육_메뉴", 10_000, 메뉴그룹등록(메뉴그룹A), 상품등록(탕수육)));

        final OrderResponse order = 주문등록(createOrderRequest(table, menu));

        // when
        final OrderResponse actual = orderService.changeOrderStatus(order.getId(), forUpdateStatus(MEAL));

        // then
        assertThat(actual.getOrderStatus()).isEqualTo(MEAL.name());
    }

    @Test
    @DisplayName("changeOrderStatus : 주문이 존재하지 않으면 예외가 발생한다.")
    void changeOrderStatus_noOrder_throwException() {
        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(999L, forUpdateStatus(MEAL)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("changeOrderStatus : 주문이 이미 계산 완료된 경우 예외가 발생한다.")
    void changeOrderStatus_alreadyCompletion_throwException() {
        // given
        final OrderTable table = 테이블등록(createOrderTable(3, false));
        final MenuResponse menu = 메뉴등록(createMenuRequest("탕수육_메뉴", 10_000, 메뉴그룹등록(메뉴그룹A), 상품등록(탕수육)));

        final OrderResponse order = 주문등록(createOrderRequest(table, menu));
        주문상태변경(order.getId(), OrderStatus.COMPLETION);

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), forUpdateStatus(COOKING)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
