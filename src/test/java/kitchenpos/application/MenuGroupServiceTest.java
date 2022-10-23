package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFixture.분식;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.fixture.MenuGroupFixture;
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
        MenuGroup menuGroup = 분식.toEntity();

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
        List<MenuGroup> menuGroups = 메뉴_그룹_목록_생성();

        // when
        List<MenuGroup> actual = menuGroupService.list();

        // then
        assertThat(actual.size()).isEqualTo(menuGroups.size());
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(menuGroups);
    }

    private List<MenuGroup> 메뉴_그룹_목록_생성() {
        return Arrays.stream(MenuGroupFixture.values())
                .map(it -> menuGroupDao.save(it.toEntity()))
                .collect(Collectors.toList());
    }
}
