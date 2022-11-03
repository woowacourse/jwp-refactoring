package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private OrderTableGroupService orderTableGroupService;

    @DisplayName("단체 지정을 생성할 때")
    @Nested
    class Create extends IntegrationTest {

        @DisplayName("성공")
        @Test
        void success() {
            // when
            OrderTableGroup actual = orderTableGroupService.create(List.of(
                    new OrderTable(1, false),
                    new OrderTable(1, false),
                    new OrderTable(1, false)
            ));

            // then
            Optional<OrderTableGroup> tableGroup = orderTableGroupDao.findById(actual.getId());
            List<OrderTable> orderTables = orderTableDao.findAllByOrderTableGroupId(
                    tableGroup.get().getId());
            assertAll(
                    () -> assertThat(tableGroup).isPresent(),
                    () -> assertThat(tableGroup.get().getCreatedDate()).isNotNull(),
                    () -> assertThat(orderTables).allMatch(it -> !it.isEmpty())
            );
        }

        @DisplayName("주문 테이블의 수가 2 미만이면 예외를 발생시킨다.")
        @Test
        void numberOfOrderTablesLessThanTwo_exception() {
            // given
            ArrayList<OrderTable> orderTables = new ArrayList<>();
            orderTables.add(orderTableDao.save(new OrderTable(1, false)));

            // then
            assertThatThrownBy(() -> orderTableGroupService.create(orderTables))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("이미 단체 지정이 된 주문 테이블이 있으면 예외를 발생시킨다.")
        @Test
        void alreadyOrderTableHasTableGroup_exception() {
            // given
            ArrayList<OrderTable> orderTables = new ArrayList<>();

            OrderTableGroup orderTableGroup = orderTableGroupDao.save(OrderTableGroup.group(List.of(
                    new OrderTable(3, false), new OrderTable(2, false)
            )));

            OrderTable orderTable = orderTableDao.save(new OrderTable(1, false));
            orderTables.add(orderTable);
            orderTables.addAll(orderTableDao.findAllByOrderTableGroupId(orderTableGroup.getId()));

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
            OrderTableGroup orderTableGroup = orderTableGroupDao.save(OrderTableGroup.group(
                    List.of(
                            new OrderTable(1, false),
                            new OrderTable(1, false),
                            new OrderTable(1, false)
                    ))
            );
            ;

            // when
            orderTableGroupService.ungroup(orderTableGroup.getId());
            OrderTableGroup savedOrderTableGroup = orderTableGroupDao.findById(orderTableGroup.getId()).get();

            // then
            List<OrderTable> actual = orderTableDao.findAllByOrderTableGroupId(savedOrderTableGroup.getId());
            assertThat(actual).hasSize(0);
        }

        @DisplayName("주문 테이블들 중 하나라도 조리중이거나 조리가 됐다면 예외를 발생시킨다.")
        @Test
        void orderTablesStatusIsMealOrCooking_exception() {
            // given
            OrderTableGroup orderTableGroup = orderTableGroupDao.save(OrderTableGroup.group(
                    List.of(
                            new OrderTable(1, false),
                            new OrderTable(1, false),
                            new OrderTable(1, false)
                    ))
            );
            ;
            OrderTable orderTable1 = orderTableGroup.getOrderTables().get(0);
            OrderTable orderTable2 = orderTableGroup.getOrderTables().get(2);

            MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("메뉴 그룹"));
            Product product = productDao.save(new Product("상품", 1000L));
            Menu menu = menuDao.save(new Menu("메뉴", new Price(1000L), menuGroup.getId(),
                    List.of(new MenuProduct(product, 2))));
            orderDao.save(new Order(orderTable1, OrderStatus.COMPLETION.name(),
                    LocalDateTime.now(), List.of(new OrderLineItem(menu, 2))));
            orderDao.save(new Order(orderTable2, OrderStatus.MEAL.name(),
                    LocalDateTime.now(), List.of(new OrderLineItem(menu, 2))));

            // then
            assertThatThrownBy(() -> orderTableGroupService.ungroup(orderTableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
