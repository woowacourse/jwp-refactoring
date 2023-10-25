package kitchenpos.application;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static kitchenpos.support.TestFixtureFactory.새로운_메뉴;
import static kitchenpos.support.TestFixtureFactory.새로운_메뉴_그룹;
import static kitchenpos.support.TestFixtureFactory.새로운_주문;
import static kitchenpos.support.TestFixtureFactory.새로운_주문_테이블;
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
import kitchenpos.order.exception.OrderException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.exception.OrderTableException;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.order.dto.request.OrderCreateRequest;
import kitchenpos.order.dto.request.OrderLineItemCreateRequest;
import kitchenpos.order.dto.request.OrderUpdateRequest;
import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.table.dto.response.OrderTableResponse;
import kitchenpos.order.application.OrderService;
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
            softly.assertThat(등록된_주문_응답.getOrderLineItems().get(0).getMenu()).usingRecursiveComparison()
                    .ignoringFields("id", "orderLineItems")
                    .isEqualTo(MenuResponse.from(메뉴));
            softly.assertThat(등록된_주문_응답.getOrderLineItems().get(0).getQuantity()).isEqualTo(1);
            softly.assertThat(등록된_주문_응답.getOrderStatus()).isEqualTo(COOKING.name());
            softly.assertThat(등록된_주문_응답.getOrderTableResponse())
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(OrderTableResponse.from(주문_테이블));
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
        Long 빈_테이블_ID = orderTableRepository.save(새로운_주문_테이블(null, 1, true)).getId();
        OrderLineItemCreateRequest 주문_항목_생성_요청 = new OrderLineItemCreateRequest(메뉴.getId(), 1);
        OrderCreateRequest 주문_생성_요청 = new OrderCreateRequest(빈_테이블_ID, List.of(주문_항목_생성_요청));

        assertThatThrownBy(() -> orderService.create(주문_생성_요청))
                .isInstanceOf(OrderException.class)
                .hasMessage("빈 테이블을 등록할 수 없습니다.");
    }

    @Test
    void 주문_목록을_조회한다() {
        Order 주문1 = orderRepository.save(새로운_주문(주문_테이블, COOKING, LocalDateTime.now(), List.of(새로운_주문_항목(메뉴, 1))));
        Order 주문2 = orderRepository.save(새로운_주문(주문_테이블, COOKING, LocalDateTime.now(), List.of(새로운_주문_항목(메뉴, 1))));

        List<OrderResponse> 주문_응답_목록 = orderService.readAll();

        assertThat(주문_응답_목록).hasSize(2)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("orderLineItems")
                .containsExactly(OrderResponse.from(주문1), OrderResponse.from(주문2));
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
