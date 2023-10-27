package kitchenpos.menugroup.applicatioin;

import kitchenpos.config.ServiceTestConfig;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.menugroup.ui.dto.MenuGroupRequest;
import kitchenpos.menugroup.ui.dto.MenuGroupResponse;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


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
        final MenuGroupRequest menuGroupRequest = MenuGroupFixture.메뉴_그룹_요청_dto_생성();

        // when
        final MenuGroupResponse actual = menuGroupService.create(menuGroupRequest);

        // then
        assertThat(actual.getId()).isPositive();
    }

    @Test
    void 메뉴_목록을_조회한다() {
        // given
        final List<MenuGroup> menuGroups = menuGroupRepository.saveAll(MenuGroupFixture.메뉴_그룹_엔티티들_생성(3));

        // when
        final List<MenuGroupResponse> actual = menuGroupService.list();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(3);
            softAssertions.assertThat(actual.get(0).getId()).isEqualTo(menuGroups.get(0).getId());
            softAssertions.assertThat(actual.get(1).getId()).isEqualTo(menuGroups.get(1).getId());
            softAssertions.assertThat(actual.get(2).getId()).isEqualTo(menuGroups.get(2).getId());
        });
    }
}
