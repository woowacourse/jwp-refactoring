package kitchenpos.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Test
    @DisplayName("메뉴 그룹 등록")
    void create() {
        // given
        MenuGroup menuGroup = MenuGroupFixture.create("제이슨 추천 메뉴");

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertThat(menuGroupDao.findById(savedMenuGroup.getId())).isPresent();
    }

    @Test
    @DisplayName("메뉴 그룹 목록 조회")
    void list() {
        // given
        MenuGroup leoGroup = menuGroupDao.save(MenuGroupFixture.create("레오 추천 메뉴") );
        MenuGroup junpakGroup = menuGroupDao.save(MenuGroupFixture.create("준팍 추천 메뉴"));

        // when
        List<MenuGroup> result = menuGroupService.list();

        // then
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(List.of(leoGroup, junpakGroup));
    }
}
