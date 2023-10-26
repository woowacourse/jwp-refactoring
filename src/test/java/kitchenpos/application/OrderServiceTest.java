package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.ui.dto.OrderLineItemRequest;
import kitchenpos.ui.dto.OrderRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static kitchenpos.fixture.MenuFixture.양념치킨;
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
        var order = new OrderRequest(테이블1().getId(), null);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문시_주문_항목이_있어야한다() {
        var order = new OrderRequest(테이블1().getId(), new ArrayList<>());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문시_주문_항목의_메뉴는_기존_메뉴여야_한다() {
        tableService.changeEmpty(테이블1().getId(), false);
        var item = new OrderLineItemRequest(-1L, 1);
        var order = new OrderRequest(테이블1().getId(), List.of(item));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문시_기존_테이블에서만_주문할_수_있다() {
        var unsavedTable = 테이블9();

        var item = new OrderLineItemRequest(후라이드치킨().getId(), 1);
        var order = new OrderRequest(unsavedTable.getId(), List.of(item));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문시_빈_테이블이면_안된다() {
        var emptyTable = 테이블1();

        var item = new OrderLineItemRequest(후라이드치킨().getId(), 1);
        var order = new OrderRequest(emptyTable.getId(), List.of(item));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문시_주문_테이블을_지정한다() {
        var fullTable = tableService.changeEmpty(테이블1().getId(), false);

        var item = new OrderLineItemRequest(후라이드치킨().getId(), 1);
        var order = new OrderRequest(fullTable.getId(), List.of(item));

        assertThat(orderService.create(order).getOrderTable().getId()).isEqualTo(fullTable.getId());
    }

    @Test
    void 주문시_조리중으로_바꾼다() {
        var order = orderOneFromTable1(양념치킨());

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @Test
    void 주문시_주문_시각을_기록한다() {
        var startedTime = LocalDateTime.now();

        var order = orderOneFromTable1(후라이드치킨());

        assertThat(order.getOrderedTime()).isBetween(startedTime, LocalDateTime.now());
    }

    @Test
    @Transactional
    void 모든_주문들을_가져온다() {
        Order saved = orderOneFromTable1(후라이드치킨());
        Order other = orderOneFromTable1(양념치킨());

        assertThat(orderService.list())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(saved, other);
    }

    @Test
    void 주문상태_변경시_기존_주문을_사용해야한다() {
        var notLikelyOrderId = Long.MAX_VALUE;

        assertThatThrownBy(() -> orderService.changeOrderStatus(notLikelyOrderId, OrderStatus.COOKING))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문상태_변경시_계산완료_상태이면_안된다() {
        Order ordered = orderOneFromTable1(양념치킨());
        orderService.changeOrderStatus(ordered.getId(), OrderStatus.COMPLETION);

        assertThatThrownBy(() -> orderService.changeOrderStatus(ordered.getId(), OrderStatus.MEAL))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문상태_변경시_변경된_주문을_반환한다() {
        Order ordered = orderOneFromTable1(양념치킨());

        Order changed = orderService.changeOrderStatus(ordered.getId(), OrderStatus.MEAL);

        assertThat(changed.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    private Order orderOneFromTable1(Menu menu) {
        var fullTable = 테이블1();
        try {
            tableService.changeEmpty(테이블1().getId(), false);
        } catch (IllegalArgumentException ignored) {
        }
        var item = new OrderLineItemRequest(menu.getId(), 1);
        var order = new OrderRequest(fullTable.getId(), List.of(item));

        return orderService.create(order);
    }
}
