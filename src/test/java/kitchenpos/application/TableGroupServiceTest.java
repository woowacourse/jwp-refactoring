package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.dao.JdbcTemplateOrderDao;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
import kitchenpos.dao.JdbcTemplateTableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableFixture;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupFixture;


class TableGroupServiceTest extends AbstractServiceTest{

    @Autowired
    private DataSource dataSource;

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

    private void createOrderTableCountBy(int count) {
        for (int i = 0; i < count; i++) {
            jdbcTemplateOrderTableDao.save(OrderTableFixture.createBeforeSave());
        }
    }
}