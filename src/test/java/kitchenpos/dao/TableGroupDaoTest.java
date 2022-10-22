package kitchenpos.dao;

import static kitchenpos.support.TestFixtureFactory.단체_지정을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DaoTest
class TableGroupDaoTest {

    @Autowired
    private TableGroupDao tableGroupDao;

    @Test
    void 단체_지정을_저장_하면_id가_채워진다() {
        TableGroup tableGroup = 단체_지정을_생성한다(LocalDateTime.now(), null);

        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        assertAll(
                () -> assertThat(savedTableGroup.getId()).isNotNull(),
                () -> assertThat(savedTableGroup).usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(tableGroup)
        );
    }

    @Test
    void id로_단체_지정을_조회할_수_있다() {
        TableGroup tableGroup = tableGroupDao.save(단체_지정을_생성한다(LocalDateTime.now(), null));

        TableGroup actual = tableGroupDao.findById(tableGroup.getId())
                .orElseGet(Assertions::fail);

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(tableGroup);
    }

    @Test
    void 없는_id로_단체_지정을_조회하면_Optional_empty를_반환한다() {
        Optional<TableGroup> actual = tableGroupDao.findById(0L);

        assertThat(actual).isEmpty();
    }

    @Test
    void 모든_단체_지정을_조회할_수_있다() {
        TableGroup tableGroup1 = tableGroupDao.save(단체_지정을_생성한다(LocalDateTime.now(), null));
        TableGroup tableGroup2 = tableGroupDao.save(단체_지정을_생성한다(LocalDateTime.now(), null));

        List<TableGroup> actual = tableGroupDao.findAll();

        assertThat(actual).hasSize(2)
                .usingFieldByFieldElementComparator()
                .containsExactly(tableGroup1, tableGroup2);
    }
}