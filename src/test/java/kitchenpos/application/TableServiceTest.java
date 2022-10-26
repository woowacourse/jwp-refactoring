package kitchenpos.application;

import static kitchenpos.application.fixture.MenuGroupFixture.치킨;
import static kitchenpos.application.fixture.MenuGroupFixture.피자;
import static kitchenpos.application.fixture.ProductFixture.양념_치킨;
import static kitchenpos.application.fixture.ProductFixture.포테이토_피자;
import static kitchenpos.application.fixture.ProductFixture.후라이드_치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.fixture.MenuFixture;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class TableServiceTest extends ServiceTestBase {

    @Autowired
    private TableService tableService;

    private Menu friedChicken;
    private Menu seasonedChicken;
    private Menu potatoPizza;

    @DisplayName("메뉴 및 메뉴 그룹 생성")
    @BeforeEach
    void setUp() {
        Product productChicken1 = productDao.save(후라이드_치킨());
        Product productChicken2 = productDao.save(양념_치킨());
        MenuGroup chickenMenuGroup = menuGroupDao.save(치킨());

        Menu menuChicken1 = MenuFixture.후라이드_치킨(chickenMenuGroup);
        Menu menuChicken2 = MenuFixture.양념_치킨(chickenMenuGroup);

        MenuProduct menuProductChicken1 = 메뉴_상품_생성(menuChicken1, productChicken1, 1);
        MenuProduct menuProductChicken2 = 메뉴_상품_생성(menuChicken2, productChicken2, 1);

        menuChicken1.setMenuProducts(Collections.singletonList(menuProductChicken1));
        menuChicken2.setMenuProducts(Collections.singletonList(menuProductChicken2));

        friedChicken = menuDao.save(menuChicken1);
        seasonedChicken = menuDao.save(menuChicken2);

        Product productPizza = productDao.save(포테이토_피자());
        MenuGroup pizzaMenuGroup = menuGroupDao.save(피자());

        Menu menuPizza = MenuFixture.포테이토_피자(pizzaMenuGroup);
        MenuProduct menuProductPizza = 메뉴_상품_생성(menuPizza, productPizza, 1);

        menuPizza.setMenuProducts(Collections.singletonList(menuProductPizza));

        potatoPizza = menuDao.save(menuPizza);
    }

    @DisplayName("Table의 전체 목록을 조회한다.")
    @Test
    void findAll() {
        // given
        OrderTable orderTable1 = 빈_주문_테이블_생성();
        OrderTable orderTable2 = 빈_주문_테이블_생성();
        OrderTable orderTable3 = 빈_주문_테이블_생성();

        orderTableDao.save(orderTable1);
        orderTableDao.save(orderTable2);
        orderTableDao.save(orderTable3);

        // when
        List<OrderTable> orderTables = tableService.list();

        //then
        assertThat(orderTables).hasSize(3);
    }

    @DisplayName("Table을 등록한다.")
    @Test
    void create() {
        // given
        OrderTable orderTable1 = 빈_주문_테이블_생성();

        // when
        OrderTable savedTable = tableService.create(orderTable1);

        //then
        assertAll(
                () -> assertThat(savedTable.isEmpty()).isTrue(),
                () -> assertThat(savedTable.getNumberOfGuests()).isZero()
        );
    }

    @DisplayName("Empty값 업데이트 시 존재하지않는 order table이면 예외 발생")
    @Test
    void changeEmptyWithNotExistedTable() {
        // given
        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setEmpty(false);

        // when & then
        assertThatThrownBy(
                () -> tableService.changeEmpty(1L, newOrderTable)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 order table 입니다.");
    }

    @DisplayName("Empty값 업데이트 시 ordertable에 tableGroupID가 있으면 예외 발생")
    @Test
    void changeEmptyWithNotExistedTableGroup() {
        // given
        OrderTable orderTable1 = 빈_주문_테이블_생성();
        TableGroup tableGroup = tableGroupDao.save(단체_지정_생성(orderTable1));
        orderTable1.setTableGroupId(tableGroup.getId());
        OrderTable savedTable = orderTableDao.save(orderTable1);
        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setEmpty(false);

        // when & then
        assertThatThrownBy(
                () -> tableService.changeEmpty(savedTable.getId(), newOrderTable)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("TableGroupId가 있습니다.");
    }

    @DisplayName("Empty값 업데이트 시 ordertable의 order의 status가 completion이 아니면 예외 발생")
    @Test
    void changeEmptyWithNotCompletedOrder() {
        // given
        OrderTable orderTable1 = 빈_주문_테이블_생성();
        OrderTable savedTable = orderTableDao.save(orderTable1);

        Order order1 = 주문_생성_및_저장(savedTable);
        OrderLineItem orderLineItem1 = 주문_항목_생성(order1, friedChicken, 1);
        orderLineItemDao.save(orderLineItem1);

        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setEmpty(false);

        // when & then
        assertThatThrownBy(
                () -> tableService.changeEmpty(savedTable.getId(), newOrderTable)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문이 진행 중입니다.");
    }

    @DisplayName("Empty 값 정상 업데이트")
    @Test
    void updateEmpty() {
        // given
        OrderTable orderTable1 = 빈_주문_테이블_생성();
        OrderTable savedTable = orderTableDao.save(orderTable1);

        Order order1 = 주문_생성(savedTable);
        order1.setOrderedTime(LocalDateTime.now());
        order1.setOrderStatus(OrderStatus.COMPLETION.name());
        Order savedOrder = orderDao.save(order1);
        OrderLineItem orderLineItem1 = 주문_항목_생성(savedOrder, friedChicken, 1);
        orderLineItemDao.save(orderLineItem1);

        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setEmpty(false);

        // when
        OrderTable orderTable = tableService.changeEmpty(savedTable.getId(), newOrderTable);

        //then
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @DisplayName("받은 손님의 수가 0보다 작으면 예외를 발생한다.")
    @Test
    void changeMinusGuest() {
        // given
        OrderTable orderTable1 = 빈_주문_테이블_생성();
        OrderTable savedTable = orderTableDao.save(orderTable1);

        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setNumberOfGuests(-1);

        // when & then
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(savedTable.getId(), newOrderTable)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("손님의 수는 0 이상이어야합니다.");
    }

    @DisplayName("ordertable이 존재하지 않으면 예외를 발생한다.")
    @Test
    void changeNotExistedOrderTable() {
        // given
        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setNumberOfGuests(2);

        // when & then
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(0L, newOrderTable)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 order table 입니다.");
    }

    @DisplayName("빈 주문 테이블의 손님의 수를 update 할 시에 예외를 발생한다.")
    @Test
    void changeEmptyOrderTable() {
        // given
        OrderTable orderTable1 = 빈_주문_테이블_생성();
        OrderTable savedTable = orderTableDao.save(orderTable1);

        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setNumberOfGuests(2);

        // when & then
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(savedTable.getId(), newOrderTable)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("빈 주문 테이블의 손님의 수를 업데이트 할 수 없습니다.");
    }

    @DisplayName("손님의 수를 정상적으로 업데이트한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTable orderTable = orderTableDao.save(주문_테이블_생성());

        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setNumberOfGuests(4);

        // when
        OrderTable savedOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), newOrderTable);

        //then
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(4);
    }

    private Order 주문_생성_및_저장(final OrderTable orderTable) {
        Order order = 주문_생성(orderTable);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.COOKING.name());

        return orderDao.save(order);
    }
}
