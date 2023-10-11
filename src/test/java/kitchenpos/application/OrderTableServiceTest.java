package kitchenpos.application;

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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class OrderTableServiceTest {

    @Autowired
    private OrderTableService orderTableService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MenuService menuService;

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private TableGroupDao tableGroupDao;
    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private ProductDao productDao;

    @Test
    @DisplayName("주문 테이블을 생성할 수 있다")
    void create() {
        //given
        final OrderTable request = new OrderTable();
        request.setNumberOfGuests(5);

        //when
        final OrderTable orderTable = orderTableService.create(request);

        //then
        assertSoftly(softAssertions -> {
            assertThat(orderTable.getId()).isNotNull();
            assertThat(orderTable.getTableGroupId()).isNull();
            assertThat(orderTable.getNumberOfGuests()).isEqualTo(5);
        });
    }

    @Test
    @DisplayName("주문 테이블 전체 조회를 할 수 있다")
    void list() {
        assertDoesNotThrow(() -> orderTableService.list());
    }

    @Nested
    @DisplayName("주문 테이블의 빈 테이블 여부 변경 테스트")
    class ChangeEmptyTest {

        @Test
        @DisplayName("주문 테이블의 빈 테이블 여부를 변경할 수 있다")
        void changeEmpty() {
            //given
            final OrderTable orderTable = saveOrderTable();

            final OrderTable request = new OrderTable();
            request.setEmpty(true);

            //when
            final OrderTable changedOrderTable = orderTableService.changeEmpty(orderTable.getId(), request);

            //then
            assertThat(changedOrderTable.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("주문 테이블의 빈 테이블 여부를 변경할 때 주문 테이블이 존재하지 않으면 예외가 발생한다")
        void changeEmpty_fail() {
            //given
            final OrderTable request = new OrderTable();
            request.setEmpty(true);

            //when, then
            assertThatThrownBy(() -> orderTableService.changeEmpty(0L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문 테이블의 빈 테이블 여부를 변경할 때 단체 지정이 존재하면 예외가 발생한다")
        void changeEmpty_fail2() {
            //given
            final TableGroup tableGroup = saveTableGroup();
            final OrderTable orderTable = saveOrderTableWithTableGroup(tableGroup);

            final OrderTable request = new OrderTable();
            request.setEmpty(true);

            //when, then
            assertThatThrownBy(() -> orderTableService.changeEmpty(orderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "MEAL"})
        @DisplayName("주문 테이블의 빈 테이블 여부를 변경할 때 주문 상태가 COOKING 또는 MEAL이면 예외가 발생한다")
        void changeEmpty_fail3(final String status) {
            //given
            final OrderTable orderTable = saveOrderTable();

            final OrderTable request = new OrderTable();
            request.setEmpty(true);

            final Order order = saveOrder(orderTable);
            final Order requestToChangeStatus = new Order();
            requestToChangeStatus.setOrderStatus(status);
            orderService.changeOrderStatus(order.getId(), requestToChangeStatus);

            //when, then
            assertThatThrownBy(() -> orderTableService.changeEmpty(orderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("주문 테이블의 방문한 손님 수 변경 테스트")
    class ChangeNumberOfGuestsTest {

        @Test
        @DisplayName("주문 테이블의 방문한 손님 수를 변경할 수 있다")
        void changeNumberOfGuests() {
            //given
            final OrderTable orderTable = saveOrderTable();

            final OrderTable request = new OrderTable();
            request.setNumberOfGuests(10);

            //when
            final OrderTable changedOrderTable = orderTableService.changeNumberOfGuests(orderTable.getId(), request);

            //then
            assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(10);
        }

        @Test
        @DisplayName("주문 테이블의 방문한 손님 수를 음수로 변경하면 예외가 발생한다")
        void changeNumberOfGuests_fail() {
            //given
            final OrderTable orderTable = saveOrderTable();

            final OrderTable request = new OrderTable();
            request.setNumberOfGuests(-1);

            //when, then
            assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(orderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문 테이블의 방문한 손님 수를 변경할 때 주문 테이블이 존재하지 않으면 예외가 발생한다")
        void changeNumberOfGuests_fail2() {
            //given
            final OrderTable request = new OrderTable();
            request.setNumberOfGuests(10);

            //when, then
            assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(0L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문 테이블의 방문한 손님 수를 변경할 때 주문 테이블이 비어 있으면 예외가 발생한다")
        void changeNumberOfGuests_fail3() {
            //given
            final OrderTable orderTable = saveOrderTableWithEmpty();

            final OrderTable request = new OrderTable();
            request.setNumberOfGuests(10);

            //when, then
            assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(orderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    private OrderTable saveOrderTable() {
        final OrderTable request = new OrderTable();
        request.setNumberOfGuests(5);

        return orderTableDao.save(request);
    }

    private OrderTable saveOrderTableWithTableGroup(final TableGroup tableGroup) {
        final OrderTable request = new OrderTable();
        request.setNumberOfGuests(5);
        request.setTableGroupId(tableGroup.getId());

        return orderTableDao.save(request);
    }

    private OrderTable saveOrderTableWithEmpty() {
        final OrderTable request = new OrderTable();
        request.setEmpty(true);

        return orderTableDao.save(request);
    }

    private TableGroup saveTableGroup() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());

        return tableGroupDao.save(tableGroup);
    }

    private Order saveOrder(final OrderTable orderTable) {
        final Menu menu = saveMenu();
        final OrderLineItem orderLineItem = createOrderLineItem(menu, 2);

        final Order request = new Order();
        request.setOrderTableId(orderTable.getId());
        request.setOrderLineItems(List.of(orderLineItem));

        return orderService.create(request);
    }

    private OrderLineItem createOrderLineItem(final Menu menu, final int quantity) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(quantity);

        return orderLineItem;
    }

    private OrderTable saveOrderTable(final boolean empty) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);

        return orderTableDao.save(orderTable);
    }

    private Menu saveMenu() {
        final Product product = saveProduct("연어", 4000);
        final MenuProduct menuProduct = createMenuProduct(4, product);
        final MenuGroup menuGroup = saveMenuGroup("일식");

        final Menu request = new Menu();
        request.setMenuGroupId(menuGroup.getId());
        request.setPrice(BigDecimal.valueOf(16000));
        request.setName("떡볶이 세트");
        request.setMenuProducts(singletonList(menuProduct));

        return menuService.create(request);
    }

    private Product saveProduct(final String name, final int price) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));

        return productDao.save(product);
    }

    private MenuProduct createMenuProduct(final int quantity, final Product product) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }

    private MenuGroup saveMenuGroup(final String name) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);

        return menuGroupDao.save(menuGroup);
    }
}
