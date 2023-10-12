package kitchenpos.application;

import kitchenpos.application.order.OrderService;
import kitchenpos.application.order.dto.OrderCreateRequest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.helper.IntegrationTestHelper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.fixture.MenuFixture.메뉴_생성;
import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹_생성;
import static kitchenpos.fixture.OrderFixture.주문_생성;
import static kitchenpos.fixture.OrderFixture.주문_생성_요청;
import static kitchenpos.fixture.OrderFixture.주문_업데이트_요청;
import static kitchenpos.fixture.OrderLineItemFixture.주문_품목_생성;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest extends IntegrationTestHelper {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Test
    void 주문을_생성한다() {
        // given
        MenuGroup menuGroup = menuGroupDao.save(메뉴_그룹_생성("그룹"));
        Menu menu = menuDao.save(메뉴_생성("메뉴", new BigDecimal(1000), menuGroup.getId(), null));
        OrderTable orderTable = orderTableDao.save(주문_테이블_생성(null, 1, false));
        OrderLineItem orderLineItem = 주문_품목_생성(null, menu.getId(), 1);
        Order order = 주문_생성(orderTable.getId(), null, LocalDateTime.now(), List.of(orderLineItem));
        OrderCreateRequest req = 주문_생성_요청(order);

        // when
        Order result = orderService.create(req);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isEqualTo(1L);
            softly.assertThat(result.getOrderStatus()).isNotNull();
            softly.assertThat(result.getOrderLineItems()).hasSize(1);
            softly.assertThat(result.getOrderLineItems().get(0).getQuantity())
                    .isEqualTo(orderLineItem.getQuantity());
        });
    }

    @Test
    void 주문_품목이_비어있다면_예외를_발생시킨다() {
        // given
        OrderTable orderTable = orderTableDao.save(주문_테이블_생성(null, 1, false));
        Order order = 주문_생성(orderTable.getId(), null, LocalDateTime.now(), List.of());
        OrderCreateRequest req = 주문_생성_요청(order);

        // when & then
        assertThatThrownBy(() -> orderService.create(req))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_품목이_주문_테이블에_있지_않으면_예외를_발생시킨다() {
        // given
        Long invalidMenuId = -1L;

        OrderTable orderTable = orderTableDao.save(주문_테이블_생성(null, 1, false));
        OrderLineItem orderLineItem = 주문_품목_생성(null, invalidMenuId, 1);
        Order order = 주문_생성(orderTable.getId(), null, LocalDateTime.now(), List.of(orderLineItem));
        OrderCreateRequest req = 주문_생성_요청(order);

        // when & then
        assertThatThrownBy(() -> orderService.create(req))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_없다면_예외를_발생한다() {
        // given
        Long invalidOrderTableId = -1L;

        MenuGroup menuGroup = menuGroupDao.save(메뉴_그룹_생성("그룹"));
        Menu menu = menuDao.save(메뉴_생성("메뉴", new BigDecimal(1000), menuGroup.getId(), null));
        OrderLineItem orderLineItem = 주문_품목_생성(null, menu.getId(), 1);
        Order order = 주문_생성(invalidOrderTableId, null, LocalDateTime.now(), List.of(orderLineItem));
        OrderCreateRequest req = 주문_생성_요청(order);

        // when & then
        assertThatThrownBy(() -> orderService.create(req))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_비어있다면_예외를_발생한다() {
        // given
        MenuGroup menuGroup = menuGroupDao.save(메뉴_그룹_생성("그룹"));
        Menu menu = menuDao.save(메뉴_생성("메뉴", new BigDecimal(1000), menuGroup.getId(), null));
        OrderTable orderTable = orderTableDao.save(주문_테이블_생성(null, 1, true));
        OrderLineItem orderLineItem = 주문_품목_생성(null, menu.getId(), 1);
        Order order = 주문_생성(orderTable.getId(), null, LocalDateTime.now(), List.of(orderLineItem));
        OrderCreateRequest req = 주문_생성_요청(order);

        // when & then
        assertThatThrownBy(() -> orderService.create(req))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_모두_조회한다() {
        // given
        MenuGroup menuGroup = menuGroupDao.save(메뉴_그룹_생성("그룹"));
        Menu menu = menuDao.save(메뉴_생성("메뉴", new BigDecimal(1000), menuGroup.getId(), null));
        OrderTable orderTable = orderTableDao.save(주문_테이블_생성(null, 1, false));
        OrderLineItem orderLineItem = 주문_품목_생성(null, menu.getId(), 1);
        Order order = 주문_생성(orderTable.getId(), null, LocalDateTime.now(), List.of(orderLineItem));
        OrderCreateRequest req = 주문_생성_요청(order);

        Order savedOrder = orderService.create(req);

        // when
        List<Order> result = orderService.list();

        // then
        assertSoftly(softly -> {
            softly.assertThat(result).hasSize(1);
            softly.assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(savedOrder);
        });
    }

    @Test
    void 주문의_상태를_변경한다() {
        // given
        String orderStatus = COOKING.name();

        MenuGroup menuGroup = menuGroupDao.save(메뉴_그룹_생성("그룹"));
        Menu menu = menuDao.save(메뉴_생성("메뉴", new BigDecimal(1000), menuGroup.getId(), null));
        OrderTable orderTable = orderTableDao.save(주문_테이블_생성(null, 1, false));
        OrderLineItem orderLineItem = 주문_품목_생성(null, menu.getId(), 1);

        Order order = 주문_생성(orderTable.getId(), null, LocalDateTime.now(), List.of(orderLineItem));
        Order changedOrder = 주문_생성(orderTable.getId(), orderStatus, LocalDateTime.now(), List.of(orderLineItem));
        OrderCreateRequest req = 주문_생성_요청(order);
        Order savedOrder = orderService.create(req);

        // when
        Order result = orderService.changeOrderStatus(savedOrder.getId(), 주문_업데이트_요청(changedOrder));

        // then
        assertThat(result.getOrderStatus()).isEqualTo(orderStatus);
    }
}
