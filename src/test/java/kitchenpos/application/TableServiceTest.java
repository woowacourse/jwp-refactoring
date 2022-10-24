package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.exception.InvalidOrderStatusException;
import kitchenpos.exception.InvalidOrderTableIsEmptyOfNumberOfGuestsException;
import kitchenpos.exception.InvalidOrderTableNumberOfGuestsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class TableServiceTest extends IntegrationTest {

    @Test
    void 테이블을_생성할_수_있다() {
        // given
        OrderTable orderTable = new OrderTable(null, 0, true);

        // when
        OrderTable extract = tableService.create(orderTable);

        // then
        assertThat(extract).isNotNull();
    }

    @Test
    void 테이블_목록들을_조회할_수_있다() {
        // when
        List<OrderTable> extract = tableService.list();

        // then
        assertThat(extract).hasSize(8);
    }

    @ParameterizedTest
    @CsvSource(value = {"COOKING", "MEAL"})
    void 테이블을_비어있는_상태로_변경할때_주문의_상태가_완료상태가_아니면_예외가_발생한다(final OrderStatus orderStatus) {
        // given
        MenuGroup menuGroup = menuGroupService.create(new MenuGroup("1인 메뉴"));
        Product product = productService.create(new Product("짜장면", BigDecimal.valueOf(1000)));
        Menu createMenu = new Menu("짜장면", BigDecimal.valueOf(1000), menuGroup.getId());
        createMenu.addMenuProducts(List.of(new MenuProduct(1L, null, product.getId(), 1)));
        Menu saveMenu = menuService.create(createMenu);
        OrderTable orderTable = tableService.create(new OrderTable(null, 2, false));
        Order order = orderService.create(new Order(orderTable.getId(), orderStatus.name(),
            LocalDateTime.now(), List.of(new OrderLineItem(saveMenu.getId(), 1))));

        // when & then
        assertThatThrownBy(
            () -> tableService.changeEmpty(orderTable.getId(), new OrderTable(orderTable.getId(), 2, false)))
            .isInstanceOf(InvalidOrderStatusException.class);
    }

    @Test
    void 테이블의_손님수를_변경한다() {
        // given
        OrderTable orderTable = tableService.create(new OrderTable(null, 2, false));

        // when
        OrderTable extract = tableService.changeNumberOfGuests(orderTable.getId(), new OrderTable(null, 3, false));

        // then
        assertThat(extract.getNumberOfGuests()).isEqualTo(3);
    }

    @Test
    void 테이블의_손님수를_0명_미만으로_변경하면_예외가_발생한다() {
        OrderTable orderTable = tableService.create(new OrderTable(null, 2, false));

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), new OrderTable(null, -1, false)))
            .isInstanceOf(InvalidOrderTableNumberOfGuestsException.class);
    }

    @Test
    void 빈_테이블에_손님수를_변경할경우_예외가_발생한다() {
        // given
        OrderTable extract = tableService.create(new OrderTable(null, 0, true));

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(extract.getId(), new OrderTable(null, 1, true)))
            .isInstanceOf(InvalidOrderTableIsEmptyOfNumberOfGuestsException.class);
    }
}