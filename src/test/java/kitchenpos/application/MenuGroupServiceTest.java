package kitchenpos.application;

import kitchenpos.common.ServiceTestConfig;
import kitchenpos.domain.MenuGroup;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.repository.MenuGroupRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("메뉴 그룹 서비스 테스트")
class MenuGroupServiceTest extends ServiceTestConfig {

    @Autowired
    MenuGroupService menuGroupService;

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Test
    void 메뉴를_등록한다() {
        // given
        final MenuGroup menuGroup = MenuGroupFixture.메뉴_그룹_생성();

        // when
        final MenuGroup actual = menuGroupService.create(menuGroup);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isPositive();
            softAssertions.assertThat(actual.getName()).isEqualTo(menuGroup.getName());
        });
    }

    @Test
    void 메뉴_목록을_조회한다() {
        // given
        final List<MenuGroup> menuGroups = menuGroupRepository.saveAll(MenuGroupFixture.메뉴_그룹들_생성(3));

        // when
        final List<MenuGroup> actual = menuGroupService.list();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(3);
            softAssertions.assertThat(actual.get(0)).usingRecursiveComparison().ignoringFields("id")
                          .isEqualTo(menuGroups.get(0));
            softAssertions.assertThat(actual.get(1)).usingRecursiveComparison().ignoringFields("id")
                          .isEqualTo(menuGroups.get(1));
            softAssertions.assertThat(actual.get(2)).usingRecursiveComparison().ignoringFields("id")
                          .isEqualTo(menuGroups.get(2));
        });
    }
}
