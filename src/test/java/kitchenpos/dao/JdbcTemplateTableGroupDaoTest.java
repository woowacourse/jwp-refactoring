package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.TableGroup;

@DisplayName("JdbcTemplateTableGroupDao 테스트")
@Sql("/dao-test.sql")
@JdbcTest
@Import(JdbcTemplateTableGroupDao.class)
class JdbcTemplateTableGroupDaoTest {
    @Autowired
    private JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao;

    @DisplayName("TableGroupDao save 테스트")
    @Test
    void save() {
        // Given
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());

        // When
        final TableGroup savedTableGroup = jdbcTemplateTableGroupDao.save(tableGroup);

        // Then
        assertAll(
                () -> assertThat(savedTableGroup)
                        .extracting(TableGroup::getId)
                        .isNotNull()
                ,
                () -> assertThat(savedTableGroup)
                        .extracting(TableGroup::getCreatedDate)
                        .isEqualTo(tableGroup.getCreatedDate())
        );
    }

    @DisplayName("TableGroupDao findById 테스트")
    @Test
    void findById() {
        // When
        final TableGroup tableGroup = jdbcTemplateTableGroupDao.findById(1L)
                .orElseThrow(IllegalArgumentException::new);

        // Then
        assertThat(tableGroup)
                .extracting(TableGroup::getCreatedDate)
                .isEqualTo(LocalDateTime.of(2020, 11, 18, 12, 0, 0))
        ;
    }

    @DisplayName("TableGroupDao findById Id가 존재하지 않을 경우")
    @Test
    void findById_NotExists() {
        // When
        final Optional<TableGroup> tableGroup = jdbcTemplateTableGroupDao.findById(4L);

        // Then
        assertThat(tableGroup.isPresent()).isFalse();
    }

    @DisplayName("TableGroupDao findAll 테스트")
    @Test
    void findAll() {
        // When
        final List<TableGroup> tableGroups = jdbcTemplateTableGroupDao.findAll();

        // Then
        assertThat(tableGroups).hasSize(3);
    }
}
