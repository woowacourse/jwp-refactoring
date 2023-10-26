package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.OrderTableCreateDto;
import kitchenpos.application.dto.OrderTableUpdateGuestDto;
import kitchenpos.exception.OrderException.InvalidOrderStatusChangeException;
import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuGroup;
import kitchenpos.menu.MenuGroupRepository;
import kitchenpos.menu.MenuProduct;
import kitchenpos.menu.MenuRepository;
import kitchenpos.order.Order;
import kitchenpos.order.OrderLineItem;
import kitchenpos.order.OrderRepository;
import kitchenpos.order.OrderStatus;
import kitchenpos.order.OrderTableChangeService;
import kitchenpos.product.Product;
import kitchenpos.product.ProductRepository;
import kitchenpos.table.OrderStatusChecker;
import kitchenpos.table.OrderTable;
import kitchenpos.table.OrderTableRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Sql(scripts = {"classpath:truncate.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableChangeService orderTableChangeService;

    @Autowired
    private OrderStatusChecker orderStatusChecker;

    @Test
    void 테이블을_등록할_수_있다() {
        // given
        final OrderTableCreateDto orderTableCreateDto = new OrderTableCreateDto(0);

        // when
        final OrderTable result = tableService.create(orderTableCreateDto);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.isEmpty()).isFalse();
        assertThat(result.getNumberOfGuests()).isZero();
    }

    @Test
    void 테이블목록을_조회할_수_있다() {
        // given
        createOrderTable(true, 0);

        // when
        final List<OrderTable> results = tableService.list();

        // then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).isEmpty()).isTrue();
        assertThat(results.get(0).getNumberOfGuests()).isZero();
    }

    @Test
    void 테이블의_비어있는_상태를_변경할_수_있다() {
        // given
        final OrderTable savedOrderTable = createOrderTable(true, 0);

        // when
        final OrderTable result = tableService.changeEmpty(savedOrderTable.getId());

        // then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void 테이블의_손님수를_변경할_수_있다() {
        // given
        final OrderTableCreateDto orderTableCreateDto = new OrderTableCreateDto(0);

        final OrderTable savedTable = tableService.create(orderTableCreateDto);

        // when
        final OrderTableUpdateGuestDto orderTableUpdateGuestDto = new OrderTableUpdateGuestDto(4);
        final OrderTable result = tableService.changeNumberOfGuests(savedTable.getId(),
            orderTableUpdateGuestDto);

        // then
        assertThat(result.getNumberOfGuests()).isEqualTo(4);
    }

    @Test
    void 빈_테이블에_손님수를_변경할_때_예외가_발생한다() {
        // given
        final OrderTable savedOrderTable = createOrderTable(true, 0);

        // when
        final OrderTableUpdateGuestDto orderTableUpdateGuestDto = new OrderTableUpdateGuestDto(4);

        // then
        assertThatThrownBy(
            () -> tableService.changeNumberOfGuests(savedOrderTable.getId(),
                orderTableUpdateGuestDto))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 손님수가_음수일_때_예외가_발생한다() {
        // given when
        final OrderTable savedOrderTable = createOrderTable(true, 0);

        // when
        final OrderTableUpdateGuestDto orderTableUpdateGuestDto = new OrderTableUpdateGuestDto(-4);

        // then
        assertThatThrownBy(
            () -> tableService.changeNumberOfGuests(savedOrderTable.getId(),
                orderTableUpdateGuestDto))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_테이블을_수정할때_예외가_발생한다() {
        // given when
        final Long notExistOrderTableId = 99999L;

        // then
        assertThatThrownBy(
            () -> tableService.changeEmpty(notExistOrderTableId))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 조리_또는_식사_중인_테이블의_빈상태를_변경할_때_예외가_발생한다() {
        // given when
        final OrderTable savedOrderTable = createOrderTable(false, 2);
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("테스트 메뉴그룹"));
        final Product product = productRepository.save(new Product("상품", BigDecimal.valueOf(2000)));
        final MenuProduct menuProduct = new MenuProduct(product.getName(), product.getPrice(), 1L);
        final Menu menu = menuRepository.save(Menu.of("테스트 메뉴", BigDecimal.valueOf(1000), menuGroup,
            List.of(menuProduct)));

        final OrderLineItem orderLineItem = new OrderLineItem(menu.getName(), menu.getPrice(), 2);
        final Order order = Order.of(savedOrderTable.getId(), orderTableChangeService,
            List.of(orderLineItem));
        order.changeOrderStatus(OrderStatus.COOKING);
        orderRepository.save(order);

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId()))
            .isInstanceOf(InvalidOrderStatusChangeException.class);
    }

    private OrderTable createOrderTable(final boolean emptyStatus, final int guests) {
        final OrderTable orderTable = new OrderTable(guests);
        orderTableRepository.save(orderTable);
        orderTable.changeEmpty(orderStatusChecker, emptyStatus);
        return orderTable;
    }
}
