package kitchenpos.domain.order;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.menu.Product;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
class OrderValidatorTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private OrderValidator orderValidator;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    private OrderLineItem 주문_항목;

    @BeforeEach
    void setUp() {
        final Product 후라이드 = productRepository.save(new Product("후라이드", BigDecimal.valueOf(16000)));
        final MenuGroup 두마리메뉴 = menuGroupRepository.save(new MenuGroup("두마리메뉴"));
        final MenuProduct 후라이드_2개 = new MenuProduct(후라이드, 2L);
        final Menu 후라이드_2개_메뉴 = menuRepository.save(new Menu("후라이드+후라이드", BigDecimal.valueOf(30000), 두마리메뉴.getId()));
        후라이드_2개_메뉴.addMenuProducts(new MenuProducts(List.of(후라이드_2개)));
        주문_항목 = new OrderLineItem(후라이드_2개_메뉴.getId(), 2);
    }

    @Test
    @DisplayName("주어진 주문 테이블 아이디에 해당하는 주문 테이블이 존재하고 비어있지 않다면 예외가 발생하지 않는다.")
    void validate() {
        // given
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(3, false));

        em.flush();
        em.clear();

        final Order order = new Order(orderTable.getId(), List.of(주문_항목));

        // when & then
        assertThatCode(() -> orderValidator.validate(order))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("주어진 주문 테이블 아이디에 해당하는 주문테이블을 찾을 수 없으면 예외가 발생한다")
    void validate_invalidOrderTable() {
        // given
        em.flush();
        em.clear();

        final long invalidOrderTableId = -999L;
        final Order invalidOrder = new Order(invalidOrderTableId, List.of(주문_항목));

        // when & then
        assertThatThrownBy(() -> orderValidator.validate(invalidOrder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("주어진 주문 테이블 아이디에 해당하는 주문 테이블의 상태가 비어있으면 예외가 발생한다")
    void validate_emptyTable() {
        // given
        final OrderTable 비어있는_테이블 = orderTableRepository.save(new OrderTable(3, true));

        em.flush();
        em.clear();

        final Order invalidOrder = new Order(비어있는_테이블.getId(), List.of(주문_항목));

        // when & then
        assertThatThrownBy(() -> orderValidator.validate(invalidOrder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블에는 주문을 생성할 수 없습니다.");
    }
}
