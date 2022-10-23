package kitchenpos.dao;

import static kitchenpos.support.TableGroupFixture.TABLE_GROUP_NOW;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;

class TableGroupDaoTest extends JdbcDaoTest {

    @Test
    void 테이블그룹을_저장한다() {
        // given
        final TableGroup tableGroup = TABLE_GROUP_NOW.생성();

        // when
        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        // then
        assertThat(savedTableGroup.getId()).isNotNull();
    }

    @Test
    void 테이블그룹을_아이디로_조회한다() {
        // given
        final TableGroup savedTableGroup = 테이블그룹을_저장한다(new TableGroup(LocalDateTime.of(2022, 10, 23, 10, 10)));

        // when
        final TableGroup foundTableGroup = tableGroupDao.findById(savedTableGroup.getId())
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(foundTableGroup.getCreatedDate())
                .isEqualTo(LocalDateTime.of(2022, 10, 23, 10, 10));
    }

    @Test
    void 모든_테이블그룹을_조회한다() {
        // given
        final int alreadyExistCount = tableGroupDao.findAll()
                .size();
        final TableGroup savedTableGroup = 테이블그룹을_저장한다(TABLE_GROUP_NOW.생성());

        // when
        final List<TableGroup> tableGroups = tableGroupDao.findAll();

        // then
        assertThat(tableGroups).usingFieldByFieldElementComparator()
                .hasSize(alreadyExistCount + 1)
                .contains(savedTableGroup);
    }
}
