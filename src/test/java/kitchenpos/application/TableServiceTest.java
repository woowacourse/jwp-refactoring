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
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableEmptyUpdateRequest;
import kitchenpos.dto.OrderTableNumberOfGuestsUpdateRequest;
import kitchenpos.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @DisplayName("테이블을 등록할 수 있다.")
    @Test
    void create() {
        OrderTableResponse response = tableService.create(new OrderTableCreateRequest(4, false));

        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getNumberOfGuests()).isEqualTo(4),
                () -> assertThat(response.isEmpty()).isFalse()
        );
    }

    @DisplayName("테이블들을 조회할 수 있다.")
    @Test
    void list() {
        orderTableDao.save(new OrderTable(0, true));
        orderTableDao.save(new OrderTable(0, true));

        List<OrderTableResponse> orderTables = tableService.list();

        assertThat(orderTables).hasSize(2);
    }

    @DisplayName("테이블을 빈 상태로 수정할 수 있다.")
    @Test
    void changeEmpty() {
        OrderTable orderTable = orderTableDao.save(new OrderTable(4, false));

        OrderTableResponse response = tableService.changeEmpty(orderTable.getId(),
                new OrderTableEmptyUpdateRequest(true));

        assertThat(response.isEmpty()).isTrue();
    }

    @DisplayName("테이블을 빈 상태로 수정 시 테이블이 존재하지 않으면 예외가 발생한다.")
    @Test
    void changeEmptyWithNotExistOrderTable() {
        assertThatThrownBy(() -> tableService.changeEmpty(9999L, new OrderTableEmptyUpdateRequest(true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블을 빈 상태로 수정 시 테이블이 단체인 경우 예외가 발생한다.")
    @Test
    void changeEmptyWithOrderTableGroup() {
        OrderTable firstOrderTable = orderTableDao.save(new OrderTable(4, false));
        OrderTable secondOrderTable = orderTableDao.save(new OrderTable(4, false));
        List<OrderTable> orderTables = createOrderTable(firstOrderTable, secondOrderTable);
        TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), orderTables));
        firstOrderTable.setTableGroupId(tableGroup.getId());
        firstOrderTable.setEmpty(false);
        secondOrderTable.setTableGroupId(tableGroup.getId());
        secondOrderTable.setEmpty(false);
        orderTableDao.save(firstOrderTable);
        orderTableDao.save(secondOrderTable);

        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTableEmptyUpdateRequest(true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블을 빈 상태로 수정 시 테이블에 준비중이거나 식사중인 주문이 존재하면 예외가 발생한다.")
    @Test
    void changeEmptyWithCookingOrMealStatus() {
        OrderTable orderTable = orderTableDao.save(new OrderTable(4, false));
        Product product = productDao.save(new Product("치킨", BigDecimal.valueOf(10000)));
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("1번 메뉴 그룹"));
        Menu menu = menuDao.save(new Menu("1번 메뉴", BigDecimal.valueOf(10000), menuGroup.getId(),
                createMenuProducts(product.getId())));
        orderDao.save(new Order(orderTable.getId(), "COOKING", LocalDateTime.now(), createOrderLineItem(menu.getId())));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), new OrderTableEmptyUpdateRequest(true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = orderTableDao.save(new OrderTable(4, false));

        OrderTableResponse response = tableService.changeNumberOfGuests(orderTable.getId(),
                new OrderTableNumberOfGuestsUpdateRequest(3));

        assertThat(response.getNumberOfGuests()).isEqualTo(3);
    }

    @DisplayName("테이블의 손님 수 변경 시 손님 수가 0보다 작으면 예외가 발생한다.")
    @Test
    void changeNumberWithInvalidNumberOfGuests() {
        OrderTable orderTable = orderTableDao.save(new OrderTable(4, false));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(),
                new OrderTableNumberOfGuestsUpdateRequest(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수 변경 시 테이블이 존재하지 않으면 예외가 발생한다.")
    @Test
    void changeNumberWithNotExistOrderTable() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(9999L, new OrderTableNumberOfGuestsUpdateRequest(4)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수 변경 시 테이블이 비어있으면 예외가 발생한다.")
    @Test
    void changeNumberWithEmptyOrderTable() {
        OrderTable orderTable = orderTableDao.save(new OrderTable(0, true));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(),
                new OrderTableNumberOfGuestsUpdateRequest(4)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private List<OrderTable> createOrderTable(OrderTable... orderTables) {
        return new ArrayList<>(Arrays.asList(orderTables));
    }

    private List<MenuProduct> createMenuProducts(Long... productIds) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (Long productId : productIds) {
            menuProducts.add(new MenuProduct(productId, 1L, BigDecimal.valueOf(10000)));
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
