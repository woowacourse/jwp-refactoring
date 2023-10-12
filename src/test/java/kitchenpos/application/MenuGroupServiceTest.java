package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest implements ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Test
    void 메뉴_그룹을_생성_할_수_있다() {
        // given
        final MenuGroup menuGroup = 메뉴_그룹("메뉴_그룹");

        // when
        final MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(menuGroup.getId()).isNull();
            softly.assertThat(savedMenuGroup.getId()).isNotNull();
        });
    }

    @Test
    void 메뉴_그룹을_조회할_수_있다() {
        // given
        final MenuGroup savedMenuGroup = menuGroupDao.save(메뉴_그룹("메뉴_그룹"));

        // when
        List<MenuGroup> list = menuGroupService.list();

        // then
        assertThat(list).usingRecursiveFieldByFieldElementComparator().contains(savedMenuGroup);
    }
}
