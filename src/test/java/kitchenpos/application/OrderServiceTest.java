package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.MEAL;
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
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
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
    private OrderService orderService;

    private MenuGroup 메뉴_그룹;
    private Menu 메뉴;
    private OrderTable 주문_테이블;
    private OrderLineItem 주문_항목;

    @BeforeEach
    void setUp() {
        메뉴_그룹 = menuGroupRepository.save(새로운_메뉴_그룹("메뉴 그룹"));
        메뉴 = menuRepository.save(새로운_메뉴("메뉴", new BigDecimal("30000.00"), 메뉴_그룹.getId(), null));
        주문_테이블 = orderTableRepository.save(새로운_주문_테이블(null, 1, false));
        주문_항목 = 새로운_주문_항목(null, 메뉴.getId(), 1);
    }

    @Test
    void 주문을_등록한다() {
        Order 주문 = 새로운_주문(주문_테이블.getId(), null, LocalDateTime.now(), List.of(주문_항목));

        Order 등록된_주문 = orderService.create(주문);

        assertSoftly(softly -> {
            softly.assertThat(등록된_주문.getId()).isNotNull();
            softly.assertThat(등록된_주문).usingRecursiveComparison()
                    .ignoringFields("id", "orderLineItems")
                    .isEqualTo(주문);
            softly.assertThat(등록된_주문.getOrderLineItems()).hasSize(1)
                    .usingRecursiveFieldByFieldElementComparatorIgnoringFields("seq")
                    .containsOnly(주문_항목);
        });
    }

    @Test
    void 주문_항목이_없으면_등록할_수_없다() {
        Order 주문 = 새로운_주문(주문_테이블.getId(), null, LocalDateTime.now(), List.of());

        assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성하려는_주문이_속한_주문_테이블이_존재하지_않으면_예외를_반환한다() {
        Order 주문 = 새로운_주문(Long.MIN_VALUE, null, LocalDateTime.now(), List.of(주문_항목));

        assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 빈_테이블이_등록될_수_없다() {
        Long 빈_테이블_ID = orderTableRepository.save(새로운_주문_테이블(null, 1, true)).getId();
        Order 빈_테이블이_포함된_주문 = 새로운_주문(빈_테이블_ID, null, LocalDateTime.now(), List.of(주문_항목));

        assertThatThrownBy(() -> orderService.create(빈_테이블이_포함된_주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_목록을_조회한다() {
        Order 주문1 = orderService.create(새로운_주문(주문_테이블.getId(), null, LocalDateTime.now(), List.of(주문_항목)));
        Order 주문2 = orderService.create(새로운_주문(주문_테이블.getId(), null, LocalDateTime.now(), List.of(주문_항목)));

        List<Order> 주문_목록 = orderService.list();

        assertThat(주문_목록).hasSize(2)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("orderLineItems")
                .containsExactly(주문1, 주문2);
    }

    @Test
    void 주문_상태를_변경할_수_있다() {
        Order 주문 = orderService.create(새로운_주문(주문_테이블.getId(), null, LocalDateTime.now(), List.of(주문_항목)));
        주문.setOrderStatus(MEAL.name());

        Order 변경된_주문 = orderService.changeOrderStatus(주문.getId(), 주문);

        assertThat(변경된_주문.getOrderStatus()).isEqualTo(MEAL.name());
    }

    @Test
    void 존재하지_않는_주문의_상태를_변경할_수_없다() {
        assertThatThrownBy(() -> orderService.changeOrderStatus(Long.MIN_VALUE, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 완료된_주문의_상태를_변경할_수_없다() {
        Order 주문 = orderService.create(새로운_주문(주문_테이블.getId(), null, LocalDateTime.now(), List.of(주문_항목)));
        주문.setOrderStatus(COMPLETION.name());
        orderService.changeOrderStatus(주문.getId(), 주문);

        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
