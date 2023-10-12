package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuGroupService menuGroupService;


    @Test
    @DisplayName("menu group 생성을 테스트한다.")
    void create() {
        // given
        MenuGroup menuGroup = new MenuGroup("추천메뉴");

        // when
        Long id = menuGroupService.create(menuGroup).getId();

        // then
        String savedName = menuGroupDao.findById(id)
                .orElseThrow(NoSuchElementException::new)
                .getName();
        assertThat(savedName).isEqualTo("추천메뉴");
    }

    @Test
    @DisplayName("menu group 전체조회를 테스트한다.")
    void list() {
        // given
        List<MenuGroup> menuGroups = List.of(
                new MenuGroup("추천메뉴1"),
                new MenuGroup("추천메뉴2"),
                new MenuGroup("추천메뉴3")
        );

        // when
        for (MenuGroup menuGroup : menuGroups) {
            menuGroupService.create(menuGroup);
        }

        // then
        List<MenuGroup> results = menuGroupService.list()
                .stream()
                .filter(menu -> containsMenuGroup(menuGroups, menu))
                .collect(Collectors.toList());
        assertThat(results).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(menuGroups);
    }

    private boolean containsMenuGroup(final List<MenuGroup> menuGroups, final MenuGroup menu) {
        for (MenuGroup menuGroup : menuGroups) {
            if (menuGroup.getName().equals(menu.getName())) {
                return true;
            }
        }

        return false;
    }

}