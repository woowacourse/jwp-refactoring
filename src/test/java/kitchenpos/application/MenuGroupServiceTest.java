package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Test
    void 메뉴_그룹_정상_생성() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("양식");

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        // then
        boolean actual = menuGroupDao.existsById(savedMenuGroup.getId());
        assertThat(actual).isTrue();
    }

    @Test
    void 메뉴_그룹_이름을_null_값으로_생성() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(null);

        // when & then
        assertThatThrownBy(() -> menuGroupService.create(menuGroup))
                .isInstanceOf(Exception.class);
    }

    @Test
    void 메뉴_그룹_목록_조회() {
        // given
        int count = 3;
        List<MenuGroup> menuGroups = saveMenuGroups(count);

        // when
        List<MenuGroup> actual = menuGroupService.list();

        // then
        assertThat(actual.size()).isEqualTo(count);
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(menuGroups);
    }

    private List<MenuGroup> saveMenuGroups(final int count) {
        List<MenuGroup> menuGroups = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            MenuGroup menuGroup = new MenuGroup();
            menuGroup.setName("메뉴 그룹명 " + i);
            menuGroups.add(menuGroupDao.save(menuGroup));
        }
        return menuGroups;
    }
}
