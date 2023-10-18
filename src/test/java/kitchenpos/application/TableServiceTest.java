package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.config.ServiceTest;
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
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.request.CreateOrderTableRequest;
import kitchenpos.ui.dto.request.UpdateOrderTableEmptyRequest;
import kitchenpos.ui.dto.request.UpdateOrderTableNumberOfGuestsRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest {

    @Autowired
    OrderTableDao orderTableDao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    TableGroupDao tableGroupDao;

    @Autowired
    MenuGroupDao menuGroupDao;

    @Autowired
    ProductDao productDao;

    @Autowired
    MenuDao menuDao;

    @Autowired
    TableService tableService;

    @Test
    void create_메서드는_orderTable을_전달하면_orderTable을_저장하고_반환한다() {
        // given
        final CreateOrderTableRequest request = new CreateOrderTableRequest(0, false);

        // when
        final OrderTable actual = tableService.create(request);

        // then
        assertThat(actual.getId()).isPositive();
    }

    @Test
    void list_메서드는_저장한_모든_orderTable을_반환한다() {
        // given
        final OrderTable persistOrderTable = orderTableDao.save(new OrderTable(0, false));

        // when
        final List<OrderTable> actual = tableService.list();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0).getId()).isEqualTo(persistOrderTable.getId())
        );
    }

    @Test
    void changeEmpty_메서드는_변경할_orderTableId와_변경한_값을_가진_orderTable을_전달하면_empty를_변경한다() {
        // given
        final OrderTable persistOrderTable = orderTableDao.save(new OrderTable(0, true));
        final UpdateOrderTableEmptyRequest request = new UpdateOrderTableEmptyRequest(false);

        // when
        final OrderTable actual = tableService.changeEmpty(persistOrderTable.getId(), request);

        // then
        assertThat(actual.isEmpty()).isEqualTo(actual.isEmpty());
    }

    @Test
    void changeEmpty_메서드는_존재하지_않는_orderTableId를_전달하면_예외가_발생한다() {
        // given
        final UpdateOrderTableEmptyRequest request = new UpdateOrderTableEmptyRequest(false);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(-999L, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "orderStatus가 {0}이면 예외가 발생한다.")
    @ValueSource(strings = {"COOKING", "MEAL"})
    void changeEmpty_메서드는_변경할_orderTableId의_orderStatu가_COMPLETION이_아니면_예외가_발생한다(final String invalidOrderStatus) {
        // given
        final MenuGroup persistMenuGroup = menuGroupDao.save(new MenuGroup("메뉴 그룹"));
        final Product persistProduct = productDao.save(new Product("상품", BigDecimal.TEN));
        final MenuProduct persistMenuProduct = new MenuProduct(persistProduct, 1);
        final Menu menu = Menu.of(
                "메뉴",
                BigDecimal.TEN,
                List.of(persistMenuProduct),
                persistMenuGroup
        );
        final Menu persistMenu = menuDao.save(menu);
        final OrderTable persistOrderTable = orderTableDao.save(new OrderTable(0, false));
        final OrderLineItem orderLineItem = new OrderLineItem(persistMenu, 1L);
        final OrderStatus orderStatus = OrderStatus.valueOf(invalidOrderStatus);
        final Order order = new Order(
                persistOrderTable,
                orderStatus,
                LocalDateTime.now().minusHours(3),
                List.of(orderLineItem)
        );
        orderDao.save(order);
        final UpdateOrderTableEmptyRequest request = new UpdateOrderTableEmptyRequest(false);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(persistOrderTable.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuests_메서드는_변경할_orderTableId와_변경할_값을_가진_orderTable을_전달하면_numberOfGuests를_변경한다() {
        // given
        final OrderTable persistOrderTable = orderTableDao.save(new OrderTable(0, false));
        final UpdateOrderTableNumberOfGuestsRequest request = new UpdateOrderTableNumberOfGuestsRequest(1);

        // when
        final OrderTable actual = tableService.changeNumberOfGuests(persistOrderTable.getId(), request);

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
    }

    @ParameterizedTest(name = "numberOfGuests가 {0}이면 예외가 발생한다.")
    @ValueSource(ints = {-1, -2, -3})
    void changeNumberOfGuests_메서드는_0_이하의_numberOfGuests를_전달하면_예외가_발생한다(final int invalidNumberOfGuests) {
        // given
        final OrderTable persistOrderTable = orderTableDao.save(new OrderTable(0, false));
        final UpdateOrderTableNumberOfGuestsRequest invalidRequest =
                new UpdateOrderTableNumberOfGuestsRequest(invalidNumberOfGuests);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(persistOrderTable.getId(), invalidRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuests_메서드는_존재하지_않는_orderTableId를_전달하면_예외가_발생한다() {
        // given
        final UpdateOrderTableNumberOfGuestsRequest request = new UpdateOrderTableNumberOfGuestsRequest(1);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(-999L, request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
