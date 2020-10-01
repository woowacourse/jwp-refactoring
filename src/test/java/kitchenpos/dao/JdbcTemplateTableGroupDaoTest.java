package kitchenpos.dao;

import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("/truncate.sql")
@SpringBootTest
class JdbcTemplateTableGroupDaoTest {
    public static final String DELETE_TABLE_GROUP = "delete from table_group where id in (:ids)";
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao;

    private List<Long> tableGroupIds;

    @BeforeEach
    void setUp() {
        tableGroupIds = new ArrayList<>();
    }

    @DisplayName("상품 저장")
    @Test
    void saveTest() {
        TableGroup tableGroup = createTableGroup(LocalDateTime.now());

        TableGroup savedTableGroup = jdbcTemplateTableGroupDao.save(tableGroup);
        tableGroupIds.add(savedTableGroup.getId());

        assertAll(
                () -> assertThat(savedTableGroup.getId()).isNotNull(),
                () -> assertThat(savedTableGroup.getCreatedDate()).isNotNull()
        );
    }

    @DisplayName("아이디에 맞는 상품 반환")
    @Test
    void findByIdTest() {
        TableGroup tableGroup = createTableGroup(LocalDateTime.now());
        TableGroup savedTableGroup = jdbcTemplateTableGroupDao.save(tableGroup);

        TableGroup findTableGroup = jdbcTemplateTableGroupDao.findById(savedTableGroup.getId()).get();
        tableGroupIds.add(findTableGroup.getId());

        assertAll(
                () -> assertThat(findTableGroup.getId()).isEqualTo(savedTableGroup.getId()),
                () -> assertThat(findTableGroup.getCreatedDate()).isEqualTo(savedTableGroup.getCreatedDate())
        );
    }

    @DisplayName("잘못된 아이디 입력 시 빈 객체 반환")
    @Test
    void findByIdWithInvalidIdTest() {
        Optional<TableGroup> findTableGroup = jdbcTemplateTableGroupDao.findById(0L);

        assertThat(findTableGroup).isEqualTo(Optional.empty());
    }

    @DisplayName("모든 상품 반환")
    @Test
    void findAllTest() {
        TableGroup firstTableGroup = createTableGroup(LocalDateTime.now());
        TableGroup secondTableGroup = createTableGroup(LocalDateTime.now());
        TableGroup thirdTableGroup = createTableGroup(LocalDateTime.now());
        jdbcTemplateTableGroupDao.save(firstTableGroup);
        jdbcTemplateTableGroupDao.save(secondTableGroup);
        jdbcTemplateTableGroupDao.save(thirdTableGroup);

        List<TableGroup> allTableGroups = jdbcTemplateTableGroupDao.findAll();
        allTableGroups.forEach(tableGroup -> tableGroupIds.add(tableGroup.getId()));

        assertThat(allTableGroups).hasSize(3);
    }

    private TableGroup createTableGroup(LocalDateTime createdDate) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(createdDate);
        return tableGroup;
    }

    @AfterEach
    void tearDown() {
        Map<String, Object> params = Collections.singletonMap("ids", tableGroupIds);
        namedParameterJdbcTemplate.update(DELETE_TABLE_GROUP, params);
    }
}