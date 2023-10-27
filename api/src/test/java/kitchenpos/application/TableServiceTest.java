package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.application.dto.ChangeEmptyOrderTableDto;
import kitchenpos.application.dto.ChangeNumberOfGuestsOrderTableDto;
import kitchenpos.application.dto.CreateOrderTableDto;
import kitchenpos.application.dto.ReadOrderTableDto;
import kitchenpos.application.exception.OrderTableNotFoundException;
import kitchenpos.config.IntegrationTest;
import kitchenpos.domain.exception.InvalidNumberOfGuestsException;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.ordertable.OrderTableRepository;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.tablegroup.TableGroupRepository;
import kitchenpos.ui.dto.request.CreateOrderTableRequest;
import kitchenpos.ui.dto.request.UpdateOrderTableEmptyRequest;
import kitchenpos.ui.dto.request.UpdateOrderTableNumberOfGuestsRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest {

    @Autowired
    OrderTableRepository orderTableRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    TableGroupRepository tableGroupRepository;

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    TableService tableService;

    @Test
    void create_메서드는_orderTable을_전달하면_orderTable을_저장하고_반환한다() {
        // given
        final CreateOrderTableRequest request = new CreateOrderTableRequest(0, false);

        // when
        final CreateOrderTableDto actual = tableService.create(request);

        // then
        assertThat(actual.getId()).isPositive();
    }

    @Test
    void list_메서드는_저장한_모든_orderTable을_반환한다() {
        // given
        final OrderTable persistOrderTable = orderTableRepository.save(new OrderTable(0, false));

        // when
        final List<ReadOrderTableDto> actual = tableService.list();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0).getId()).isEqualTo(persistOrderTable.getId())
        );
    }

    @Test
    void changeEmpty_메서드는_변경할_orderTableId와_변경한_값을_가진_orderTable을_전달하면_empty를_변경한다() {
        // given
        final OrderTable persistOrderTable = orderTableRepository.save(new OrderTable(0, true));
        persistOrderTable.group(1L);
        final UpdateOrderTableEmptyRequest request = new UpdateOrderTableEmptyRequest(false);

        // when
        final ChangeEmptyOrderTableDto actual =
                tableService.changeEmpty(persistOrderTable.getId(), request);

        // then
        assertThat(actual.isEmpty()).isEqualTo(actual.isEmpty());
    }

    @Test
    void changeEmpty_메서드는_존재하지_않는_orderTableId를_전달하면_예외가_발생한다() {
        // given
        final UpdateOrderTableEmptyRequest request = new UpdateOrderTableEmptyRequest(false);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(-999L, request))
                .isInstanceOf(OrderTableNotFoundException.class);
    }

    @Test
    void changeNumberOfGuests_메서드는_변경할_orderTableId와_변경할_값을_가진_orderTable을_전달하면_numberOfGuests를_변경한다() {
        // given
        final OrderTable persistOrderTable = orderTableRepository.save(new OrderTable(0, false));
        final UpdateOrderTableNumberOfGuestsRequest request = new UpdateOrderTableNumberOfGuestsRequest(1);

        // when
        final ChangeNumberOfGuestsOrderTableDto actual =
                tableService.changeNumberOfGuests(persistOrderTable.getId(), request);

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
    }

    @ParameterizedTest(name = "numberOfGuests가 {0}이면 예외가 발생한다.")
    @ValueSource(ints = {-1, -2, -3})
    void changeNumberOfGuests_메서드는_0_이하의_numberOfGuests를_전달하면_예외가_발생한다(final int invalidNumberOfGuests) {
        // given
        final OrderTable persistOrderTable = orderTableRepository.save(new OrderTable(0, false));
        final UpdateOrderTableNumberOfGuestsRequest invalidRequest =
                new UpdateOrderTableNumberOfGuestsRequest(invalidNumberOfGuests);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(persistOrderTable.getId(), invalidRequest))
                .isInstanceOf(InvalidNumberOfGuestsException.class);
    }

    @Test
    void changeNumberOfGuests_메서드는_존재하지_않는_orderTableId를_전달하면_예외가_발생한다() {
        // given
        final UpdateOrderTableNumberOfGuestsRequest request = new UpdateOrderTableNumberOfGuestsRequest(1);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(-999L, request))
                .isInstanceOf(OrderTableNotFoundException.class);
    }
}
