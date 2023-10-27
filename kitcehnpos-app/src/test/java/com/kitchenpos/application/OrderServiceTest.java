package com.kitchenpos.application;

import com.kitchenpos.application.dto.OrderCreateRequest;
import com.kitchenpos.application.dto.OrderLineItemCreateRequest;
import com.kitchenpos.domain.Menu;
import com.kitchenpos.domain.MenuGroup;
import com.kitchenpos.domain.MenuGroupRepository;
import com.kitchenpos.domain.MenuRepository;
import com.kitchenpos.domain.Order;
import com.kitchenpos.domain.OrderLineItem;
import com.kitchenpos.domain.OrderRepository;
import com.kitchenpos.domain.OrderTable;
import com.kitchenpos.domain.OrderTableRepository;
import com.kitchenpos.exception.MenuNotFoundException;
import com.kitchenpos.exception.OrderLineItemEmptyException;
import com.kitchenpos.exception.OrderTableEmptyException;
import com.kitchenpos.exception.OrderTableNotFoundException;
import com.kitchenpos.helper.IntegrationTestHelper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.kitchenpos.domain.OrderStatus.COOKING;
import static com.kitchenpos.fixture.MenuFixture.메뉴_생성;
import static com.kitchenpos.fixture.MenuGroupFixture.메뉴_그룹_생성;
import static com.kitchenpos.fixture.OrderFixture.주문_생성;
import static com.kitchenpos.fixture.OrderFixture.주문_생성_요청;
import static com.kitchenpos.fixture.OrderFixture.주문_생성_요청_잘못된_주문_테이블;
import static com.kitchenpos.fixture.OrderFixture.주문_업데이트_요청;
import static com.kitchenpos.fixture.OrderLineItemFixture.주문_품목_생성;
import static com.kitchenpos.fixture.OrderTableFixture.주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest extends IntegrationTestHelper {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void 주문을_생성한다() {
        // given
        MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹_생성());
        Menu menu = menuRepository.save(메뉴_생성("메뉴", 100L, menuGroup.getId(), List.of()));
        OrderTable orderTable = orderTableRepository.save(주문_테이블_생성(1, false));

        OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(menu.getId(), 1);

        OrderCreateRequest req = new OrderCreateRequest(
                orderTable.getId(),
                List.of(orderLineItemCreateRequest)
        );

        // when
        Order result = orderService.create(req);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isEqualTo(1L);
            softly.assertThat(result.getOrderStatus()).isNotNull();
            softly.assertThat(result.getOrderLineItems()).hasSize(1);
            softly.assertThat(result.getOrderLineItems().get(0).getQuantity())
                    .isEqualTo(req.getOrderLineItems().size());
        });
    }

    @Test
    void 주문_품목이_비어있다면_예외를_발생시킨다() {
        // given
        OrderTable orderTable = orderTableRepository.save(주문_테이블_생성(1, false));
        OrderCreateRequest req = 주문_생성_요청(orderTable, List.of());

        // when & then
        assertThatThrownBy(() -> orderService.create(req))
                .isInstanceOf(OrderLineItemEmptyException.class);
    }

    @Test
    void 주문_품목이_주문_테이블에_있지_않으면_예외를_발생시킨다() {
        // given
        Menu menu = new Menu("name", 100L, null, List.of());

        OrderTable orderTable = orderTableRepository.save(주문_테이블_생성(1, false));
        OrderLineItem orderLineItem = 주문_품목_생성(-100L, 1L);
        OrderCreateRequest req = 주문_생성_요청(orderTable, List.of(orderLineItem));

        // when & then
        assertThatThrownBy(() -> orderService.create(req))
                .isInstanceOf(MenuNotFoundException.class);
    }

    @Test
    void 주문_테이블이_없다면_예외를_발생한다() {
        // given
        OrderTable orderTable = orderTableRepository.save(new OrderTable(10, true));

        MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹_생성("그룹"));
        Menu menu = menuRepository.save(메뉴_생성("메뉴", 1000L, menuGroup.getId(), List.of()));
        OrderLineItem orderLineItem = 주문_품목_생성(menu.getId(), 1L);
        OrderCreateRequest req = 주문_생성_요청_잘못된_주문_테이블(orderTable, List.of(orderLineItem));

        // when & then
        assertThatThrownBy(() -> orderService.create(req))
                .isInstanceOf(OrderTableNotFoundException.class);
    }

    @Test
    void 주문_테이블이_비어있다면_예외를_발생한다() {
        // given
        MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹_생성("그룹"));
        Menu menu = menuRepository.save(메뉴_생성("메뉴", 1000L, menuGroup.getId(), List.of()));
        OrderTable orderTable = orderTableRepository.save(주문_테이블_생성(1, true));
        OrderLineItem orderLineItem = 주문_품목_생성(menu.getId(), 1L);
        OrderCreateRequest req = 주문_생성_요청(orderTable, List.of(orderLineItem));

        // when & then
        assertThatThrownBy(() -> orderService.create(req))
                .isInstanceOf(OrderTableEmptyException.class);
    }

    @Test
    void 주문을_모두_조회한다() {
        MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹_생성());
        Menu menu = menuRepository.save(메뉴_생성("메뉴", 100L, menuGroup.getId(), List.of()));
        OrderTable orderTable = orderTableRepository.save(주문_테이블_생성(1, false));
        orderRepository.save(주문_생성(orderTable.getId(), COOKING.name(), List.of(주문_품목_생성(menu.getId(), 1L))));

        // when
        List<Order> result = orderService.list();

        // then
        assertThat(result).hasSize(1);
    }

    @Test
    void 주문의_상태를_변경한다() {
        // given
        String orderStatus = COOKING.name();

        MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹_생성("그룹"));
        Menu menu = menuRepository.save(메뉴_생성("메뉴", 1000L, menuGroup.getId(), List.of()));
        OrderTable orderTable = orderTableRepository.save(주문_테이블_생성(1, false));
        OrderLineItem orderLineItem = 주문_품목_생성(menu.getId(), 1L);

        Order changedOrder = 주문_생성(orderTable.getId(), orderStatus, List.of(orderLineItem));
        OrderCreateRequest req = 주문_생성_요청(orderTable, List.of(orderLineItem));
        Order savedOrder = orderService.create(req);

        // when
        Order result = orderService.changeOrderStatus(savedOrder.getId(), 주문_업데이트_요청(changedOrder));

        // then
        assertThat(result.getOrderStatus()).isEqualTo(orderStatus);
    }
}
