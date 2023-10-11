package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴그룹 등록시 등록된 메뉴 그룹 정보가 반환된다.")
    void createMenuGroup() {
        // given
        final MenuGroup requestMenuGroup = new MenuGroup();
        final String testGroupName = "테스트 그룹";
        requestMenuGroup.setName(testGroupName);

        // when
        final MenuGroup actual = menuGroupService.create(requestMenuGroup);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(actual.getId()).isNotNull();
            softly.assertThat(actual.getName()).isEqualTo(testGroupName);
        });
    }

    @Test
    @DisplayName("등록된 메뉴그룹들의 리스트를 조회한다.")
    void test() {
        // given
        final MenuGroup requestMenuGroup = new MenuGroup();
        final String testGroupName = "조회 테스트 그룹";
        requestMenuGroup.setName(testGroupName);
        menuGroupService.create(requestMenuGroup);

        // when
        final List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(menuGroups).isNotEmpty();
            softly.assertThat(menuGroups.get(menuGroups.size() - 1).getName()).isEqualTo(testGroupName);
        });
    }
}
