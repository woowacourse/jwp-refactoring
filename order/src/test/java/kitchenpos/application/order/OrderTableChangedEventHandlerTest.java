package kitchenpos.application.order;

import static kitchenpos.domain.order.OrderStatus.COMPLETION;
import static kitchenpos.fixture.MenuFixture.메뉴;
import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품;
import static kitchenpos.fixture.OrderFixture.주문;
import static kitchenpos.fixture.OrderLineItemFixture.주문_항목;
import static kitchenpos.fixture.OrderTableFixture.테이블;
import static kitchenpos.fixture.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableChangedEvent;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.test.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class OrderTableChangedEventHandlerTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private Menu menu;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹("피자"));
        Product product = productRepository.save(상품("치즈 피자", 8900L));
        MenuProduct menuProduct = 메뉴_상품(product, 1L);
        menu = menuRepository.save(메뉴("피자", 8900L, menuGroup.getId(), List.of(menuProduct)));
    }

    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    @ParameterizedTest(name = "주문 상태가 {0}인 경우 예외를 던진다")
    void 테이블의_상태_변경_시_해당_테이블을_사용하는_주문의_상태가_조리중이거나_식사중인_경우_예외를_던진다(OrderStatus orderStatus) {
        // given
        OrderTable orderTable = orderTableRepository.save(테이블(false));
        OrderLineItem orderLineItem = 주문_항목(menu, 2L);
        orderRepository.save(주문(orderTable, orderStatus, List.of(orderLineItem)));

        // expect
        assertThatThrownBy(() -> eventPublisher.publishEvent(new OrderTableChangedEvent(orderTable.getId())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블의 주문 상태가 조리중이거나 식사중인 경우 테이블의 상태를 변경할 수 없습니다.");
    }

    @Test
    void 테이블의_상태_변경_시_해당_테이블을_사용하는_주문이_없는_경우_예외를_던지지_않는다() {
        // given
        OrderTable orderTable = orderTableRepository.save(테이블(false));

        // expect
        assertThatNoException()
                .isThrownBy(() -> eventPublisher.publishEvent(new OrderTableChangedEvent(orderTable.getId())));
    }

    @Test
    void 테이블의_상태_변경_시_해당_테이블을_사용하는_주문의_상태가_완료된_경우_예외를_던지지_않는다() {
        // given
        OrderTable orderTable = orderTableRepository.save(테이블(false));
        OrderLineItem orderLineItem = 주문_항목(menu, 2L);
        orderRepository.save(주문(orderTable, COMPLETION, List.of(orderLineItem)));

        // expect
        assertThatNoException()
                .isThrownBy(() -> eventPublisher.publishEvent(new OrderTableChangedEvent(orderTable.getId())));
    }
}
