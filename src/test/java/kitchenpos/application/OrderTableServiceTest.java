package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
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
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.support.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderTableServiceTest extends IntegrationTest {

    @Autowired
    private OrderTableGroupDao orderTableGroupDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private OrderTableService orderTableService;

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
        orderTableDao.save(new OrderTable(2, false));
        orderTableDao.save(new OrderTable(2, false));

        // when
        List<OrderTable> actual = orderTableService.list();

        // then
        assertThat(actual).hasSize(2);
    }

    @DisplayName("주문 테이블을 비울 때")
    @Nested
    class ChangeEmpty extends IntegrationTest {

        @DisplayName("성공")
        @Test
        void success() {
            // given
            OrderTableGroup orderTableGroup = orderTableGroupDao.save(new OrderTableGroup(LocalDateTime.now(), List.of(
                    new OrderTable(2, false),
                    new OrderTable(2, false)
            )));
            OrderTable orderTable = orderTableGroup.getOrderTables().get(0);

            MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("메뉴 그룹"));
            Product product = productDao.save(new Product("상품", 1000L));
            Menu menu = menuDao.save(new Menu("메뉴", new Price(1000L), menuGroup,
                    List.of(new MenuProduct(product, 2))));
            orderDao.save(new Order(orderTable, OrderStatus.COMPLETION.name(), LocalDateTime.now(),
                    List.of(new OrderLineItem(menu, 2))));
            // when
            System.out.println("===");
            OrderTable actual = orderTableService.changeEmpty(orderTable.getId());
            System.out.println("===");

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
            OrderTableGroup orderTableGroup = orderTableGroupDao.save(
                    new OrderTableGroup(LocalDateTime.now(), List.of(
                            new OrderTable(2, false),
                            new OrderTable(3, false)
                    ))
            );
            OrderTable orderTable = orderTableGroup.getOrderTables().get(0);

            // when
            assertThatThrownBy(() -> orderTableService.changeEmpty(orderTable.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블의 주문 상태가 조리중이거나 조리가 됐다면 예외를 발생시킨다.")
        @Test
        void existsOrderStatusIsCookingOrMeal_exception() {
            // given
            OrderTableGroup orderTableGroup = orderTableGroupDao.save(new OrderTableGroup(LocalDateTime.now(),
                    List.of(
                            new OrderTable(2, false),
                            new OrderTable(3, false)
                    ))
            );

            OrderTable orderTable = orderTableGroup.getOrderTables().get(0);

            MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("메뉴 그룹"));
            Product product = productDao.save(new Product("상품", 1000L));
            Menu menu = menuDao.save(new Menu("메뉴", new Price(1000L), menuGroup,
                    List.of(new MenuProduct(product, 2))));
            orderDao.save(new Order(orderTable, OrderStatus.COOKING.name(), LocalDateTime.now(),
                    List.of(new OrderLineItem(menu, 2))));

            // then
            assertThatThrownBy(() -> orderTableService.changeEmpty(orderTable.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("주문 테이블의 손님수를 수정할 때")
    @Nested
    class ChangeNumberOfGuests extends IntegrationTest {

        @DisplayName("성공")
        @Test
        void success() {
            // given
            OrderTable orderTable = orderTableDao.save(new OrderTable(2, false));

            // when
            OrderTable actual = orderTableService.changeNumberOfGuests(orderTable.getId(), 3);

            // then
            assertThat(actual.getNumberOfGuests()).isEqualTo(3);
        }

        @DisplayName("손님의 수가 0보다 작으면 예외를 발생시킨다.")
        @Test
        void numberOfGuestsLessThanZero_exception() {
            // given
            OrderTable orderTable = orderTableDao.save(new OrderTable(2, false));

            // then
            assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(orderTable.getId(), -1))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블을 조회하지 못하면 예외를 발생시킨다.")
        @Test
        void notFoundOrderTable_exception() {
            // given
            new OrderTable(3, false);

            // then
            assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(0L, 2))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("테이블이 empty 상태면 예외를 발생시킨다.")
        @Test
        void emptyIsTrue_exception() {
            // given
            OrderTable entity = new OrderTable(2, false);
            entity.changeEmpty();
            OrderTable orderTable = orderTableDao.save(entity);

            // then
            assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(orderTable.getId(), 3))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
