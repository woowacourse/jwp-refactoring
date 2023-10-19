package kitchenpos.application;

import kitchenpos.application.dto.CreateOrderTableIdDto;
import kitchenpos.application.dto.CreateTableGroupDto;
import kitchenpos.application.dto.OrderTableDto;
import kitchenpos.application.dto.TableGroupDto;
import kitchenpos.domain.order.GuestNumber;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;
import kitchenpos.exception.OrderTableException;
import kitchenpos.exception.TableGroupException;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class TableGroupServiceTest extends MockServiceTest {

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @Test
    void 테이블그룹을_추가한다() {
        // given
        OrderTable firstOrderTable = new OrderTable(new GuestNumber(1), true);
        OrderTable secondOrderTable = new OrderTable(new GuestNumber(1), true);

        BDDMockito.given(orderTableRepository.findAllByIdIn(BDDMockito.anyCollection()))
                .willReturn(List.of(firstOrderTable, secondOrderTable));

        CreateTableGroupDto createTableGroupDto
                = new CreateTableGroupDto(List.of(
                new CreateOrderTableIdDto(1L),
                new CreateOrderTableIdDto(2L)));

        // when
        TableGroupDto actual = tableGroupService.create(createTableGroupDto);
        List<Boolean> actualOrderTableEmpty = actual.getOrderTables().stream()
                .map(OrderTableDto::getEmpty)
                .collect(Collectors.toList());

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(actualOrderTableEmpty.size()).isEqualTo(2);
        softAssertions.assertThat(actualOrderTableEmpty).containsExactlyInAnyOrderElementsOf(List.of(false, false));
        softAssertions.assertAll();
    }

    @Test
    void 테이블그룹을_추가할_때_주문테이블이_포함되어_있지_않으면_예외를_던진다() {
        // given
        BDDMockito.given(orderTableRepository.findAllByIdIn(BDDMockito.anyCollection()))
                .willReturn(Collections.emptyList());

        CreateTableGroupDto createTableGroupDto
                = new CreateTableGroupDto(Collections.emptyList());

        // when, then
        Assertions.assertThatThrownBy(() -> tableGroupService.create(createTableGroupDto))
                .isInstanceOf(TableGroupException.class);
    }

    @Test
    void 테이블그룹을_추가할_때_주문테이블이_2개_미만이면_예외를_던진다() {
        // given
        OrderTable firstOrderTable = new OrderTable(new GuestNumber(1), true);

        BDDMockito.given(orderTableRepository.findAllByIdIn(BDDMockito.anyCollection()))
                .willReturn(List.of(firstOrderTable));

        CreateTableGroupDto createTableGroupDto
                = new CreateTableGroupDto(List.of(
                new CreateOrderTableIdDto(1L)));

        // when, then
        Assertions.assertThatThrownBy(() -> tableGroupService.create(createTableGroupDto))
                .isInstanceOf(TableGroupException.class);
    }

    @Test
    void 테이블그룹을_추가할_때_주문테이블이_존재하지_않는_주문테이블이면_예외를_던진다() {
        // given
        BDDMockito.given(orderTableRepository.findAllByIdIn(BDDMockito.anyCollection()))
                .willReturn(Collections.emptyList());

        CreateTableGroupDto createTableGroupDto
                = new CreateTableGroupDto(List.of(
                new CreateOrderTableIdDto(1L),
                new CreateOrderTableIdDto(2L)));

        // when, then
        Assertions.assertThatThrownBy(() -> tableGroupService.create(createTableGroupDto))
                .isInstanceOf(OrderTableException.class);
    }

    @Test
    void 테이블그룹을_추가할_때_주문테이블이_주문이_가능한_상태이면_예외를_던진다() {
        // given
        OrderTable firstOrderTable = new OrderTable(new GuestNumber(1), false);
        OrderTable secondOrderTable = new OrderTable(new GuestNumber(1), true);

        BDDMockito.given(orderTableRepository.findAllByIdIn(BDDMockito.anyCollection()))
                .willReturn(List.of(firstOrderTable, secondOrderTable));

        CreateTableGroupDto createTableGroupDto
                = new CreateTableGroupDto(List.of(
                new CreateOrderTableIdDto(1L),
                new CreateOrderTableIdDto(2L)));

        // when, then
        Assertions.assertThatThrownBy(() -> tableGroupService.create(createTableGroupDto))
                .isInstanceOf(TableGroupException.class);
    }

    @Test
    void 테이블그룹을_추가할_때_주문테이블이_이미_다른_테이블_그룹에_속해있으면_예외를_던진다() {
        // given
        OrderTable firstOrderTable = new OrderTable(new GuestNumber(1), true);
        firstOrderTable.changeTableGroup(new TableGroup(LocalDateTime.now()));
        OrderTable secondOrderTable = new OrderTable(new GuestNumber(1), true);

        BDDMockito.given(orderTableRepository.findAllByIdIn(BDDMockito.anyCollection()))
                .willReturn(List.of(firstOrderTable, secondOrderTable));

        CreateTableGroupDto createTableGroupDto
                = new CreateTableGroupDto(List.of(
                new CreateOrderTableIdDto(1L),
                new CreateOrderTableIdDto(2L)));

        // when, then
        Assertions.assertThatThrownBy(() -> tableGroupService.create(createTableGroupDto))
                .isInstanceOf(TableGroupException.class);
    }

    @Test
    void 테이블그룹을_삭제하면_주문테이블이_주문이_가능한_상태로_바뀌고_테이블_그룹_아이디가_null_로_바뀐다() {
        // given
        TableGroup tableGroup = new TableGroup(LocalDateTime.now());

        OrderTable firstOrderTable = new OrderTable(new GuestNumber(1), true);
        OrderTable secondOrderTable = new OrderTable(new GuestNumber(1), true);

        BDDMockito.given(tableGroupRepository.findById(BDDMockito.anyLong()))
                .willReturn(Optional.of(tableGroup));
        BDDMockito.given(orderTableRepository.findAllByTableGroupIdWithOrders(BDDMockito.anyLong()))
                .willReturn(List.of(firstOrderTable, secondOrderTable));

        // when
        tableGroupService.ungroup(1L);

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(firstOrderTable.getEmpty()).isFalse();
        softAssertions.assertThat(firstOrderTable.getTableGroup()).isNull();
        softAssertions.assertThat(secondOrderTable.getEmpty()).isFalse();
        softAssertions.assertThat(secondOrderTable.getTableGroup()).isNull();
        softAssertions.assertAll();
    }

    @Test
    void 테이블그룹을_삭제할_때_주문테이블_내에_존재하는_주문들_중_COOKING_또는_MEAL_상태가_존재하면_예외를_던진다() {
        // given
        TableGroup tableGroup = new TableGroup(LocalDateTime.now());

        OrderTable firstOrderTable = new OrderTable(new GuestNumber(1), false);
        Order order = new Order(LocalDateTime.now());
        firstOrderTable.addOrder(order);

        BDDMockito.given(tableGroupRepository.findById(BDDMockito.anyLong()))
                .willReturn(Optional.of(tableGroup));
        BDDMockito.given(orderTableRepository.findAllByTableGroupIdWithOrders(BDDMockito.anyLong()))
                .willReturn(List.of(firstOrderTable));

        // when, then
        Assertions.assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(OrderTableException.class);
    }
}
