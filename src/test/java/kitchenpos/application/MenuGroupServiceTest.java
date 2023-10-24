package kitchenpos.application;

import kitchenpos.application.dto.request.CreateMenuGroupRequest;
import kitchenpos.application.dto.response.CreateMenuGroupResponse;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.repository.MenuGroupRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static kitchenpos.fixture.MenuGroupFixture.MENU_GROUP;
import static kitchenpos.fixture.MenuGroupFixture.REQUEST;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_생성한다() {
        // given
        CreateMenuGroupRequest request = REQUEST.메뉴_그룹_치킨_생성_요청();
        MenuGroup menuGroup = MENU_GROUP.메뉴_그룹_치킨();
        given(menuGroupRepository.save(any()))
                .willReturn(menuGroup);

        // when & then
        CreateMenuGroupResponse result = menuGroupService.create(request);
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(result.getId()).isEqualTo(menuGroup.getId());
            softAssertions.assertThat(result.getName()).isEqualTo(menuGroup.getName());
        });
    }

    @Test
    void 메뉴_그룹_목록을_조회한다() {
        // given
        MenuGroup menuGroup = MenuGroup.builder()
                .id(1L)
                .name("메뉴 그룹")
                .build();
        given(menuGroupRepository.findAll())
                .willReturn(List.of(menuGroup));

        // when & then
        Assertions.assertThat(menuGroupService.list())
                .usingRecursiveComparison()
                .isEqualTo(List.of(menuGroup));
    }
}
