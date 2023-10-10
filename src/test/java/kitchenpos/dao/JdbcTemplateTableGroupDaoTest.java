package kitchenpos.dao;

import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@Import(value = JdbcTemplateTableGroupDao.class)
@JdbcTest
class JdbcTemplateTableGroupDaoTest {

    @Autowired
    private JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao;

    @Test
    void 저장한다() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of());
        tableGroup.setCreatedDate(LocalDateTime.of(2023,03,03,03,03,03));

        // when
        TableGroup result = jdbcTemplateTableGroupDao.save(tableGroup);

        // then
        assertThat(result.getOrderTables()).isNull();
    }

    @Test
    void id값으로_조회한다() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of());
        tableGroup.setCreatedDate(LocalDateTime.of(2023,03,03,03,03,03));

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
        assertThat(result.size()).isEqualTo(0);
    }
}
