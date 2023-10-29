package kitchenpos.order.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.exception.InvalidOrderException;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PlaceOrderServiceTest {
    @Autowired
    private PlaceOrderService placeOrderService;

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
        orderTable = orderTableRepository.save(new OrderTable(null, 6, true));
        final OrderTable orderTable2 = orderTableRepository.save(new OrderTable(null, 6, true));
        tableGroupRepository.save(new TableGroup(LocalDateTime.now(), List.of(orderTable, orderTable2)));
    }

    @Test
    void 주문_항목의_메뉴가_중복이면_예외가_발생한다() {
        // given
        final OrderLineItem orderLineItem1 = new OrderLineItem(null, menu.getId(), 1);
        final OrderLineItem orderLineItem2 = new OrderLineItem(null, menu.getId(), 2);
        final Order order = new Order(1L, List.of(orderLineItem1, orderLineItem2));

        // when & then
        assertThatThrownBy(() -> placeOrderService.place(order))
                .isInstanceOf(InvalidOrderException.class);
    }

    @Test
    void 주문_테이블이_존재하지_않으면_예외가_발생한다() {
        // given
        final long nonExistTableId = 99L;
        final OrderLineItem orderLineItem = new OrderLineItem(null, menu.getId(), 1);
        final Order order = new Order(nonExistTableId, List.of(orderLineItem));

        // when & then
        assertThatThrownBy(() -> placeOrderService.place(order))
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
        assertThatThrownBy(() -> placeOrderService.place(order))
                .isInstanceOf(InvalidOrderException.class);
    }
}
