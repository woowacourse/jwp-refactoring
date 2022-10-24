package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
class MenuGroupDaoTest {

    private final MenuGroupDao menuGroupDao;

    @Autowired
    public MenuGroupDaoTest(final DataSource dataSource) {
        this.menuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
    }

    @Test
    @DisplayName("메뉴 그룹을 저장한다")
    void save() {
        // given
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("듀오 치킨 세트");

        // when
        final MenuGroup saved = menuGroupDao.save(menuGroup);

        // then
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getName()).isEqualTo("듀오 치킨 세트")
        );
    }

    @Test
    @DisplayName("id로 메뉴 그룹을 조회한다")
    void findById() {
        // given
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("듀오 치킨 세트");
        final MenuGroup saved = menuGroupDao.save(menuGroup);

        // when
        final MenuGroup foundMenuGroup = menuGroupDao.findById(saved.getId())
                .get();

        // then
        assertThat(foundMenuGroup).usingRecursiveComparison()
                .isEqualTo(saved);
    }

    @Test
    @DisplayName("id로 메뉴 그룹을 조회할 때 결과가 없다면 Optional.empty를 반환한다")
    void findByIdNotExist() {
        // when
        final Optional<MenuGroup> menuGroup = menuGroupDao.findById(-1L);

        // then
        assertThat(menuGroup).isEmpty();
    }

    @Test
    @DisplayName("모든 메뉴 그룹을 조회한다")
    void findAll() {
        // given
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("듀오 치킨 세트");
        final MenuGroup saved = menuGroupDao.save(menuGroup);

        // when
        final List<MenuGroup> menuGroups = menuGroupDao.findAll();

        // then
        assertAll(
                () -> assertThat(menuGroups).hasSizeGreaterThanOrEqualTo(1),
                () -> assertThat(menuGroups).extracting("id")
                        .contains(saved.getId())
        );
    }

    @Test
    @DisplayName("id로 해당 메뉴 그룹이 존재하는지 확인한다")
    void existsById() {
        // given
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("듀오 치킨 세트");
        final MenuGroup saved = menuGroupDao.save(menuGroup);

        // when
        final boolean exists = menuGroupDao.existsById(saved.getId());
        final boolean notExists = menuGroupDao.existsById(-1L);

        // then
        assertAll(
                () -> assertThat(exists).isTrue(),
                () -> assertThat(notExists).isFalse()
        );
    }
}

