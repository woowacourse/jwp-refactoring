package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import kitchenpos.support.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderTableGroupServiceTest extends IntegrationTest {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderTableGroupDao orderTableGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private OrderTableGroupService orderTableGroupService;

    @DisplayName("단체 지정을 생성할 때")
    @Nested
    class Create extends IntegrationTest {

        @DisplayName("성공")
        @Test
        void success() {
            // given
            ArrayList<OrderTable> orderTables = new ArrayList<>();
            orderTables.add(orderTableDao.save(new OrderTable(null, 1, false)));
            orderTables.add(orderTableDao.save(new OrderTable(null, 2, false)));
            orderTables.add(orderTableDao.save(new OrderTable(null, 3, false)));

            // when
            OrderTableGroup actual = orderTableGroupService.create(orderTables);

            // then
            Optional<OrderTableGroup> tableGroup = orderTableGroupDao.findById(actual.getId());
            assertAll(
                    () -> assertThat(tableGroup).isPresent(),
                    () -> assertThat(tableGroup.get().getCreatedDate()).isNotNull(),
                    () -> assertThat(tableGroup.get().getOrderTables()).allMatch(it -> !it.isEmpty())
            );
        }

        @DisplayName("주문 테이블의 수가 2 미만이면 예외를 발생시킨다.")
        @Test
        void numberOfOrderTablesLessThanTwo_exception() {
            // given
            ArrayList<OrderTable> orderTables = new ArrayList<>();
            orderTables.add(orderTableDao.save(new OrderTable(null, 1, false)));

            // then
            assertThatThrownBy(() -> orderTableGroupService.create(orderTables))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("저장된 주문 테이블의 수와 맞지 않으면 예외를 발생시킨다.")
        @Test
        void notMatchNumberOfOrderTables_exception() {
            // given
            ArrayList<OrderTable> orderTables = new ArrayList<>();
            orderTables.add(orderTableDao.save(new OrderTable(null, 1, false)));
            orderTables.add(orderTableDao.save(new OrderTable(null, 2, false)));
            orderTables.add(new OrderTable(null, 3, false));

            // then
            assertThatThrownBy(() -> orderTableGroupService.create(orderTables))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("이미 단체 지정이 된 주문 테이블이 있으면 예외를 발생시킨다.")
        @Test
        void alreadyOrderTableHasTableGroup_exception() {
            // given
            ArrayList<OrderTable> orderTables = new ArrayList<>();
            orderTables.add(orderTableDao.save(new OrderTable(1L, 1, false)));
            orderTables.add(orderTableDao.save(new OrderTable(null, 2, false)));

            // then
            assertThatThrownBy(() -> orderTableGroupService.create(orderTables))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("단체 지정된 주문 테이블들을 분리할 때")
    @Nested
    class Ungroup extends IntegrationTest {

        @DisplayName("성공")
        @Test
        void success() {
            // given
            OrderTable orderTable1 = orderTableDao.save(new OrderTable(1L, 1, false));
            OrderTable orderTable2 = orderTableDao.save(new OrderTable(1L, 1, false));
            OrderTable orderTable3 = orderTableDao.save(new OrderTable(1L, 1, false));
            OrderTableGroup orderTableGroup = orderTableGroupDao.save(new OrderTableGroup(LocalDateTime.now(),
                    List.of(orderTable1, orderTable2, orderTable3)));

            // when
            orderTableGroupService.ungroup(orderTableGroup.getId());

            // then
            List<OrderTable> actual = orderTableDao.findAllByTableGroupId(orderTableGroup.getId());
            assertThat(actual).hasSize(0);
        }

        @DisplayName("주문 테이블들 중 하나라도 조리중이거나 조리가 됐다면 예외를 발생시킨다.")
        @Test
        void orderTablesStatusIsMealOrCooking_exception() {
            // given
            OrderTable orderTable1 = orderTableDao.save(new OrderTable(1L, 1, false));
            OrderTable orderTable2 = orderTableDao.save(new OrderTable(1L, 1, false));
            OrderTable orderTable3 = orderTableDao.save(new OrderTable(1L, 1, false));
            OrderTableGroup orderTableGroup = orderTableGroupDao.save(new OrderTableGroup(LocalDateTime.now(),
                    List.of(orderTable1, orderTable2, orderTable3)));

            MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("메뉴 그룹"));
            Product product = productDao.save(new Product("상품", 1000L));
            MenuProduct menuProduct = menuProductDao.save(
                    new MenuProduct(product.getId(), 2, product.getPrice()));
            Menu menu = menuDao.save(new Menu("메뉴", BigDecimal.valueOf(1000), menuGroup.getId(),
                    List.of(menuProduct)));
            orderDao.save(new Order(orderTable1.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now(),
                    List.of(new OrderLineItem(menu.getId(), 2))));
            orderDao.save(new Order(orderTable2.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(),
                    List.of(new OrderLineItem(menu.getId(), 2))));

            // then
            assertThatThrownBy(() -> orderTableGroupService.ungroup(orderTableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
