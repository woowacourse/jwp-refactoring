package kitchenpos.order.application;

import static kitchenpos.support.TestFixtureFactory.새로운_메뉴;
import static kitchenpos.support.TestFixtureFactory.새로운_메뉴_그룹;
import static kitchenpos.support.TestFixtureFactory.새로운_주문;
import static kitchenpos.support.TestFixtureFactory.새로운_주문_메뉴;
import static kitchenpos.support.TestFixtureFactory.새로운_주문_테이블;
import static kitchenpos.support.TestFixtureFactory.새로운_주문_테이블_로그;
import static kitchenpos.support.TestFixtureFactory.새로운_주문_항목;

import kitchenpos.application.OrderService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItemRepository;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableLog;
import kitchenpos.domain.OrderTableLogRepository;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.OrderedMenu;
import kitchenpos.domain.OrderedMenuRepository;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemCreateRequest;
import kitchenpos.dto.request.OrderUpdateRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.exception.OrderException;
import kitchenpos.exception.OrderTableException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.support.ServiceTest;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
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

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(등록된_주문_응답.getId()).isNotNull();
        });
    }

    @Test
    void 주문_항목이_없으면_등록할_수_없다() {
        OrderCreateRequest 주문_생성_요청 = new OrderCreateRequest(주문_테이블.getId(), List.of());

        Assertions.assertThatThrownBy(() -> orderService.create(주문_생성_요청))
                .isInstanceOf(OrderException.class)
                .hasMessage("주문 항목이 없습니다.");
    }

    @Test
    void 생성하려는_주문이_속한_주문_테이블이_존재하지_않으면_예외를_반환한다() {
        OrderLineItemCreateRequest 주문_항목_생성_요청 = new OrderLineItemCreateRequest(메뉴.getId(), 1);
        OrderCreateRequest 주문_생성_요청 = new OrderCreateRequest(Long.MIN_VALUE, List.of(주문_항목_생성_요청));

        Assertions.assertThatThrownBy(() -> orderService.create(주문_생성_요청))
                .isInstanceOf(OrderTableException.class)
                .hasMessage("해당하는 주문 테이블이 없습니다.");
    }

    @Test
    void 빈_테이블이_등록될_수_없다() {
        Long 빈_테이블_ID = orderTableRepository.save(새로운_주문_테이블(null, 0, true)).getId();
        OrderLineItemCreateRequest 주문_항목_생성_요청 = new OrderLineItemCreateRequest(메뉴.getId(), 1);
        OrderCreateRequest 주문_생성_요청 = new OrderCreateRequest(빈_테이블_ID, List.of(주문_항목_생성_요청));

        Assertions.assertThatThrownBy(() -> orderService.create(주문_생성_요청))
                .isInstanceOf(OrderException.class)
                .hasMessage("빈 테이블을 등록할 수 없습니다.");
    }

    @Test
    void 주문_목록을_조회한다() {
        OrderTable 주문_테이블1 = orderTableRepository.save(새로운_주문_테이블(null, 2, false));
        OrderTable 주문_테이블2 = orderTableRepository.save(새로운_주문_테이블(null, 3, false));

        Order 주문1 = orderRepository.save(새로운_주문(OrderStatus.COOKING, LocalDateTime.now()));
        Order 주문2 = orderRepository.save(새로운_주문(OrderStatus.COOKING, LocalDateTime.now()));
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

        Assertions.assertThat(주문_응답_목록).hasSize(2);
    }

    @Test
    void 주문_상태를_변경할_수_있다() {
        OrderLineItemCreateRequest 주문_항목_생성_요청 = new OrderLineItemCreateRequest(메뉴.getId(), 1);
        OrderCreateRequest 주문_생성_요청 = new OrderCreateRequest(주문_테이블.getId(), List.of(주문_항목_생성_요청));
        OrderResponse 등록된_주문_응답 = orderService.create(주문_생성_요청);
        OrderUpdateRequest 주문_상태_변경_요청 = new OrderUpdateRequest(OrderStatus.MEAL.name());
        OrderResponse 주문_상태_변경_응답 = orderService.updateOrderStatus(등록된_주문_응답.getId(), 주문_상태_변경_요청);

        Assertions.assertThat(주문_상태_변경_응답.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    void 존재하지_않는_주문의_상태를_변경할_수_없다() {
        Assertions.assertThatThrownBy(() -> orderService.updateOrderStatus(Long.MIN_VALUE, null))
                .isInstanceOf(OrderException.class)
                .hasMessage("해당하는 주문이 존재하지 않습니다.");
    }

    @Test
    void 완료된_주문의_상태를_변경할_수_없다() {
        OrderLineItemCreateRequest 주문_항목_생성_요청 = new OrderLineItemCreateRequest(메뉴.getId(), 1);
        OrderCreateRequest 주문_생성_요청 = new OrderCreateRequest(주문_테이블.getId(), List.of(주문_항목_생성_요청));
        OrderResponse 등록된_주문_응답 = orderService.create(주문_생성_요청);
        OrderUpdateRequest 주문_상태_변경_요청 = new OrderUpdateRequest(OrderStatus.COMPLETION.name());
        OrderResponse 주문_상태_변경_응답 = orderService.updateOrderStatus(등록된_주문_응답.getId(), 주문_상태_변경_요청);

        OrderUpdateRequest 완료된_주문_상태_변경_요청 = new OrderUpdateRequest(OrderStatus.MEAL.name());

        Assertions.assertThatThrownBy(() -> orderService.updateOrderStatus(주문_상태_변경_응답.getId(), 완료된_주문_상태_변경_요청))
                .isInstanceOf(OrderException.class)
                .hasMessage("이미 완료된 주문입니다.");
    }

}
