package kitchenpos.dao;

import static kitchenpos.fixture.TableGroupFixture.TABLE_GROUP_FIXTURE_1;
import static kitchenpos.fixture.TableGroupFixture.TABLE_GROUP_FIXTURE_2;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TableGroupDaoTest {

    @Autowired
    private TableGroupDao tableGroupDao;

    @Test
    void save() {
        TableGroup tableGroup = TABLE_GROUP_FIXTURE_1;

        TableGroup persistTableGroup = tableGroupDao.save(tableGroup);

        assertThat(persistTableGroup.getId()).isNotNull();
    }

    @Test
    void findById() {
        TableGroup persistTableGroup = tableGroupDao.save(TABLE_GROUP_FIXTURE_1);

        TableGroup findTableGroup = tableGroupDao.findById(persistTableGroup.getId()).get();

        assertThat(findTableGroup).isEqualToComparingFieldByField(persistTableGroup);
    }

    @Test
    void findAll() {
        tableGroupDao.save(TABLE_GROUP_FIXTURE_1);
        tableGroupDao.save(TABLE_GROUP_FIXTURE_2);

        List<TableGroup> tableGroups = tableGroupDao.findAll();
        List<LocalDateTime> createdDates = tableGroups.stream()
            .map(TableGroup::getCreatedDate)
            .collect(Collectors.toList());

        assertThat(createdDates)
            .contains(TABLE_GROUP_FIXTURE_1.getCreatedDate(), TABLE_GROUP_FIXTURE_2.getCreatedDate());
    }
}
