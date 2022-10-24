package kitchenpos.application;

import static kitchenpos.application.fixture.MenuGroupFixture.치킨;
import static kitchenpos.application.fixture.MenuGroupFixture.피자;
import static kitchenpos.application.fixture.MenuProductFixture.메뉴_상품_생성;
import static kitchenpos.application.fixture.ProductFixture.양념_치킨;
import static kitchenpos.application.fixture.ProductFixture.포테이토_피자;
import static kitchenpos.application.fixture.ProductFixture.후라이드_치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.fixture.MenuFixture;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("/truncate.sql")
@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

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

    @Nested
    @DisplayName("ungroup은")
    class ungroup {

        @DisplayName("단체 주문에 대해 포함된 주문 중 완료가 아닌 주문이 존재하면 예외를 발생한다.")
        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        void ungroupNotCompletedOrder(OrderStatus orderStatus) {
            // given
            OrderTable orderTable1 = 주문_테이블_생성();
            TableGroup tableGroup = 단체_지정_생성(orderTable1);
            TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
            orderTable1.setTableGroupId(savedTableGroup.getId());
            OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);

            Order order1 = 주문_생성(savedOrderTable1);
            order1.setOrderStatus(orderStatus.name());
            order1.setOrderedTime(LocalDateTime.now());
            Order savedOrder = orderDao.save(order1);

            // when & then
            assertThatThrownBy(
                    () -> tableGroupService.ungroup(savedTableGroup.getId())
            ).isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("완료되지 않은 주문이 존재합니다.");
        }

        @DisplayName("정상적으로 그룹을 해제한다.")
        @Test
        void upgroup() {
            // given
            OrderTable orderTable1 = 주문_테이블_생성();
            TableGroup tableGroup = 단체_지정_생성(orderTable1);
            TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
            orderTable1.setTableGroupId(savedTableGroup.getId());
            OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);

            Order order1 = 주문_생성(savedOrderTable1);
            order1.setOrderStatus(OrderStatus.COMPLETION.name());
            order1.setOrderedTime(LocalDateTime.now());
            Order savedOrder = orderDao.save(order1);

            // when
            tableGroupService.ungroup(savedTableGroup.getId());

            //then
            assertThat(orderTableDao.findAllByTableGroupId(savedTableGroup.getId())).isEmpty();
        }
    }

    @Nested
    @DisplayName("create 는")
    class create {

        @DisplayName("orderTable의 크기가 2보다 작은경우 예외를 발생한다.")
        @Test
        void orderTableSizeSmallerThan2() {
            // given
            OrderTable orderTable1 = orderTableDao.save(빈_주문_테이블_생성());
            TableGroup tableGroup = 단체_지정_생성(orderTable1);

            // when & then
            assertThatThrownBy(
                    () -> tableGroupService.create(tableGroup)
            ).isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("주문 테이블의 수는 2 이상이어야합니다.");
        }

        @DisplayName("저장된 order table과 TableGroup 내의 order table이 다르면 예외를 발생한다.")
        @Test
        void orderTableDifferentInTableGroup() {
            // given
            OrderTable savedOrderTable = orderTableDao.save(빈_주문_테이블_생성());
            OrderTable notSavedOrderTable = 빈_주문_테이블_생성();
            TableGroup tableGroup = 단체_지정_생성(savedOrderTable, notSavedOrderTable);

            // when & then
            assertThatThrownBy(
                    () -> tableGroupService.create(tableGroup)
            ).isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("저장된 table과 다릅니다.");
        }

        @DisplayName("TableGroup 내의 order table 중 empty가 아닌 경우가 존재하면 예외가 발생한다.")
        @Test
        void emptyOrderTable() {
            // given
            OrderTable notEmptyOrderTable = orderTableDao.save(주문_테이블_생성());
            OrderTable emptyOrderTable = orderTableDao.save(빈_주문_테이블_생성());
            TableGroup tableGroup = 단체_지정_생성(notEmptyOrderTable, emptyOrderTable);

            // when & then
            assertThatThrownBy(
                    () -> tableGroupService.create(tableGroup)
            ).isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("저장할 table은 empty 샹태가 아니거나 다른 table group에 포함되서는 안됩니다.");
        }

        @DisplayName("TableGroup 내의 order table 중 다른 table group의 id가 존재하면 예외가 발생한다.")
        @Test
        void otherGroupOrderTable() {
            // given
            OrderTable orderTable = 빈_주문_테이블_생성();
            TableGroup otherTableGroup = tableGroupDao.save(단체_지정_생성(orderTable));
            orderTable.setTableGroupId(otherTableGroup.getId());
            OrderTable otherGroupOrderTable = orderTableDao.save(orderTable);
            OrderTable thisGroupOrderTable = orderTableDao.save(빈_주문_테이블_생성());
            TableGroup tableGroup = 단체_지정_생성(otherGroupOrderTable, thisGroupOrderTable);

            // when & then
            assertThatThrownBy(
                    () -> tableGroupService.create(tableGroup)
            ).isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("저장할 table은 empty 샹태가 아니거나 다른 table group에 포함되서는 안됩니다.");
        }

        @DisplayName("정상 케이스")
        @Test
        void group() {
            // given
            OrderTable orderTable1 = orderTableDao.save(빈_주문_테이블_생성());
            OrderTable orderTable2 = orderTableDao.save(빈_주문_테이블_생성());
            TableGroup tableGroup = 단체_지정_생성(orderTable1, orderTable2);

            // when
            TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            //then
            assertAll(
                    () -> assertThat(tableGroupDao.findById(savedTableGroup.getId())).isPresent(),
                    () -> assertThat(orderTableDao.findAllByTableGroupId(savedTableGroup.getId())).hasSize(2)
            );

        }
    }

    private OrderTable 빈_주문_테이블_생성() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(0);

        return orderTable;
    }

    private TableGroup 단체_지정_생성(final OrderTable... orderTables) {
        List<OrderTable> orderTableList = Arrays.asList(orderTables);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(orderTableList);

        return tableGroup;
    }

    private OrderTable 주문_테이블_생성() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(2);

        return orderTable;
    }

    private Order 주문_생성(final OrderTable orderTable) {
        Order order = new Order();
        order.setOrderTableId(orderTable.getId());

        return order;
    }
}
