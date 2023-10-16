package kitchenpos.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Test
    void 메뉴_그룹_등록() {
        // given
        MenuGroup menuGroup = MenuGroupFixture.create("제이슨 추천 메뉴");

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertThat(menuGroupDao.findById(savedMenuGroup.getId())).isPresent();
    }

    @Test
    void 메뉴_그룹_목록_조회() {
        // given
        MenuGroup leoGroup = menuGroupDao.save(MenuGroupFixture.create("레오 추천 메뉴"));
        MenuGroup junpakGroup = menuGroupDao.save(MenuGroupFixture.create("준팍 추천 메뉴"));

        // when
        List<MenuGroup> result = menuGroupService.list();

        // then
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(List.of(leoGroup, junpakGroup));
    }
}
