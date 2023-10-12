package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFixture.추천_메뉴_그룹;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuGroupFixture;
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
    void 메뉴_그룹을_생성한다() {
        // given
        MenuGroup menuGroup = 추천_메뉴_그룹();

        // when
        Long id = menuGroupService.create(menuGroup).getId();

        // then
        String savedName = menuGroupDao.findById(id)
                .orElseThrow(NoSuchElementException::new)
                .getName();
        assertThat(savedName).isEqualTo(menuGroup.getName());
    }

    @Test
    void 전체_메뉴_그룹을_조회한다() {
        // given
        List<MenuGroup> menuGroups = List.of(
                MenuGroupFixture.추천_메뉴_그룹(),
                MenuGroupFixture.떠오르는_메뉴_그룹(),
                MenuGroupFixture.싼_메뉴_그룹()
        );
        List<MenuGroup> savedMenuGroups = new ArrayList<>();
        for (MenuGroup menuGroup : menuGroups) {
            savedMenuGroups.add(menuGroupDao.save(menuGroup));
        }

        // then
        List<MenuGroup> results = menuGroupService.list()
                .stream()
                .filter(menu -> containsMenuGroups(savedMenuGroups, menu))
                .collect(Collectors.toList());
        assertThat(results).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(menuGroups);
    }

    private boolean containsMenuGroups(List<MenuGroup> menuGroups, MenuGroup menu) {
        for (MenuGroup menuGroup : menuGroups) {
            if (menuGroup.getId().equals(menu.getId())) {
                return true;
            }
        }

        return false;
    }

}