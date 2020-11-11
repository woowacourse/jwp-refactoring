package kitchenpos.dao;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static kitchenpos.fixture.MenuGroupFixture.createMenuGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DaoTest
class JdbcTemplateMenuGroupDaoTest {
    @Autowired
    private MenuGroupDao menuGroupDao;

    @DisplayName("메뉴 그룹 엔티티를 저장하면 id가 부여되며 저장된다")
    @Test
    void insert() {
        MenuGroup menuGroup = createMenuGroup(null, "추천메뉴");

        MenuGroup result = menuGroupDao.save(menuGroup);

        assertAll(
                () -> assertThat(result).isEqualToIgnoringGivenFields(menuGroup, "id"),
                () -> assertThat(result.getId()).isNotNull()
        );
    }

    @Test
    @DisplayName("존재하는 id로 엔티티를 조회하면 저장되어있는 엔티티가 조회된다")
    void findExist() {
        MenuGroup menuGroup = createMenuGroup(null, "추천메뉴");
        MenuGroup persisted = menuGroupDao.save(menuGroup);

        MenuGroup result = menuGroupDao.findById(persisted.getId()).get();

        assertThat(result).isEqualToComparingFieldByField(persisted);
    }

    @Test
    @DisplayName("저장되어있지 않은 엔티티를 조회하면 빈 optional 객체가 반환된다")
    void findNotExist() {
        assertThat(menuGroupDao.findById(0L)).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("모든 엔티티를 조회하면 저장되어 있는 엔티티들이 반환된다")
    void findAll() {
        menuGroupDao.save(createMenuGroup(null, "추천메뉴"));
        menuGroupDao.save(createMenuGroup(null, "무슨메뉴"));
        menuGroupDao.save(createMenuGroup(null, "어떤메뉴"));

        assertThat(menuGroupDao.findAll()).hasSize(3);
    }

    @Test
    @DisplayName("id로 엔티티의 존재 여부를 확인한다")
    void countByIdIn() {
        MenuGroup persisted = menuGroupDao.save(createMenuGroup(null, "추천메뉴"));

        assertAll(
                () -> assertThat(menuGroupDao.existsById(persisted.getId())).isTrue(),
                () -> assertThat(menuGroupDao.existsById(persisted.getId() + 1)).isFalse()
        );
    }
}