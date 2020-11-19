package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import kitchenpos.domain.MenuGroup;

@DisplayName("JdbcTemplateMenuGroupDao 테스트")
@JdbcTest
@Import(JdbcTemplateMenuGroupDao.class)
class JdbcTemplateMenuGroupDaoTest {
    @Autowired
    private JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao;

    @DisplayName("MenuGroupDao save 테스트")
    @Test
    void save() {
        // Given
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("세마리메뉴");

        // When
        final MenuGroup savedMenuGroup = jdbcTemplateMenuGroupDao.save(menuGroup);

        // Then
        assertAll(
                () -> assertThat(savedMenuGroup)
                        .extracting(MenuGroup::getId)
                        .isNotNull()
                ,
                () -> assertThat(savedMenuGroup)
                        .extracting(MenuGroup::getName)
                        .isEqualTo(menuGroup.getName())
        );
    }

    @DisplayName("MenuGroupDao findById 테스트")
    @Test
    void findById() {
        // When
        final MenuGroup menuGroup = jdbcTemplateMenuGroupDao.findById(1L)
                .orElseThrow(IllegalArgumentException::new);

        // Then
        assertThat(menuGroup)
                .extracting(MenuGroup::getName)
                .isEqualTo("두마리메뉴")
        ;
    }

    @DisplayName("MenuGroupDao findAll 테스트")
    @Test
    void findAll() {
        // When
        final List<MenuGroup> menuGroups = jdbcTemplateMenuGroupDao.findAll();

        // Then
        assertThat(menuGroups).hasSize(4);
    }

    @DisplayName("MenuGroupDao existsById 테스트")
    @ParameterizedTest
    @CsvSource(value = {"1,true", "10,false"})
    void existsById(final long id, final boolean exists) {
        // When
        final boolean existsMenuGroup = jdbcTemplateMenuGroupDao.existsById(id);

        // Then
        assertThat(existsMenuGroup).isEqualTo(exists);
    }
}
