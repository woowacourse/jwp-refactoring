package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.application.fixture.OrderFixture;
import kitchenpos.application.fixture.OrderTableFixture;
import kitchenpos.application.fixture.TableGroupFixture;
import kitchenpos.dao.JdbcTemplateOrderDao;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
import kitchenpos.dao.JdbcTemplateTableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

class TableGroupServiceTest extends AbstractServiceTest {
    private TableGroupService tableGroupService;
    private JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao;
    private JdbcTemplateOrderDao jdbcTemplateOrderDao;
    private JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao;

    @BeforeEach
    void setUp() {
        jdbcTemplateOrderTableDao = new JdbcTemplateOrderTableDao(dataSource);
        jdbcTemplateOrderDao = new JdbcTemplateOrderDao(dataSource);
        jdbcTemplateTableGroupDao = new JdbcTemplateTableGroupDao(dataSource);

        tableGroupService = new TableGroupService(
            jdbcTemplateOrderDao,
            jdbcTemplateOrderTableDao,
            jdbcTemplateTableGroupDao);
    }

    @DisplayName("order table이 비어있는 경우 예외를 반환한다.")
    @Test
    void createEmptyOrderTable() {
        TableGroup emptyOrderTable = TableGroupFixture.createEmptyOrderTable();

        assertThatThrownBy(() -> tableGroupService.create(emptyOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("order table의 사이즈가 2 미만인 경우 예외를 반환한다.")
    @Test
    void createSize1() {
        TableGroup emptyOrderTable = TableGroupFixture.createTableGroupWithOrderTableSize(1);

        assertThatThrownBy(() -> tableGroupService.create(emptyOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("DB에서 조회해온 OrderTable의 사이즈와 파라미터로 온 OrderTable의 사이즈가 다른 경우 예외를 반환한다.")
    @Test
    void parameterSizeIsDifferentWithDB() {
        TableGroup paramTableGroup = TableGroupFixture.createTableGroupWithOrderTableSize(3);

        assertThatThrownBy(() -> tableGroupService.create(paramTableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비어있지 않은 테이블을 그룹화하는 경우 예외를 반환한다.")
    @Test
    void isNotEmptyOrderTable() {
        TableGroup paramTableGroup = TableGroupFixture.createTableGroupWithNotEmptyOrderTableSize(3);
        jdbcTemplateTableGroupDao.save(paramTableGroup);
        jdbcTemplateOrderTableDao.save(OrderTableFixture.createGroupedOrderTable(1L));
        createOrderTableCountBy(2);

        assertThatThrownBy(() -> tableGroupService.create(paramTableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 그룹화 되어있는 테이블이 있는 경우 예외를 반환한다.")
    @Test
    void isAlreadyGroupBy() {
        TableGroup paramTableGroup = TableGroupFixture.createTableGroupWithNotEmptyOrderTableSize(3);
        TableGroup savedGroup = jdbcTemplateTableGroupDao.save(paramTableGroup);
        jdbcTemplateOrderTableDao.save(OrderTableFixture.createGroupedOrderTable(savedGroup.getId()));
        createOrderTableCountBy(2);

        assertThatThrownBy(() -> tableGroupService.create(paramTableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderTables와, TableGroup이 정상적으로 저장된다.")
    @Test
    void create() {
        TableGroup tableGroup = TableGroupFixture.createTableGroupWithOrderTableSize(3);
        createOrderTableCountBy(3);
        TableGroup savedGroup = tableGroupService.create(tableGroup);

        assertAll(
            () -> assertThat(savedGroup.getId()).isEqualTo(1L),
            () -> assertThat(savedGroup.getCreatedDate()).isBeforeOrEqualTo(LocalDateTime.now()),
            () -> assertThat(savedGroup.getOrderTables())
                .extracting(OrderTable::getId)
                .isEqualTo(Arrays.asList(1L, 2L, 3L)),
            () -> assertThat(savedGroup.getOrderTables())
                .extracting(OrderTable::getTableGroupId)
                .isEqualTo(Arrays.asList(1L, 1L, 1L))
        );
    }

    @DisplayName("조리중인이거나, 식사중인 테이블은 ungroup할 수 없다.")
    @Test
    void isAlreadyCookingOrMeal() {
        List<OrderTable> orderTables = createOrderTableCountBy(3);
        TableGroup tableGroup = tableGroupService.create(
            TableGroupFixture.createTableGroupWithOrderTableSize(orderTables));
        createOrderCountBy(3, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL, OrderStatus.COOKING));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("ungroup을 성공적으로 수행한다.")
    @Test
    void ungroup() {
        List<OrderTable> orderTables = createOrderTableCountBy(3);
        TableGroup tableGroup = tableGroupService.create(
            TableGroupFixture.createTableGroupWithOrderTableSize(orderTables));
        createOrderCountBy(3, Arrays.asList(OrderStatus.COMPLETION, OrderStatus.COMPLETION, OrderStatus.COMPLETION));
        tableGroupService.ungroup(tableGroup.getId());

        assertThat(jdbcTemplateOrderTableDao.findAllByTableGroupId(tableGroup.getId())).hasSize(0);
    }

    private List<OrderTable> createOrderTableCountBy(int count) {
        return IntStream.range(0, count)
            .mapToObj(i -> jdbcTemplateOrderTableDao.save(OrderTableFixture.createBeforeSave()))
            .collect(Collectors.toList());
    }

    private List<Order> createOrderCountBy(int count, List<OrderStatus> status) {
        return LongStream.range(1, count + 1)
            .mapToObj(i -> jdbcTemplateOrderDao.save(OrderFixture.createWithStatus(i, status.get((int)(i - 1)))))
            .collect(Collectors.toList());
    }
}