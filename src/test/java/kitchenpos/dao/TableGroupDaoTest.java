package kitchenpos.dao;

import static kitchenpos.utils.TestObjectUtils.NOT_EXIST_VALUE;
import static kitchenpos.utils.TestObjectUtils.createOrderTable;
import static kitchenpos.utils.TestObjectUtils.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TableGroupDaoTest extends DaoTest {

    @Autowired
    private TableGroupDao tableGroupDao;

    private List<OrderTable> orderTables;

    @BeforeEach
    void setUp() {
        orderTables = Arrays.asList(
            createOrderTable(0, true),
            createOrderTable(0, true)
        );
    }

    @DisplayName("테이블 그룹 save - 성공")
    @Test
    void save() {
        TableGroup tableGroup = createTableGroup(orderTables);
        tableGroup.setCreatedDate(LocalDateTime.now());
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        assertAll(() -> {
            assertThat(savedTableGroup.getId()).isNotNull();
        });
    }

    @DisplayName("테이블 그룹 findById - 성공")
    @Test
    void findById() {
        TableGroup tableGroup = createTableGroup(orderTables);
        tableGroup.setCreatedDate(LocalDateTime.now());
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
        Optional<TableGroup> foundTableGroup = tableGroupDao.findById(savedTableGroup.getId());

        assertThat(foundTableGroup.isPresent()).isTrue();
    }

    @DisplayName("테이블 그룹 findById - 예외, 빈 데이터에 접근하려고 하는 경우")
    @Test
    void findById_EmptyResultDataAccess_ThrownException() {
        Optional<TableGroup> foundTableGroup = tableGroupDao.findById(NOT_EXIST_VALUE);

        assertThat(foundTableGroup.isPresent()).isFalse();
    }

    @DisplayName("테이블 그룹 findAll - 성공")
    @Test
    void findAll() {
        TableGroup tableGroup = createTableGroup(orderTables);
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroupDao.save(tableGroup);

        List<TableGroup> tableGroups = tableGroupDao.findAll();

        assertThat(tableGroups).hasSize(1);
    }
}
