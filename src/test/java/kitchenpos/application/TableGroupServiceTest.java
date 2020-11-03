package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.OrderFixture.createOrderWithOrderStatusAndTableId;
import static kitchenpos.fixture.OrderTableFixture.createOrderTableWithEmpty;
import static kitchenpos.fixture.OrderTableFixture.createOrderTableWithTableGroupId;
import static kitchenpos.fixture.TableGroupFixture.createTableGroupWithOrderTables;
import static kitchenpos.fixture.TableGroupFixture.createTableGroupWithoutId;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Sql(value = "/truncate.sql")
public class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("TableGroup 정상 생성")
    @Test
    void create() {
        List<OrderTable> orderTables = Arrays.asList(
                orderTableDao.save(createOrderTableWithEmpty(true)),
                orderTableDao.save(createOrderTableWithEmpty(true))
        );
        TableGroup tableGroup = createTableGroupWithOrderTables(orderTables);

        TableGroup actual = tableGroupService.create(tableGroup);

        assertAll(() -> {
            assertThat(actual.getId()).isNotNull();
            assertThat(actual.getCreatedDate()).isNotNull();
            assertThat(actual.getOrderTables())
                    .extracting(OrderTable::getId, OrderTable::getTableGroupId)
                    .doesNotContainNull();
            assertThat(actual.getOrderTables())
                    .extracting(OrderTable::isEmpty)
                    .containsOnly(false);
        });
    }

    @DisplayName("TableGroup의 orderTable들이 비어있는 경우 예외 반환")
    @Test
    void createEmptyOrderTables() {
        List<OrderTable> orderTables = null;
        TableGroup tableGroup = createTableGroupWithOrderTables(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("TableGroup의 orderTable 개수가 2개 미만인 경우 예외 반환")
    @Test
    void createWrongOrderTablesCount() {
        List<OrderTable> orderTables = Arrays.asList(createOrderTableWithEmpty(true));
        TableGroup tableGroup = createTableGroupWithOrderTables(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("TableGroup의 orderTable이 한개라도 DB에 등록되어 있지 않은 경우 예외 반환")
    @Test
    void createUnregisteredOrderTables() {
        List<OrderTable> orderTables = Arrays.asList(
                orderTableDao.save(createOrderTableWithEmpty(true)), createOrderTableWithEmpty(true)
        );
        TableGroup tableGroup = createTableGroupWithOrderTables(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("TableGroup의 orderTable이 비어 있지않은 경우 예외 반환")
    @Test
    void createNotEmptyOrderTables() {
        List<OrderTable> orderTables = Arrays.asList(
                orderTableDao.save(createOrderTableWithEmpty(false)),
                orderTableDao.save(createOrderTableWithEmpty(true))
        );
        TableGroup tableGroup = createTableGroupWithOrderTables(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("TableGroup의 orderTable TableGroupId가 null이 아닌 경우 예외 반환")
    @Test
    void createNonNullTableGroupIdOrderTables() {
        TableGroup savedTableGroup = tableGroupDao.save(createTableGroupWithoutId());
        List<OrderTable> orderTables = Arrays.asList(
                orderTableDao.save(createOrderTableWithTableGroupId(savedTableGroup.getId())),
                orderTableDao.save(createOrderTableWithEmpty(true))
        );
        TableGroup tableGroup = createTableGroupWithOrderTables(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("TableGroup 정상 해제")
    @Test
    void ungroup() {
        TableGroup savedTableGroup = tableGroupDao.save(createTableGroupWithoutId());
        OrderTable savedOrderTable = orderTableDao.save(createOrderTableWithTableGroupId(savedTableGroup.getId()));

        tableGroupService.ungroup(savedTableGroup.getId());

        OrderTable actual = orderTableDao.findById(savedOrderTable.getId()).orElse(null);
        assertThat(actual.getTableGroupId()).isNull();
    }

    @DisplayName("TableGroup 해제 시 해당테이블 중 조리중이거나 식사중인 테이블이 있는 경우 예외 반환")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void ungroupCookingOrMealOrderTable(String orderStatus) {
        TableGroup savedTableGroup = tableGroupDao.save(createTableGroupWithoutId());
        OrderTable savedOrderTable = orderTableDao.save(createOrderTableWithTableGroupId(savedTableGroup.getId()));
        orderDao.save(createOrderWithOrderStatusAndTableId(orderStatus, savedOrderTable.getId()));

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
