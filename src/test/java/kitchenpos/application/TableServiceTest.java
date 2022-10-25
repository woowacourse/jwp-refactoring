package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@ApplicationTest
class TableServiceTest {

    private final OrderTableDao orderTableDao;
    private final ProductDao productDao;
    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final TableService tableService;
    private final OrderService orderService;

    TableServiceTest(OrderTableDao orderTableDao, ProductDao productDao, MenuDao menuDao,
                     MenuGroupDao menuGroupDao, TableService tableService,
                     OrderService orderService) {
        this.orderTableDao = orderTableDao;
        this.productDao = productDao;
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.tableService = tableService;
        this.orderService = orderService;
    }

    @Test
    void 테이블을_생성한다() {
        OrderTable orderTable = new OrderTable(null, 0, true);

        OrderTable actual = tableService.create(orderTable);
        assertThat(actual).isExactlyInstanceOf(Long.class);
    }

    @Test
    void 테이블을_모두_조회한다() {
        orderTableDao.save(new OrderTable());

        List<OrderTable> orderTables = tableService.list();

        assertThat(orderTables).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    void 테이블을_비운다() {
        OrderTable savedTable = orderTableDao.save(new OrderTable(null, 0, false));

        OrderTable result = tableService.changeEmpty(savedTable.getId(), new OrderTable(null, 0, true));

        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void 테이블을_비울때_테이블이_존재하지_않을_경우_예외를_발생시킨다() {
        assertThatThrownBy(() -> tableService.changeEmpty(-1L, null))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블을_비울때_조리중인_주문이_있는_경우_예외를_발생시킨다() {
        OrderTable savedTable = orderTableDao.save(new OrderTable(null, 0, false));
        Order order = new Order(savedTable.getId(), List.of(new OrderLineItem(1L, 메뉴를_저장한다().getId(), 1)));
        orderService.create(order);

        assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), new OrderTable(1L, 0, true)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블을_비울때_식사중인_주문이_있는_경우_예외를_발생시킨다() {
        OrderTable savedTable = orderTableDao.save(new OrderTable(null, 0, false));
        Order order = new Order(savedTable.getId(), List.of(new OrderLineItem(1L, 메뉴를_저장한다().getId(), 1)));
        Order savedOrder = orderService.create(order);
        savedOrder.setOrderStatus("MEAL");
        orderService.changeOrderStatus(savedOrder.getId(), savedOrder);

        assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), new OrderTable(1L, 0, true)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블의_손님수를_변경한다() {
        OrderTable savedTable = orderTableDao.save(new OrderTable(null, 0, false));

        savedTable.setNumberOfGuests(1);
        OrderTable result = tableService.changeNumberOfGuests(savedTable.getId(), savedTable);

        assertThat(result.getNumberOfGuests()).isEqualTo(1);
    }

    @Test
    void 테이블의_손님수를_변경할때_수정할_손님수가_0보다_작을때_예외를_발생시킨다() {
        OrderTable savedTable = orderTableDao.save(new OrderTable(null, 0, false));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(
                savedTable.getId(), new OrderTable(savedTable.getId(), -1, false)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블의_손님수를_변경할때_테이블이_존재하지_않는_경우_예외를_발생시킨다() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(-1L, new OrderTable(null, 1, false)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블의_손님수를_변경할때_이미_비어있는_경우_예외를_발생시킨다() {
        OrderTable savedTable = orderTableDao.save(new OrderTable(null, 0, true));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), new OrderTable(null, 1, true)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    Menu 메뉴를_저장한다() {
        Product jwt_후라이드 = productDao.save(new Product("JWT 후라이드", new BigDecimal(100_000)));
        Product jwt_양념 = productDao.save(new Product("JWT 양념", new BigDecimal(100_000)));
        MenuProduct 후라이드 = new MenuProduct(1L, jwt_후라이드.getId(), 1);
        MenuProduct 양념 = new MenuProduct(2L, jwt_양념.getId(), 1);
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("추천메뉴"));
        Menu menu = new Menu("반반치킨", new BigDecimal(200_000), menuGroup.getId(), List.of(후라이드, 양념));

        return menuDao.save(menu);
    }
}
