package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private TableGroupService tableGroupService;

    @DisplayName("테이블 그룹을 생성할 수 있다.")
    @Test
    void create() {
        OrderTable firstOrderTable = orderTableDao.save(new OrderTable(0, true));
        OrderTable secondOrderTable = orderTableDao.save(new OrderTable(0, true));
        List<OrderTable> orderTables = createOrderTable(firstOrderTable, secondOrderTable);

        TableGroup tableGroup = tableGroupService.create(new TableGroup(orderTables));

        assertThat(tableGroup).isNotNull();
    }

    @DisplayName("테이블 그룹에 등록된 테이블이 없으면 예외가 발생한다.")
    @Test
    void createWithInvalidOrderTable() {
        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(new ArrayList<>())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹에 등록된 테이블이 2개 미만이면 예외가 발생한다.")
    @Test
    void createWithLessThanTwoOrderTable() {
        OrderTable firstOrderTable = orderTableDao.save(new OrderTable(0, true));
        List<OrderTable> orderTables = createOrderTable(firstOrderTable);

        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(orderTables)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 테이블이 있는 경우 예외가 발생한다.")
    @Test
    void createWithNotExistOrderTable() {
        OrderTable firstOrderTable = orderTableDao.save(new OrderTable(0, true));
        List<OrderTable> orderTables = createOrderTable(firstOrderTable, new OrderTable());

        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(orderTables)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블이 비어있지 않으면 예외가 발생한다.")
    @Test
    void createWithNotEmptyOrderTable() {
        OrderTable firstOrderTable = orderTableDao.save(new OrderTable(0, false));
        OrderTable secondOrderTable = orderTableDao.save(new OrderTable(0, false));
        List<OrderTable> orderTables = createOrderTable(firstOrderTable, secondOrderTable);

        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(orderTables)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 테이블 그룹이 존재하면 예외가 발생한다.")
    @Test
    void createWithOrderTableExistingTableGroup() {
        OrderTable firstOrderTable = orderTableDao.save(new OrderTable(0, true));
        OrderTable secondOrderTable = orderTableDao.save(new OrderTable(0, true));
        List<OrderTable> orderTables = createOrderTable(firstOrderTable, secondOrderTable);
        tableGroupService.create(new TableGroup(orderTables));

        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(orderTables)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 해제할 수 있다.")
    @Test
    void ungroup() {
        OrderTable firstOrderTable = orderTableDao.save(new OrderTable(0, true));
        OrderTable secondOrderTable = orderTableDao.save(new OrderTable(0, true));
        List<OrderTable> orderTables = createOrderTable(firstOrderTable, secondOrderTable);
        TableGroup tableGroup = tableGroupService.create(new TableGroup(orderTables));

        tableGroupService.ungroup(tableGroup.getId());
        OrderTable foundFirstOrderTable = orderTableDao.findById(firstOrderTable.getId()).get();
        OrderTable foundSecondOrderTable = orderTableDao.findById(secondOrderTable.getId()).get();

        assertAll(
                () -> assertThat(foundFirstOrderTable.getTableGroupId()).isNull(),
                () -> assertThat(foundSecondOrderTable.getTableGroupId()).isNull()
        );
    }

    @DisplayName("준비중이거나 식사중인 테이블이 있으면 예외가 발생한다.")
    @ValueSource(strings = {"COOKING", "MEAL"})
    @ParameterizedTest
    void ungroupWithCookingOrMeal(String orderStatus) {
        OrderTable firstOrderTable = orderTableDao.save(new OrderTable(0, true));
        OrderTable secondOrderTable = orderTableDao.save(new OrderTable(0, true));
        Product product = productDao.save(new Product("치킨", BigDecimal.valueOf(10000)));
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("1번 메뉴 그룹"));
        Menu menu = menuDao.save(
                new Menu("1번 메뉴", BigDecimal.valueOf(10000), menuGroup.getId(), createMenuProducts(product.getId())));
        orderDao.save(
                new Order(firstOrderTable.getId(), orderStatus, LocalDateTime.now(), createOrderLineItem(menu.getId())));
        List<OrderTable> orderTables = createOrderTable(firstOrderTable, secondOrderTable);
        TableGroup tableGroup = tableGroupService.create(new TableGroup(orderTables));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private List<OrderTable> createOrderTable(OrderTable... orderTables) {
        return new ArrayList<>(Arrays.asList(orderTables));
    }

    private List<MenuProduct> createMenuProducts(Long... productIds) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (Long productId : productIds) {
            menuProducts.add(new MenuProduct(productId, 1L));
        }
        return menuProducts;
    }

    private List<OrderLineItem> createOrderLineItem(Long... menuIds) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (Long menuId : menuIds) {
            orderLineItems.add(new OrderLineItem(menuId, 10));
        }
        return orderLineItems;
    }
}
