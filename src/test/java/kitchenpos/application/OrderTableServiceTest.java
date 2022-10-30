package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.OrderTableGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableGroup;
import kitchenpos.domain.Product;
import kitchenpos.fakedao.MenuFakeDao;
import kitchenpos.fakedao.MenuGroupFakeDao;
import kitchenpos.fakedao.MenuProductFakeDao;
import kitchenpos.fakedao.OrderFakeDao;
import kitchenpos.fakedao.OrderTableFakeDao;
import kitchenpos.fakedao.OrderTableGroupFakeDao;
import kitchenpos.fakedao.ProductFakeDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class OrderTableServiceTest {

    private OrderTableGroupDao orderTableGroupDao = new OrderTableGroupFakeDao();
    private OrderDao orderDao = new OrderFakeDao();
    private OrderTableDao orderTableDao = new OrderTableFakeDao();
    private MenuGroupDao menuGroupDao = new MenuGroupFakeDao();
    private MenuDao menuDao = new MenuFakeDao();
    private ProductDao productDao = new ProductFakeDao();
    private MenuProductDao menuProductDao = new MenuProductFakeDao();

    private OrderTableService orderTableService = new OrderTableService(orderDao, orderTableDao, orderTableGroupDao);

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void create() {
        // when
        OrderTable actual = orderTableService.create(3, false);

        // then
        assertThat(actual).isNotNull();
    }

    @DisplayName("모든 주문 테이블을 조회한다.")
    @Test
    void list() {
        // given
        orderTableDao.save(new OrderTable(null, 2, false));
        orderTableDao.save(new OrderTable(null, 2, false));

        // when
        List<OrderTable> actual = orderTableService.list();

        // then
        assertThat(actual).hasSize(2);
    }

    @DisplayName("주문 테이블을 비울 때")
    @Nested
    class ChangeEmpty {

        @DisplayName("성공")
        @Test
        void success() {
            // given
            OrderTable orderTable1 = orderTableDao.save(new OrderTable(1L, 2, false));
            OrderTable orderTable2 = orderTableDao.save(new OrderTable(1L, 3, false));
            orderTableGroupDao.save(new OrderTableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2)));

            MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("메뉴 그룹"));
            Product product = productDao.save(new Product("상품", BigDecimal.valueOf(1000L)));
            MenuProduct menuProduct = menuProductDao.save(
                    new MenuProduct(product.getId(), 2, product.getPrice()));
            Menu menu = menuDao.save(new Menu("메뉴", BigDecimal.valueOf(1000), menuGroup.getId(),
                    List.of(menuProduct)));
            orderDao.save(new Order(orderTable1.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now(),
                    List.of(new OrderLineItem(menu.getId(), 2))));
            // when
            OrderTable actual = orderTableService.changeEmpty(orderTable1.getId());

            // then
            assertThat(actual.isEmpty()).isTrue();
        }

        @DisplayName("주문 테이블을 찾지 못하면 예외를 발생시킨다.")
        @Test
        void notFoundOrderTable_exception() {
            // then
            assertThatThrownBy(() -> orderTableService.changeEmpty(0L))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("단체 그룹이 지정되어 있으면 예외를 발생시킨다.")
        @Test
        void existTableGroup_exception() {
            // given
            OrderTable orderTable = orderTableDao.save(new OrderTable(1L, 2, false));

            // when
            assertThatThrownBy(() -> orderTableService.changeEmpty(orderTable.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블의 주문 상태가 조리중이거나 조리가 됐다면 예외를 발생시킨다.")
        @Test
        void existsOrderStatusIsCookingOrMeal_exception() {
            // given
            OrderTable orderTable1 = orderTableDao.save(new OrderTable(1L, 2, false));
            OrderTable orderTable2 = orderTableDao.save(new OrderTable(1L, 3, false));
            orderTableGroupDao.save(new OrderTableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2)));

            MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("메뉴 그룹"));
            Product product = productDao.save(new Product("상품", BigDecimal.valueOf(1000L)));
            MenuProduct menuProduct = menuProductDao.save(
                    new MenuProduct(product.getId(), 2, product.getPrice()));
            Menu menu = menuDao.save(new Menu("메뉴", BigDecimal.valueOf(1000), menuGroup.getId(),
                    List.of(menuProduct)));
            orderDao.save(new Order(orderTable1.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(),
                    List.of(new OrderLineItem(menu.getId(), 2))));

            // then
            assertThatThrownBy(() -> orderTableService.changeEmpty(orderTable1.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("주문 테이블의 손님수를 수정할 때")
    @Nested
    class ChangeNumberOfGuests {

        @DisplayName("성공")
        @Test
        void success() {
            // given
            OrderTable orderTable = orderTableDao.save(new OrderTable(null, 2, false));

            // when
            OrderTable actual = orderTableService.changeNumberOfGuests(orderTable.getId(), 3);

            // then
            assertThat(actual.getNumberOfGuests()).isEqualTo(3);
        }

        @DisplayName("손님의 수가 0보다 작으면 예외를 발생시킨다.")
        @Test
        void numberOfGuestsLessThanZero_exception() {
            // given
            OrderTable orderTable = orderTableDao.save(new OrderTable(null, 2, false));

            // then
            assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(orderTable.getId(), -1))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블을 조회하지 못하면 예외를 발생시킨다.")
        @Test
        void notFoundOrderTable_exception() {
            // given
            new OrderTable(null, 3, false);

            // then
            assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(0L, 2))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("테이블이 empty 상태면 예외를 발생시킨다.")
        @Test
        void emptyIsTrue_exception() {
            // given
            OrderTable entity = new OrderTable(null, 2, false);
            entity.changeEmpty();
            OrderTable orderTable = orderTableDao.save(entity);

            // then
            assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(orderTable.getId(), 3))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
