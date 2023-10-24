package kitchenpos.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
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
    private MenuDao menuDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    private Menu menu;
    private MenuGroup menuGroup;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        menuGroup = menuGroupDao.save(new MenuGroup("메뉴그룹"));
        final Product product = productDao.save(new Product("치킨", BigDecimal.valueOf(10000)));
        menu = menuDao.save(new Menu("치킨 세트 메뉴", new BigDecimal(20000), menuGroup.getId(),
                List.of(new MenuProduct(null, null, product.getId(), 1))));
        menuProductDao.save(new MenuProduct(null, menu.getId(), product.getId(), 1));
        final TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), null));
        orderTable = orderTableDao.save(new OrderTable(tableGroup.getId(), 6, false));
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
        orderTableDao.save(orderTable);
        final Order order = new Order(orderTable.getId(), List.of(orderLineItem));

        // when & then
        assertThatThrownBy(() -> orderValidator.validate(order))
                .isInstanceOf(InvalidOrderException.class);
    }
}
