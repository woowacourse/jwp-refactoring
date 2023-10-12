package kitchenpos.application;

import static kitchenpos.test.fixture.MenuGroupFixture.메뉴_그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.test.ServiceTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_추가한다() {
        MenuGroup menuGroup = 메뉴_그룹("일식");

        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        assertSoftly(softly -> {
            softly.assertThat(savedMenuGroup.getId()).isNotNull();
            softly.assertThat(savedMenuGroup.getName()).isEqualTo(menuGroup.getName());
        });
    }

    @Nested
    class 메뉴_그룹_조회_시 {

        @Test
        void 모든_메뉴_그룹_목록을_조회한다() {
            MenuGroup menuGroupA = 메뉴_그룹("일식");
            MenuGroup menuGroupB = 메뉴_그룹("중식");
            MenuGroup savedMenuGroupA = menuGroupService.create(menuGroupA);
            MenuGroup savedMenuGroupB = menuGroupService.create(menuGroupB);

            List<MenuGroup> menuGroups = menuGroupService.list();

            assertThat(menuGroups).usingRecursiveComparison().isEqualTo(List.of(savedMenuGroupA, savedMenuGroupB));
        }

        @Test
        void 메뉴_그룹이_존재하지_않으면_목록이_비어있다() {
            List<MenuGroup> menuGroups = menuGroupService.list();

            assertThat(menuGroups).isEmpty();
        }
    }
}
