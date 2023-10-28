package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.request.MenuGroupCreateRequest;
import kitchenpos.menugroup.dto.response.MenuGroupResponse;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹_생성() {
        // given
        MenuGroupCreateRequest request = new MenuGroupCreateRequest("menuGroup");
        MenuGroup savedMenuGroup = new MenuGroup(1L, "menuGroup");
        given(menuGroupRepository.save(any()))
            .willReturn(savedMenuGroup);

        // when
        MenuGroupResponse response = menuGroupService.create(request);

        // then
        assertSoftly(softly -> {
            assertThat(response.getMenuId()).isEqualTo(savedMenuGroup.getId());
            assertThat(response.getMenuName()).isEqualTo(savedMenuGroup.getName());
            verify(menuGroupRepository, times(1)).save(any());
        });
    }

    @Test
    void 메뉴_그룹_목록_조회() {
        // given
        List<MenuGroup> menuGroups = List.of(new MenuGroup(1L, "menuGroup1"),
            new MenuGroup(2L, "menuGroup2"));
        given(menuGroupRepository.findAll())
            .willReturn(menuGroups);

        // when
        List<MenuGroupResponse> acutal = menuGroupService.list();

        // then
        assertSoftly(softly -> {
            assertThat(acutal).hasSize(2);
            verify(menuGroupRepository, times(1)).findAll();
        });
    }

}
