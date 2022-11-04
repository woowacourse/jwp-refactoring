package kitchenpos.table.application;

import static kitchenpos.common.fixture.OrderFixtures.generateOrder;
import static kitchenpos.common.fixture.OrderTableFixtures.generateOrderTable;
import static kitchenpos.common.fixture.TableGroupFixtures.generateTableGroup;
import static kitchenpos.common.fixture.TableGroupFixtures.generateTableGroupSaveRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import kitchenpos.order.OrderRepository;
import kitchenpos.table.OrderTableRepository;
import kitchenpos.table.TableGroupRepository;
import kitchenpos.order.OrderStatus;
import kitchenpos.table.OrderTable;
import kitchenpos.table.TableGroup;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.dto.TableGroupSaveRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/truncate.sql")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TableGroupServiceTest {


    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupService tableGroupService;

    @Autowired
    public TableGroupServiceTest(final OrderRepository orderRepository,
                                 final OrderTableRepository orderTableRepository,
                                 final TableGroupRepository tableGroupRepository,
                                 final TableGroupService tableGroupService) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupService = tableGroupService;
    }

    @Test
    void tableGroup을_생성한다() {
        OrderTable 테이블_1번 = orderTableRepository.save(generateOrderTable(0, true));
        OrderTable 테이블_2번 = orderTableRepository.save(generateOrderTable(0, true));

        TableGroupResponse actual = tableGroupService.create(
                generateTableGroupSaveRequest(List.of(테이블_1번, 테이블_2번)));

        assertThat(actual.getOrderTables()).hasSize(2);
    }

    @Test
    void orderTables가_비어있으면_예외를_던진다() {
        TableGroupSaveRequest request = generateTableGroupSaveRequest(List.of());

        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void orderTables의_사이즈가_2미만인_경우_예외를_던진다() {
        OrderTable orderTable = orderTableRepository.save(generateOrderTable(0, true));

        assertThatThrownBy(() -> tableGroupService.create(generateTableGroupSaveRequest(List.of(orderTable))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void orderGroup이_가진_orderTables의_사이즈와_저장된_orderTables의_사이즈가_다른_경우_예외를_던진다() {
        OrderTable 테이블_1번 = orderTableRepository.save(generateOrderTable(0, true));
        OrderTable 테이블_2번 = orderTableRepository.save(generateOrderTable(0, true));
        OrderTable 테이블_3번 = generateOrderTable(0, true);

        assertThatThrownBy(
                () -> tableGroupService.create(
                        generateTableGroupSaveRequest(List.of(테이블_1번, 테이블_2번, 테이블_3번))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 저장된_orderTables_중_비어있지_않은_table이_존재하는_경우_예외를_던진다() {
        OrderTable 테이블_1번 = orderTableRepository.save(generateOrderTable(0, true));
        OrderTable 테이블_2번 = orderTableRepository.save(generateOrderTable(0, true));
        OrderTable 비어있지_않은_테이블 = orderTableRepository.save(generateOrderTable(0, false));

        assertThatThrownBy(
                () -> tableGroupService.create(
                        generateTableGroupSaveRequest(List.of(테이블_1번, 테이블_2번, 비어있지_않은_테이블))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 저장된_orderTables_중_tableGroupId가_null이_아닌_경우_예외를_던진다() {
        TableGroup tableGroup = tableGroupRepository.save(generateTableGroup(List.of()));
        OrderTable 테이블_1번 = orderTableRepository.save(generateOrderTable(tableGroup.getId(), 0, true));
        OrderTable 테이블_2번 = orderTableRepository.save(generateOrderTable(tableGroup.getId(), 0, true));

        OrderTable 테이블_3번 = orderTableRepository.save(generateOrderTable(0, true));

        assertThatThrownBy(
                () -> tableGroupService.create(
                        generateTableGroupSaveRequest(List.of(테이블_1번, 테이블_2번, 테이블_3번))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void tableGroup을_해제한다() {
        TableGroup tableGroup = tableGroupRepository.save(generateTableGroup(List.of()));
        orderTableRepository.save(generateOrderTable(tableGroup.getId(), 0, true));
        orderTableRepository.save(generateOrderTable(tableGroup.getId(), 0, true));

        tableGroupService.ungroup(tableGroup.getId());

        assertAll(() -> {
            List<OrderTable> actual = orderTableRepository.findAll();
            assertThat(actual.get(0).getTableGroupId()).isNull();
            assertThat(actual.get(0).isEmpty()).isFalse();
            assertThat(actual.get(1).getTableGroupId()).isNull();
            assertThat(actual.get(1).isEmpty()).isFalse();
        });
    }

    @ParameterizedTest(name = "orderTables의 orderStatus가 {0} 인 경우 예외를 던진다.")
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void orderTables의_orderStatus가_COOKING_MEAL인_경우_예외를_던진다(final OrderStatus orderStatus) {
        TableGroup tableGroup = tableGroupRepository.save(generateTableGroup(List.of()));
        OrderTable 테이블_1번 = orderTableRepository.save(generateOrderTable(tableGroup.getId(), 0, true));
        OrderTable 테이블_2번 = orderTableRepository.save(generateOrderTable(tableGroup.getId(), 0, true));

        orderRepository.save(generateOrder(테이블_1번.getId(), orderStatus, List.of()));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
