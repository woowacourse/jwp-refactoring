package kitchenpos.dao;

import kitchenpos.domain.TableGroup;
import kitchenpos.helper.JdbcTestHelper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JdbcTemplateTableGroupDaoTest extends JdbcTestHelper {

    @Autowired
    private JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao;

    @Test
    void 저장한다() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of());
        tableGroup.setCreatedDate(LocalDateTime.of(2023, 03, 03, 03, 03, 03));

        // when
        TableGroup result = jdbcTemplateTableGroupDao.save(tableGroup);

        // then
        assertThat(result.getOrderTables()).isNull();
    }

    @Test
    void id값으로_조회한다() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of());
        tableGroup.setCreatedDate(LocalDateTime.of(2023, 03, 03, 03, 03, 03));

        TableGroup saved = jdbcTemplateTableGroupDao.save(tableGroup);

        // when
        Optional<TableGroup> result = jdbcTemplateTableGroupDao.findById(saved.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(result).isPresent();
            softly.assertThat(result.get().getId()).isEqualTo(saved.getId());
        });
    }

    @Test
    void 모두_조회한다() {
        // when
        List<TableGroup> result = jdbcTemplateTableGroupDao.findAll();

        // then
        assertThat(result.size()).isZero();
    }
}
