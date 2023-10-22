package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static kitchenpos.fixture.MenuFixture.양념치킨;
import static kitchenpos.fixture.MenuFixture.후라이드_두마리;
import static kitchenpos.fixture.MenuFixture.후라이드치킨;
import static kitchenpos.fixture.OrderTableFixture.테이블1;
import static kitchenpos.fixture.OrderTableFixture.테이블9;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private TableService tableService;

    @Test
    void 주문시_주문_항목이_null이면_안된다() {
        var order = new Order();
        order.setOrderLineItems(null);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문시_주문_항목이_있어야한다() {
        var order = new Order();
        order.setOrderLineItems(new ArrayList<>());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문시_주문_항목의_메뉴는_기존_메뉴여야_한다() {
        Menu unsaved = 후라이드_두마리();

        var order = new Order();
        var item = new OrderLineItem();
        item.setMenuId(unsaved.getId());
        order.setOrderLineItems(List.of(item));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문시_주문_Id를_직접_지정할_수_없다() {
        var fullTable = 테이블1();
        fullTable.setEmpty(false);
        tableService.changeEmpty(fullTable.getId(), fullTable);
        var order = new Order();
        order.setOrderTableId(fullTable.getId());
        var item = new OrderLineItem();
        item.setMenuId(후라이드치킨().getId());
        order.setOrderLineItems(List.of(item));

        order.setId(Long.MAX_VALUE);

        assertThat(orderService.create(order).getId()).isNotEqualTo(Long.MAX_VALUE);
    }

    @Test
    void 주문시_기존_테이블에서만_주문할_수_있다() {
        var unsavedTable = 테이블9();

        var order = new Order();
        order.setOrderTableId(unsavedTable.getId());
        var item = new OrderLineItem();
        item.setMenuId(후라이드치킨().getId());
        order.setOrderLineItems(List.of(item));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문시_빈_테이블이면_안된다() {
        var emptyTable = 테이블1();

        var order = new Order();
        order.setOrderTableId(emptyTable.getId());
        var item = new OrderLineItem();
        item.setMenuId(후라이드치킨().getId());
        order.setOrderLineItems(List.of(item));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문시_주문_테이블을_지정한다() {
        var fullTable = 테이블1();
        fullTable.setEmpty(false);
        tableService.changeEmpty(fullTable.getId(), fullTable);
        var order = new Order();
        order.setOrderTableId(fullTable.getId());
        var item = new OrderLineItem();
        item.setMenuId(후라이드치킨().getId());
        order.setOrderLineItems(List.of(item));

        assertThat(orderService.create(order).getOrderTableId()).isEqualTo(fullTable.getId());
    }

    @Test
    void 주문시_조리중으로_바꾼다() {
        var order = orderFromTable1(양념치킨());

        assertThat(orderService.create(order).getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    void 주문시_주문_시각을_기록한다() {
        var startedTime = LocalDateTime.now();

        var order = orderFromTable1(후라이드치킨());

        assertThat(orderService.create(order).getOrderedTime()).isBetween(startedTime, LocalDateTime.now());
    }

    @Test
    void 모든_주문들을_가져온다() {
        Order saved = orderFromTable1(후라이드치킨());
        Order other = orderFromTable1(양념치킨());

        assertThat(orderService.list())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(saved, other);
    }

    @Test
    void 주문상태_변경시_기존_주문을_사용해야한다() {
        var notLikelyOrderId = Long.MAX_VALUE;

        assertThatThrownBy(() -> orderService.changeOrderStatus(notLikelyOrderId, new Order()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문상태_변경시_계산완료_상태이면_안된다() {
        Order ordered = orderFromTable1(양념치킨());
        ordered.setOrderStatus(OrderStatus.COMPLETION.name());
        orderService.changeOrderStatus(ordered.getId(), ordered);

        assertThatThrownBy(() -> orderService.changeOrderStatus(ordered.getId(), new Order()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문상태_변경시_변경된_주문을_반환한다() {
        Order ordered = orderFromTable1(양념치킨());
        ordered.setOrderStatus(OrderStatus.MEAL.name());

        Order changed = orderService.changeOrderStatus(ordered.getId(), ordered);

        assertThat(changed.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    private Order orderFromTable1(Menu menu) {
        var fullTable = 테이블1();
        fullTable.setEmpty(false);
        try {
            tableService.changeEmpty(fullTable.getId(), fullTable);
        } catch (IllegalArgumentException ignored) {
        }
        var order = new Order();
        order.setOrderTableId(fullTable.getId());
        var item = new OrderLineItem();
        item.setMenuId(menu.getId());
        order.setOrderLineItems(List.of(item));

        return orderService.create(order);
    }
}
