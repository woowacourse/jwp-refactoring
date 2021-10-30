package kitchenpos.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.application.MenuGroupService;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.service.fixture.MenuGroupFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 그룹 기능에서")
public class MenuGroupServiceTest {

    private MenuGroupFixture menuGroupFixture;
    private MenuGroupDao menuGroupDao;
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        menuGroupFixture = MenuGroupFixture.createFixture();
        menuGroupDao = menuGroupFixture.getTestMenuGroupDao();
        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    @Test
    void createMenuGroupTest() {
        //given
        MenuGroup menuGroup = new MenuGroup();
        String newMenuGroupName = "새로운 메뉴그룹";
        menuGroup.setName(newMenuGroupName);

        //when
        MenuGroup persistedMenuGroup = menuGroupService.create(menuGroup);

        //then
        assertThat(persistedMenuGroup.getName()).isEqualTo(newMenuGroupName);
    }

    @DisplayName("메뉴 그룹 목록을 받아올 수 있다.")
    @Test
    void menuGroupListTest() {
        //given
        List<MenuGroup> expectedFixtures = menuGroupFixture.getFixtures();

        //when & then
        List<MenuGroup> list = menuGroupService.list();

        assertThat(expectedFixtures.size()).isEqualTo(list.size());
        expectedFixtures.forEach(
            menu -> assertThat(list).usingRecursiveFieldByFieldElementComparator()
                .contains(menu)
        );
    }
}
