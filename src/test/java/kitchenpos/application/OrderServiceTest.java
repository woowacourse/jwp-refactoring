package kitchenpos.application;

import kitchenpos.common.service.ServiceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
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
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Test
    void Order을_생성할_수_있다() {
        //given
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(tableGroup.getId(), 0, false));
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("후라이드"));
        final Menu menu = menuRepository.save(new Menu("디노공룡메뉴", new BigDecimal(17000), menuGroup.getId()));

        //when
        final Long orderId = orderService.create(List.of(menu.getId()), List.of(2), orderTable.getId());

        //then
        assertThat(orderId).isNotNull();
    }

    @Test
    void 주문_항목_사이즈가_맞지_않으면_예외가_발생한다() {
        //given
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("후라이드"));
        final Menu menu = menuRepository.save(new Menu("디노공룡메뉴", new BigDecimal(17000), menuGroup.getId()));

        //when, then
        assertThatThrownBy(() -> orderService.create(List.of(menu.getId(), 2L), List.of(2), null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_비어있으면_예외가_발생한다() {
        //given
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(tableGroup.getId(), 0, true));
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("후라이드"));
        final Menu menu = menuRepository.save(new Menu("디노공룡메뉴", new BigDecimal(17000), menuGroup.getId()));

        //when, then
        assertThatThrownBy(() -> orderService.create(List.of(menu.getId()), List.of(2), orderTable.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_조회할_수_있다() {
        //given
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(tableGroup.getId(), 0, false));
        orderRepository.save(new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now()));

        //when
        final List<Order> orders = orderService.list();

        //then
        assertThat(orders).hasSize(1);
    }

    @Test
    void 주문_상태를_변경할_수_있다() {
        //given
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(tableGroup.getId(), 0, false));
        final Order order = orderRepository.save(new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now()));

        //when
        orderService.changeOrderStatus(order.getId(), OrderStatus.COMPLETION.name());

        //then
        final Order changedOrder = orderRepository.findById(order.getId()).orElseThrow(IllegalArgumentException::new);
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @Test
    void 주문_상태가_완료_상태면_예외_처리() {
        //given
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(tableGroup.getId(), 0, false));
        final Order order = orderRepository.save(new Order(orderTable.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now()));

        //when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), OrderStatus.COOKING.name()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
