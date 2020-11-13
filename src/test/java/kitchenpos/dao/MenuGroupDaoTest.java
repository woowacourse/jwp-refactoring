package kitchenpos.dao;

import static kitchenpos.fixture.MenuGroupFixture.createMenuGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DaoTest
public class MenuGroupDaoTest {
    @Autowired
    private MenuGroupDao menuGroupDao;

    @DisplayName("메뉴 그룹을 저장할 수 있다.")
    @Test
    void save() {
        MenuGroup menuGroup = createMenuGroup(null, "2+1메뉴");

        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        assertAll(
            () -> assertThat(savedMenuGroup).isNotNull(),
            () -> assertThat(savedMenuGroup).isEqualToIgnoringGivenFields(menuGroup, "id")
        );
    }

    @DisplayName("메뉴 그룹 아이디로 메뉴 그룹을 조회할 수 있다.")
    @Test
    void findById() {
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup(null, "2+1메뉴"));

        Optional<MenuGroup> foundMenuGroup = menuGroupDao.findById(menuGroup.getId());

        assertThat(foundMenuGroup.get()).isEqualToComparingFieldByField(menuGroup);
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
    @Test
    void findAll() {
        List<MenuGroup> savedMenuGroups = Arrays.asList(
            menuGroupDao.save(createMenuGroup(null, "메뉴그룹1")),
            menuGroupDao.save(createMenuGroup(null, "메뉴그룹2")),
            menuGroupDao.save(createMenuGroup(null, "메뉴그룹3"))
        );

        List<MenuGroup> allMenuGroups = menuGroupDao.findAll();

        assertThat(allMenuGroups).usingFieldByFieldElementComparator().containsAll(savedMenuGroups);
    }

    @DisplayName("메뉴 그룹 아이디로 메뉴 그룹 존재 여부를 확인할 수 있다.")
    @Test
    void existsById() {
        Long menuGroupId = menuGroupDao.save(createMenuGroup(null, "메뉴그룹1")).getId();

        assertAll(
            () -> assertThat(menuGroupDao.existsById(menuGroupId)).isTrue(),
            () -> assertThat(menuGroupDao.existsById(menuGroupId + 1)).isFalse()
        );
    }
}
