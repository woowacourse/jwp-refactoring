package kitchenpos.application;

import static kitchenpos.application.fixture.MenuGroupFixture.UNSAVED_MENU_GROUP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @DisplayName("메뉴그룹 생성")
    @Test
    void create() {
        MenuGroup savedMenuGroup = menuGroupService.create(UNSAVED_MENU_GROUP);
        Optional<MenuGroup> foundMenuGroup = menuGroupDao.findById(savedMenuGroup.getId());

        assertThat(foundMenuGroup).isPresent();
    }

    @DisplayName("메뉴그룹 생성할때 이름이 없으면 예외가 발생한다.")
    @Test
    void create_Exception() {
        MenuGroup menuGroup = new MenuGroup(null);
        assertThatThrownBy(() -> menuGroupService.create(menuGroup))
                .isInstanceOf(Exception.class);
    }

    @DisplayName("메뉴그룹 조회")
    @Test
    void list() {
        int numberOfSavedMenuGroupBeforeCreate = menuGroupService.list().size();
        menuGroupService.create(UNSAVED_MENU_GROUP);

        int numberOfSavedMenuGroup = menuGroupService.list().size();

        assertThat(numberOfSavedMenuGroupBeforeCreate + 1).isEqualTo(numberOfSavedMenuGroup);
    }
}
