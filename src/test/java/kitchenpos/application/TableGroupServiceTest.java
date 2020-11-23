package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.fixture.OrderTableFixture.createOrderTableWithEmpty;
import static kitchenpos.fixture.OrderTableFixture.createOrderTableWithId;
import static kitchenpos.fixture.OrderTableFixture.createOrderTableWithTableGroup;
import static kitchenpos.fixture.TableGroupFixture.createTableGroupWithoutId;
import static kitchenpos.utils.TestUtils.findById;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
public class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("TableGroup 정상 생성")
    @Test
    void create() {
        List<OrderTable> savedOrderTables = Arrays.asList(
                orderTableRepository.save(createOrderTableWithEmpty(true)),
                orderTableRepository.save(createOrderTableWithEmpty(true))
        );
        TableGroupRequest tableGroupRequest = createTableRequest(savedOrderTables);

        TableGroup actual = tableGroupService.create(tableGroupRequest);

        assertAll(() -> {
            assertThat(actual.getId()).isNotNull();
            assertThat(actual.getCreatedDate()).isNotNull();
        });
    }

    @DisplayName("TableGroup의 orderTable들이 비어있는 경우 예외 반환")
    @Test
    void createEmptyOrderTables() {
        List<OrderTableRequest> orderTableRequests = null;
        TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableRequests);

        assertThatNullPointerException()
                .isThrownBy(() -> tableGroupService.create(tableGroupRequest));
    }

    @DisplayName("TableGroup의 orderTable 개수가 2개 미만인 경우 예외 반환")
    @Test
    void createWrongOrderTablesCount() {
        OrderTable savedOrderTable = orderTableRepository.save(createOrderTableWithEmpty(true));
        TableGroupRequest tableGroupRequest = createTableRequest(Arrays.asList(savedOrderTable));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .withMessage("table group에 속한 테이블은 2개 이상이여야 합니다.");
    }

    @DisplayName("TableGroup의 orderTable이 한개라도 DB에 등록되어 있지 않은 경우 예외 반환")
    @Test
    void createUnregisteredOrderTables() {
        OrderTable savedOrderTable = orderTableRepository.save(createOrderTableWithEmpty(true));
        List<OrderTable> orderTables = Arrays.asList(
                savedOrderTable, createOrderTableWithId(3L));
        TableGroupRequest tableGroupRequest = createTableRequest(orderTables);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .withMessage("요청한 Table 중 DB에 저장되어있지 않은 Table이 있습니다.");
    }

    @DisplayName("TableGroup의 orderTable이 비어 있지않은 경우 예외 반환")
    @Test
    void createNotEmptyOrderTables() {
        List<OrderTable> orderTables = Arrays.asList(
                orderTableRepository.save(createOrderTableWithEmpty(false)),
                orderTableRepository.save(createOrderTableWithEmpty(true))
        );
        TableGroupRequest tableRequest = createTableRequest(orderTables);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableRequest))
                .withMessage("이미 테이블 그룹에 속한 테이블이거나 비어있지 않은 테이블입니다.");
    }

    @DisplayName("TableGroup의 orderTable TableGroupId가 null이 아닌 경우 예외 반환")
    @Test
    void createNonNullTableGroupIdOrderTables() {
        TableGroup savedTableGroup = tableGroupRepository.save(createTableGroupWithoutId());
        List<OrderTable> orderTables = Arrays.asList(
                orderTableRepository.save(createOrderTableWithTableGroup(savedTableGroup)),
                orderTableRepository.save(createOrderTableWithEmpty(true))
        );
        TableGroupRequest tableRequest = createTableRequest(orderTables);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableRequest))
                .withMessage("이미 테이블 그룹에 속한 테이블이거나 비어있지 않은 테이블입니다.");
    }

    private TableGroupRequest createTableRequest(List<OrderTable> orderTables) {
        List<OrderTableRequest> orderTableRequests = orderTables.stream()
                .map(OrderTable::getId)
                .map(OrderTableRequest::new)
                .collect(Collectors.toList());
        return new TableGroupRequest(orderTableRequests);
    }

    @DisplayName("TableGroup 정상 해제")
    @Test
    void ungroup() {
        TableGroup savedTableGroup = tableGroupRepository.save(createTableGroupWithoutId());
        OrderTable savedOrderTable = orderTableRepository.save(createOrderTableWithTableGroup(savedTableGroup));

        tableGroupService.ungroup(savedTableGroup.getId());
        OrderTable actual = findById(orderTableRepository, savedOrderTable.getId());

        assertThat(actual.getTableGroup()).isNull();
    }

    @DisplayName("TableGroup 해제 시 해당테이블 중 조리중이거나 식사중인 테이블이 있는 경우 예외 반환")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void ungroupCookingOrMealOrderTable(OrderStatus orderStatus) {
        TableGroup savedTableGroup = tableGroupRepository.save(createTableGroupWithoutId());
        OrderTable savedOrderTable = orderTableRepository.save(createOrderTableWithTableGroup(savedTableGroup));
        orderRepository.save(OrderFixture.createOrderWithOrderTableAndOrderStatus(savedOrderTable, orderStatus));


        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .withMessage("조리중이나 식사중일 땐 테이블 그룹 해제가 불가능합니다.");
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
        orderTableRepository.deleteAll();
        tableGroupRepository.deleteAll();
    }
}
