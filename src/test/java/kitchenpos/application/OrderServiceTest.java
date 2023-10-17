package kitchenpos.application;

import kitchenpos.common.service.ServiceTest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private OrderDao orderDao;

    @Test
    void Order을_생성할_수_있다() {
        //given
        final TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), null));
        final OrderTable orderTable = orderTableDao.save(new OrderTable(tableGroup.getId(), 0, false));
        final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("마라탕그룹"));
        final Menu menu = menuDao.save(new Menu("디노 마라탕", new BigDecimal(20000), menuGroup.getId()));
        final OrderLineItem orderLineItem = new OrderLineItem(null, menu.getId(), 1);

        //when
        final Order order = orderService.create(new Order(orderTable.getId(), null, LocalDateTime.now(),
                List.of(orderLineItem)));

        //then
        assertThat(order.getId()).isNotNull();

    }

    @Test
    void 주문_항목이_없으면_예외가_발생한다() {
        //given
        final TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), null));
        final OrderTable orderTable = orderTableDao.save(new OrderTable(tableGroup.getId(), 0, false));

        //when, then
        assertThatThrownBy(() -> orderService.create(new Order(orderTable.getId(), null, LocalDateTime.now(), null)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_항목_사이즈가_맞지_않으면_예외가_발생한다() {
        //given
        final TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), null));
        final OrderTable orderTable = orderTableDao.save(new OrderTable(tableGroup.getId(), 0, false));
        final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("마라탕그룹"));
        final Menu menu = menuDao.save(new Menu("디노 마라탕", new BigDecimal(20000), menuGroup.getId()));
        final OrderLineItem orderLineItem = new OrderLineItem(null, menu.getId(), 1);

        //when, then
        assertThatThrownBy(() -> orderService.create(new Order(orderTable.getId(), null, LocalDateTime.now(),
                List.of(orderLineItem, orderLineItem))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_비어있으면_예외가_발생한다() {
        //given
        final TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), null));
        final OrderTable orderTable = orderTableDao.save(new OrderTable(tableGroup.getId(), 0, true));
        final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("마라탕그룹"));
        final Menu menu = menuDao.save(new Menu("디노 마라탕", new BigDecimal(20000), menuGroup.getId()));
        final OrderLineItem orderLineItem = new OrderLineItem(null, menu.getId(), 1);

        //when, then
        assertThatThrownBy(() -> orderService.create(new Order(orderTable.getId(), null, LocalDateTime.now(),
                List.of(orderLineItem))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_조회할_수_있다() {
        //given
        final TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), null));
        final OrderTable orderTable = orderTableDao.save(new OrderTable(tableGroup.getId(), 0, false));
        final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("마라탕그룹"));
        final Menu menu = menuDao.save(new Menu("디노 마라탕", new BigDecimal(20000), menuGroup.getId()));
        final OrderLineItem orderLineItem = new OrderLineItem(null, menu.getId(), 1);
        orderDao.save(new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), List.of(orderLineItem)));

        //when
        final List<Order> orders = orderService.list();

        //then
        assertThat(orders).hasSize(1);
    }

    @Test
    void 주문_상태를_변경할_수_있다() {
        //given
        final TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), null));
        final OrderTable orderTable = orderTableDao.save(new OrderTable(tableGroup.getId(), 0, false));
        final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("마라탕그룹"));
        final Menu menu = menuDao.save(new Menu("디노 마라탕", new BigDecimal(20000), menuGroup.getId()));
        final OrderLineItem orderLineItem = new OrderLineItem(null, menu.getId(), 1);
        final Order order = orderDao.save(new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(),
                List.of(orderLineItem)));

        //when
        final Order savedOrder = orderService.changeOrderStatus(order.getId(),
                new Order(null, OrderStatus.COMPLETION.name(), null, null));

        //then
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @Test
    void 주문_상태가_완료_상태면_예외_처리() {
        //given
        final TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), null));
        final OrderTable orderTable = orderTableDao.save(new OrderTable(tableGroup.getId(), 0, false));
        final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("마라탕그룹"));
        final Menu menu = menuDao.save(new Menu("디노 마라탕", new BigDecimal(20000), menuGroup.getId()));
        final OrderLineItem orderLineItem = new OrderLineItem(null, menu.getId(), 1);
        final Order order = orderDao.save(new Order(orderTable.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now(),
                List.of(orderLineItem)));

        //when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), order))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
