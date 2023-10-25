package kitchenpos.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.exception.InvalidOrderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderValidatorTest {
    @Autowired
    private OrderValidator orderValidator;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    private Menu menu;
    private MenuGroup menuGroup;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        menuGroup = menuGroupRepository
                .save(new MenuGroup("메뉴그룹"));
        final Product product = productRepository.save(new Product("치킨", BigDecimal.valueOf(10000)));
        menu = menuRepository.save(new Menu("치킨 세트 메뉴", new BigDecimal(20000), menuGroup.getId(),
                List.of(new MenuProduct(null, product.getId(), 1))));
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now(), null));
        orderTable = orderTableRepository.save(new OrderTable(tableGroup.getId(), 6, false));
    }

    @Test
    void 주문_항목의_메뉴가_중복이면_예외가_발생한다() {
        // given
        final OrderLineItem orderLineItem1 = new OrderLineItem(null, menu.getId(), 1);
        final OrderLineItem orderLineItem2 = new OrderLineItem(null, menu.getId(), 2);
        final Order order = new Order(1L, List.of(orderLineItem1, orderLineItem2));

        // when & then
        assertThatThrownBy(() -> orderValidator.validate(order))
                .isInstanceOf(InvalidOrderException.class);
    }

    @Test
    void 주문_테이블이_존재하지_않으면_예외가_발생한다() {
        // given
        final long nonExistTableId = 99L;
        final OrderLineItem orderLineItem = new OrderLineItem(null, menu.getId(), 1);
        final Order order = new Order(nonExistTableId, List.of(orderLineItem));

        // when & then
        assertThatThrownBy(() -> orderValidator.validate(order))
                .isInstanceOf(InvalidOrderException.class);
    }

    @Test
    void 주문_테이블이_비어있으면_예외가_발생한다() {
        // given
        final OrderLineItem orderLineItem = new OrderLineItem(null, menu.getId(), 1);
        orderTable.changeEmpty(true);
        orderTableRepository.save(orderTable);
        final Order order = new Order(orderTable.getId(), List.of(orderLineItem));

        // when & then
        assertThatThrownBy(() -> orderValidator.validate(order))
                .isInstanceOf(InvalidOrderException.class);
    }
}
