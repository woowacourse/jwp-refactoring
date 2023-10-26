package kitchenpos.order.application;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static kitchenpos.support.TestFixtureFactory.새로운_메뉴;
import static kitchenpos.support.TestFixtureFactory.새로운_메뉴_그룹;
import static kitchenpos.support.TestFixtureFactory.새로운_주문;
import static kitchenpos.support.TestFixtureFactory.새로운_주문_메뉴;
import static kitchenpos.support.TestFixtureFactory.새로운_주문_테이블;
import static kitchenpos.support.TestFixtureFactory.새로운_주문_테이블_로그;
import static kitchenpos.support.TestFixtureFactory.새로운_주문_항목;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderTableLog;
import kitchenpos.order.domain.OrderTableLogRepository;
import kitchenpos.order.domain.OrderedMenu;
import kitchenpos.order.domain.OrderedMenuRepository;
import kitchenpos.order.dto.request.OrderCreateRequest;
import kitchenpos.order.dto.request.OrderLineItemCreateRequest;
import kitchenpos.order.dto.request.OrderUpdateRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.order.exception.OrderException;
import kitchenpos.support.ServiceTest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.exception.OrderTableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class OrderServiceTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableLogRepository orderTableLogRepository;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private OrderedMenuRepository orderedMenuRepository;

    @Autowired
    private OrderService orderService;

    private MenuGroup 메뉴_그룹;
    private Menu 메뉴;
    private OrderTable 주문_테이블;

    @BeforeEach
    void setUp() {
        메뉴_그룹 = menuGroupRepository.save(새로운_메뉴_그룹(null, "메뉴 그룹"));
        메뉴 = menuRepository.save(새로운_메뉴("메뉴", new BigDecimal("30000.00"), 메뉴_그룹));
        주문_테이블 = orderTableRepository.save(새로운_주문_테이블(null, 1, false));
    }

    @Test
    void 주문을_등록한다() {
        OrderLineItemCreateRequest 주문_항목_생성_요청 = new OrderLineItemCreateRequest(메뉴.getId(), 1);
        OrderCreateRequest 주문_생성_요청 = new OrderCreateRequest(주문_테이블.getId(), List.of(주문_항목_생성_요청));
        OrderResponse 등록된_주문_응답 = orderService.create(주문_생성_요청);

        assertSoftly(softly -> {
            softly.assertThat(등록된_주문_응답.getId()).isNotNull();
        });
    }

    @Test
    void 주문_항목이_없으면_등록할_수_없다() {
        OrderCreateRequest 주문_생성_요청 = new OrderCreateRequest(주문_테이블.getId(), List.of());

        assertThatThrownBy(() -> orderService.create(주문_생성_요청))
                .isInstanceOf(OrderException.class)
                .hasMessage("주문 항목이 없습니다.");
    }

    @Test
    void 생성하려는_주문이_속한_주문_테이블이_존재하지_않으면_예외를_반환한다() {
        OrderLineItemCreateRequest 주문_항목_생성_요청 = new OrderLineItemCreateRequest(메뉴.getId(), 1);
        OrderCreateRequest 주문_생성_요청 = new OrderCreateRequest(Long.MIN_VALUE, List.of(주문_항목_생성_요청));

        assertThatThrownBy(() -> orderService.create(주문_생성_요청))
                .isInstanceOf(OrderTableException.class)
                .hasMessage("해당하는 주문 테이블이 없습니다.");
    }

    @Test
    void 빈_테이블이_등록될_수_없다() {
        Long 빈_테이블_ID = orderTableRepository.save(새로운_주문_테이블(null, 0, true)).getId();
        OrderLineItemCreateRequest 주문_항목_생성_요청 = new OrderLineItemCreateRequest(메뉴.getId(), 1);
        OrderCreateRequest 주문_생성_요청 = new OrderCreateRequest(빈_테이블_ID, List.of(주문_항목_생성_요청));

        assertThatThrownBy(() -> orderService.create(주문_생성_요청))
                .isInstanceOf(OrderException.class)
                .hasMessage("빈 테이블을 등록할 수 없습니다.");
    }

    @Test
    void 주문_목록을_조회한다() {
        OrderTable 주문_테이블1 = orderTableRepository.save(새로운_주문_테이블(null, 2, false));
        OrderTable 주문_테이블2 = orderTableRepository.save(새로운_주문_테이블(null, 3, false));

        Order 주문1 = orderRepository.save(새로운_주문(COOKING, LocalDateTime.now()));
        Order 주문2 = orderRepository.save(새로운_주문(COOKING, LocalDateTime.now()));
        OrderTableLog 주문_테이블_로그1 = orderTableLogRepository.save(
                새로운_주문_테이블_로그(주문1, 주문_테이블1.getId(), 주문_테이블1.getNumberOfGuests()));
        OrderTableLog 주문_테이블_로그2 = orderTableLogRepository.save(
                새로운_주문_테이블_로그(주문2, 주문_테이블2.getId(), 주문_테이블2.getNumberOfGuests()));
        OrderedMenu 주문_메뉴1 = orderedMenuRepository.save(새로운_주문_메뉴("메뉴", BigDecimal.TEN, 메뉴_그룹));
        OrderedMenu 주문_메뉴2 = orderedMenuRepository.save(새로운_주문_메뉴("메뉴", BigDecimal.TEN, 메뉴_그룹));
        OrderLineItem 주문_항목1 = orderLineItemRepository.save(
                새로운_주문_항목(주문1, 주문_메뉴1, 3));
        OrderLineItem 주문_항목2 = orderLineItemRepository.save(
                새로운_주문_항목(주문1, 주문_메뉴2, 3));

        List<OrderResponse> 주문_응답_목록 = orderService.readAll();

        assertThat(주문_응답_목록).hasSize(2);
    }

    @Test
    void 주문_상태를_변경할_수_있다() {
        OrderLineItemCreateRequest 주문_항목_생성_요청 = new OrderLineItemCreateRequest(메뉴.getId(), 1);
        OrderCreateRequest 주문_생성_요청 = new OrderCreateRequest(주문_테이블.getId(), List.of(주문_항목_생성_요청));
        OrderResponse 등록된_주문_응답 = orderService.create(주문_생성_요청);
        OrderUpdateRequest 주문_상태_변경_요청 = new OrderUpdateRequest(MEAL.name());
        OrderResponse 주문_상태_변경_응답 = orderService.updateOrderStatus(등록된_주문_응답.getId(), 주문_상태_변경_요청);

        assertThat(주문_상태_변경_응답.getOrderStatus()).isEqualTo(MEAL.name());
    }

    @Test
    void 존재하지_않는_주문의_상태를_변경할_수_없다() {
        assertThatThrownBy(() -> orderService.updateOrderStatus(Long.MIN_VALUE, null))
                .isInstanceOf(OrderException.class)
                .hasMessage("해당하는 주문이 존재하지 않습니다.");
    }

    @Test
    void 완료된_주문의_상태를_변경할_수_없다() {
        OrderLineItemCreateRequest 주문_항목_생성_요청 = new OrderLineItemCreateRequest(메뉴.getId(), 1);
        OrderCreateRequest 주문_생성_요청 = new OrderCreateRequest(주문_테이블.getId(), List.of(주문_항목_생성_요청));
        OrderResponse 등록된_주문_응답 = orderService.create(주문_생성_요청);
        OrderUpdateRequest 주문_상태_변경_요청 = new OrderUpdateRequest(COMPLETION.name());
        OrderResponse 주문_상태_변경_응답 = orderService.updateOrderStatus(등록된_주문_응답.getId(), 주문_상태_변경_요청);

        OrderUpdateRequest 완료된_주문_상태_변경_요청 = new OrderUpdateRequest(MEAL.name());

        assertThatThrownBy(() -> orderService.updateOrderStatus(주문_상태_변경_응답.getId(), 완료된_주문_상태_변경_요청))
                .isInstanceOf(OrderException.class)
                .hasMessage("이미 완료된 주문입니다.");
    }

}
