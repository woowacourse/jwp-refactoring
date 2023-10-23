package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.fixture.MenuFixture.메뉴;
import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품;
import static kitchenpos.fixture.OrderFixture.주문;
import static kitchenpos.fixture.OrderLineItemFixture.주문_항목;
import static kitchenpos.fixture.OrderTableFixture.테이블;
import static kitchenpos.fixture.ProductFixture.상품;
import static kitchenpos.fixture.TableGroupFixture.단체_지정;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupUngroupedEvent;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.table.OrderTable;
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
public class TableGroupUngroupedEventHandlerTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

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
    @ParameterizedTest(name = "테이블에 해당하는 주문 상태가 {0}인 경우 예외를 던진다")
    void 테이블의_그룹_해제_시_해당_테이블을_사용하는_주문의_상태가_하나라도_조리중이거나_식사중인_경우_예외를_던진다(OrderStatus orderStatus) {
        // given
        TableGroup tableGroup = tableGroupRepository.save(단체_지정());
        OrderTable orderTable1 = orderTableRepository.save(테이블(false, 0, tableGroup));
        OrderTable orderTable2 = orderTableRepository.save(테이블(false, 0, tableGroup));
        OrderLineItem orderLineItem1 = 주문_항목(menu.getId(), 2);
        OrderLineItem orderLineItem2 = 주문_항목(menu.getId(), 2);
        orderRepository.save(주문(orderTable1, COMPLETION, List.of(orderLineItem1)));
        orderRepository.save(주문(orderTable2, orderStatus, List.of(orderLineItem2)));

        // expect
        assertThatThrownBy(() -> eventPublisher.publishEvent(new TableGroupUngroupedEvent(tableGroup.getId())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블의 주문 상태가 조리중이거나 식사중인 경우 단체 지정 해제를 할 수 없습니다.");
    }

    @Test
    void 테이블의_그룹_해제_시_해당_테이블을_사용하는_주문이_없는_경우_예외를_던지지_않는다() {
        // given
        TableGroup tableGroup = tableGroupRepository.save(단체_지정());

        // expect
        assertThatNoException()
                .isThrownBy(() -> eventPublisher.publishEvent(new TableGroupUngroupedEvent(tableGroup.getId())));
    }

    @Test
    void 테이블의_그룹_해제_시_해당_테이블을_사용하는_모든_주문의_상태가_완료된_경우_예외를_던지지_않는다() {
        // given
        TableGroup tableGroup = tableGroupRepository.save(단체_지정());
        OrderTable orderTable1 = orderTableRepository.save(테이블(false, 0, tableGroup));
        OrderTable orderTable2 = orderTableRepository.save(테이블(false, 0, tableGroup));
        OrderLineItem orderLineItem1 = 주문_항목(menu.getId(), 2);
        OrderLineItem orderLineItem2 = 주문_항목(menu.getId(), 2);
        orderRepository.save(주문(orderTable1, COMPLETION, List.of(orderLineItem1)));
        orderRepository.save(주문(orderTable2, COMPLETION, List.of(orderLineItem2)));

        // expect
        assertThatNoException()
                .isThrownBy(() -> eventPublisher.publishEvent(new TableGroupUngroupedEvent(tableGroup.getId())));
    }
}
