package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import javax.sql.DataSource;
import kitchenpos.domain.MenuGroup;
import kitchenpos.fixture.MenuGroupFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
class JdbcTemplateMenuGroupDaoTest {

    @Autowired
    private DataSource dataSource;
    private JdbcTemplateMenuGroupDao menuGroupDao;

    @BeforeEach
    void setUp() {
        menuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
    }

    @Test
    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    void save() {
        // given
        final MenuGroup menuGroup = MenuGroupFixture.createDefaultWithoutId();

        // when
        final MenuGroup saved = menuGroupDao.save(menuGroup);

        // then
        assertAll(

                () -> assertThat(saved).extracting("id").isNotNull(),
                () -> assertThat(saved).usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(menuGroup)
        );
    }

    @Test
    @DisplayName("등록된 메뉴 그룹을 조회할 수 있다.")
    void findById() {
        // given
        final MenuGroup menuGroup = MenuGroupFixture.createDefaultWithoutId();
        final MenuGroup saved = menuGroupDao.save(menuGroup);

        // when
        final MenuGroup actual = menuGroupDao.findById(saved.getId())
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(saved);
    }

    @Test
    @DisplayName("등록된 메뉴 그룹을 모두 조회할 수 있다.")
    void findAll() {
        // given
        final MenuGroup menuGroup1 = MenuGroupFixture.createDefaultWithoutId();
        final MenuGroup menuGroup2 = MenuGroupFixture.createDefaultWithoutId();
        final MenuGroup saved1 = menuGroupDao.save(menuGroup1);
        final MenuGroup saved2 = menuGroupDao.save(menuGroup2);

        // when
        final List<MenuGroup> actual = menuGroupDao.findAll();

        // then
        assertAll(
                () -> assertThat(actual).usingRecursiveFieldByFieldElementComparator()
                .extracting("id")
                .containsSequence(saved1.getId(), saved2.getId()),
                () -> assertThat(actual).usingElementComparatorIgnoringFields("id")
                        .containsSequence(saved1, saved2)
        );
    }

    @Test
    @DisplayName("특정 메뉴 그룹이 등록되어 있는지 확인할 수 있다.")
    void existsById() {
        // given
        final MenuGroup menuGroup = MenuGroupFixture.createDefaultWithoutId();
        final MenuGroup saved = menuGroupDao.save(menuGroup);

        // when
        final boolean savedActual = menuGroupDao.existsById(saved.getId());
        final boolean notSavedActual = menuGroupDao.existsById(Long.MAX_VALUE);

        // then
        assertAll(
                () -> assertThat(savedActual).isTrue(),
                () -> assertThat(notSavedActual).isFalse()
        );
    }
}
