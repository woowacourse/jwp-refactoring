package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.dao.TableRepository;
import kitchenpos.domain.GuestNumber;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupDto;
import kitchenpos.exception.GroupTableNotEnoughException;
import kitchenpos.exception.GroupedTableNotEmptyException;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.exception.TableAlreadyGroupedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceTest {

    private static final GuestNumber GUEST_NUMBER = GuestNumber.from(5);

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderDao orderDao;

    private OrderTable orderTable1;

    @BeforeEach
    void setUp() {
        orderTable1 = tableRepository.save(new OrderTable(null, GUEST_NUMBER, true, null));
    }

    @DisplayName("TableGroup을 생성할 수 있다.")
    @Test
    void create() {
        OrderTable orderTable2 = tableRepository.save(new OrderTable(null, GUEST_NUMBER, true, null));

        TableGroupDto tableGroupDto = tableGroupService.create(
                new TableGroupDto(List.of(orderTable1.getId(), orderTable2.getId())));

        OrderTable groupedOrderOrderTable1 = tableRepository.findById(orderTable1.getId())
                .orElseThrow(OrderTableNotFoundException::new);
        OrderTable groupedOrderOrderTable2 = tableRepository.findById(orderTable2.getId())
                .orElseThrow(OrderTableNotFoundException::new);
        assertAll(
                () -> assertThat(groupedOrderOrderTable1.getTableGroup().getId()).isEqualTo(tableGroupDto.getId()),
                () -> assertThat(groupedOrderOrderTable2.getTableGroup().getId()).isEqualTo(tableGroupDto.getId())
        );
    }

    @DisplayName("2개 미만의 OrderTable로 TableGroup을 생성하려고 하면 예외를 발생시킨다.")
    @Test
    void create_Exception_NotEnoughOrderTableNumber() {
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupDto(List.of(orderTable1.getId()))))
                .isInstanceOf(GroupTableNotEnoughException.class);
    }

    @DisplayName("존재하지 않는 OrderTable로 TableGroup을 생성하려고 하면 예외를 발생시킨다.")
    @Test
    void create_Exception_NotFoundOrderTable() {
        Long notFountTableId1 = 1000L;
        Long notFountTableId2 = 1001L;

        assertThatThrownBy(() -> tableGroupService.create(
                new TableGroupDto(List.of(orderTable1.getId(), notFountTableId1, notFountTableId2))))
                .isInstanceOf(OrderTableNotFoundException.class);
    }

    @DisplayName("empty가 아닌 OrderTable로 TableGroup을 생성하려고 하면 예외를 발생시킨다.")
    @Test
    void create_Exception_NotEmptyOrderTable() {
        OrderTable orderTable2 = tableRepository.save(new OrderTable(null, GUEST_NUMBER, false, null));

        assertThatThrownBy(
                () -> tableGroupService.create(new TableGroupDto(List.of(orderTable1.getId(), orderTable2.getId()))))
                .isInstanceOf(GroupedTableNotEmptyException.class);
    }

    @DisplayName("이미 TableGroup에 속해 있는 OrderTable로 TableGroup을 생성하려고 하면 예외를 발생시킨다.")
    @Test
    void create_Exception_AlreadyGroupedOrderTable() {
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        OrderTable orderTable2 = tableRepository.save(new OrderTable(null, GUEST_NUMBER, true, tableGroup));

        assertThatThrownBy(
                () -> tableGroupService.create(new TableGroupDto(List.of(orderTable1.getId(), orderTable2.getId()))))
                .isInstanceOf(TableAlreadyGroupedException.class);
    }

    @DisplayName("TableGroup을 그룹 해제할 수 있다.")
    @Test
    void ungroup() {
        OrderTable orderTable2 = tableRepository.save(new OrderTable(null, GUEST_NUMBER, true, null));
        TableGroupDto tableGroupDto = tableGroupService
                .create(new TableGroupDto(List.of(orderTable1.getId(), orderTable2.getId())));

        tableGroupService.ungroup(tableGroupDto.getId());

        OrderTable groupedOrderOrderTable1 = tableRepository.findById(orderTable1.getId())
                .orElseThrow(OrderTableNotFoundException::new);
        OrderTable groupedOrderOrderTable2 = tableRepository.findById(orderTable2.getId())
                .orElseThrow(OrderTableNotFoundException::new);
        assertAll(
                () -> assertThat(groupedOrderOrderTable1.getTableGroup()).isNull(),
                () -> assertThat(groupedOrderOrderTable2.getTableGroup()).isNull()
        );
    }

    @DisplayName("조리중이거나 식사중인 테이블이 존재하는 TableGroup을 그룹 해제할 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void ungroup_Exception_NotCompleteOrderTableStatus(String orderStatus) {
        OrderTable orderOrderTable2 = tableRepository.save(new OrderTable(null, GUEST_NUMBER, true, null));
        TableGroupDto tableGroupDto = tableGroupService.create(
                new TableGroupDto(List.of(orderTable1.getId(), orderOrderTable2.getId())));
        orderDao.save(new Order(orderOrderTable2.getId(), orderStatus, Collections.emptyList()));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupDto.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("조리중이거나 식사중인 테이블이 포함된 Table Group은 그룹 해제 할 수 없습니다.");
    }
}
