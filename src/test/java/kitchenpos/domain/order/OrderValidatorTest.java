package kitchenpos.domain.order;

import static kitchenpos.fixture.MenuFixture.메뉴;
import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품;
import static kitchenpos.fixture.OrderTableFixture.테이블;
import static kitchenpos.fixture.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.test.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ServiceTest
class OrderValidatorTest {

    @Autowired
    private OrderValidator sut;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    private Menu menu;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹("피자"));
        Product product = productRepository.save(상품("치즈 피자", 8900L));
        MenuProduct menuProduct = 메뉴_상품(product, 1L);
        menu = menuRepository.save(메뉴("피자", 8900L, menuGroup.getId(), List.of(menuProduct)));
    }

    @Test
    void 주문_항목_목록이_없다면_예외를_던진다() {
        // given
        Order order = new Order(1L, List.of());

        // expect
        assertThatThrownBy(() -> sut.validate(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목 목록이 있어야 합니다.");
    }

    @Test
    void 등록되지_않은_테이블에서_주문을_하는_경우_예외를_던진다() {
        // given
        Order order = new Order(1L, List.of(new OrderLineItem(menu.getId(), 1)));

        // expect
        assertThatThrownBy(() -> sut.validate(order))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("존재하지 않는 테이블입니다.");
    }

    @Test
    void 빈_테이블에서_주문을_하는_경우_예외를_던진다() {
        // given
        OrderTable orderTable = orderTableRepository.save(테이블(true));
        Order order = new Order(orderTable.getId(), List.of(new OrderLineItem(menu.getId(), 1)));

        // expect
        assertThatThrownBy(() -> sut.validate(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블인 경우 주문을 할 수 없습니다.");
    }
}
