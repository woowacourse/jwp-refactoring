package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFixture.createMenuGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MenuGroupServiceTest extends AbstractServiceTest {
    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    @Test
    void create() {
        MenuGroup menuGroup = createMenuGroupRequest("메뉴그룹1");

        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        assertAll(
            () -> assertThat(savedMenuGroup.getId()).isNotNull(),
            () -> assertThat(savedMenuGroup.getName()).isEqualTo(menuGroup.getName())
        );
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
    @Test
    void list() {
        List<MenuGroup> savedMenuGroups = Arrays.asList(
            menuGroupDao.save(createMenuGroupRequest("메뉴그룹1")),
            menuGroupDao.save(createMenuGroupRequest("메뉴그룹2")),
            menuGroupDao.save(createMenuGroupRequest("메뉴그룹3"))
        );

        List<MenuGroup> allMenuGroups = menuGroupService.list();

        assertThat(allMenuGroups).usingFieldByFieldElementComparator().containsAll(savedMenuGroups);
    }
}
