package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFixture.떠오르는_메뉴_그룹;
import static kitchenpos.fixture.MenuGroupFixture.싼_메뉴_그룹;
import static kitchenpos.fixture.MenuGroupFixture.추천_메뉴_그룹;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.request.MenuGroupCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest extends ServiceIntegrationTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_생성한다() {
        // given
        MenuGroupCreateRequest request = new MenuGroupCreateRequest(
                추천_메뉴_그룹().getName()
        );

        // when
        Long id = menuGroupService.create(request)
                .getId();

        // then
        String actual = menuGroupRepository.findById(id)
                .get()
                .getName();
        assertThat(actual).isEqualTo(request.getName());
    }

    @Test
    void 전체_메뉴_그룹을_조회한다() {
        // given
        List<MenuGroup> menuGroups = List.of(
                추천_메뉴_그룹(),
                떠오르는_메뉴_그룹(),
                싼_메뉴_그룹()
        );
        List<MenuGroup> expected = new ArrayList<>();
        for (MenuGroup menuGroup : menuGroups) {
            expected.add(menuGroupRepository.save(menuGroup));
        }

        // when
        List<MenuGroup> actual = menuGroupService.list();

        // then
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

}