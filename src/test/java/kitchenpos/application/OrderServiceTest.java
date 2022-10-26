package kitchenpos.application;

import static kitchenpos.application.fixture.MenuFixture.createMenu;
import static kitchenpos.application.fixture.MenuGroupFixture.메뉴그룹A;
import static kitchenpos.application.fixture.MenuGroupFixture.메뉴그룹B;
import static kitchenpos.application.fixture.OrderFixture.createOrder;
import static kitchenpos.application.fixture.OrderFixture.forUpdateStatus;
import static kitchenpos.application.fixture.OrderTableFixture.createOrderTable;
import static kitchenpos.application.fixture.OrderTableFixture.forUpdateEmpty;
import static kitchenpos.application.fixture.ProductFixture.짜장면;
import static kitchenpos.application.fixture.ProductFixture.탕수육;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderServiceTest extends ServiceTest {

    @Test
    @DisplayName("주문을 등록한다.")
    void create() {
        // given
        final OrderTable table = 테이블등록(createOrderTable(3, false));
        final Menu menu = 메뉴등록(createMenu("탕수육_메뉴", 10_000, 메뉴그룹등록(메뉴그룹A), 상품등록(탕수육)));

        final Order order = createOrder(table, menu);

        // when
        final Order createdOrder = orderService.create(order);

        // then
        assertAll(
                () -> assertThat(createdOrder.getId()).isNotNull(),
                () -> assertThat(createdOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name())
        );
    }

    @Test
    @DisplayName("craete : 주문 항목에 기재된 메뉴가 존재하지 않을 경우 예외가 발생한다.")
    void create_noMenu_throwException() {
        // given
        final OrderTable table = 테이블등록(createOrderTable(3, false));
        final Menu notRegisteredMenu = createMenu("탕수육_메뉴", 10_000, 메뉴그룹등록(메뉴그룹A), 상품등록(탕수육));
        notRegisteredMenu.setId(999L);

        final Order order = createOrder(table, notRegisteredMenu);

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("craete : 주문 테이블이 비어있을 경우 등록할 수 없다")
    void create_emptyTable_throwException() {
        // given
        final OrderTable table = 테이블등록(createOrderTable(3, false));
        final Menu menu = 메뉴등록(createMenu("탕수육_메뉴", 10_000, 메뉴그룹등록(메뉴그룹A), 상품등록(탕수육)));

        tableService.changeEmpty(table.getId(), forUpdateEmpty(true));

        final Order order = createOrder(table, menu);

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 목록을 조회한다.")
    void list() {
        // given
        final OrderTable table1 = 테이블등록(createOrderTable(3, false));
        final Menu menu1 = 메뉴등록(createMenu("탕수육_메뉴", 10_000, 메뉴그룹등록(메뉴그룹A), 상품등록(탕수육)));

        주문등록(createOrder(table1, menu1));

        final OrderTable table2 = 테이블등록(createOrderTable(3, false));
        final Menu menu2 = 메뉴등록(createMenu("짜장면_메뉴", 8_000, 메뉴그룹등록(메뉴그룹B), 상품등록(짜장면)));

        주문등록(createOrder(table2, menu2));

        // when
        final List<Order> actual = orderService.list();

        // then
        assertThat(actual).hasSize(2);
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatus() {
        // given
        final OrderTable table = 테이블등록(createOrderTable(3, false));
        final Menu menu = 메뉴등록(createMenu("탕수육_메뉴", 10_000, 메뉴그룹등록(메뉴그룹A), 상품등록(탕수육)));

        final Order order = 주문등록(createOrder(table, menu));

        // when
        final Order actual = orderService.changeOrderStatus(order.getId(), forUpdateStatus(MEAL));

        // then
        assertThat(actual.getOrderStatus()).isEqualTo(MEAL.name());
    }

    @Test
    @DisplayName("changeOrderStatus : 주문이 존재하지 않으면 예외가 발생한다.")
    void changeOrderStatus_noOrder_throwException() {
        // given
        final OrderTable table = 테이블등록(createOrderTable(3, false));
        final Menu menu = 메뉴등록(createMenu("탕수육_메뉴", 10_000, 메뉴그룹등록(메뉴그룹A), 상품등록(탕수육)));

        final Order notRegisteredOrder = createOrder(table, menu);

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(notRegisteredOrder.getId(), forUpdateStatus(MEAL)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("changeOrderStatus : 주문이 이미 계산 완료된 경우 예외가 발생한다.")
    void changeOrderStatus_alreadyCompletion_throwException() {
        // given
        final OrderTable table = 테이블등록(createOrderTable(3, false));
        final Menu menu = 메뉴등록(createMenu("탕수육_메뉴", 10_000, 메뉴그룹등록(메뉴그룹A), 상품등록(탕수육)));

        final Order order = 주문등록(createOrder(table, menu));
        주문상태변경(order, OrderStatus.COMPLETION);

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), forUpdateStatus(COOKING)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("changeOrderStatus : 유효하지 않은 주문상태를 입력한 경우 예외가 발생한다.")
    void changeOrderStatus_invalidOrderStatus_throwException() {
        // given
        final OrderTable table = 테이블등록(createOrderTable(3, false));
        final Menu menu = 메뉴등록(createMenu("탕수육_메뉴", 10_000, 메뉴그룹등록(메뉴그룹A), 상품등록(탕수육)));

        final Order order = 주문등록(createOrder(table, menu));

        final Order forUpdate = new Order();
        forUpdate.setOrderStatus("TEST");

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), forUpdate))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
