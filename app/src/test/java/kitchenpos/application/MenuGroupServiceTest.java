package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import kitchenpos.menugroup.application.dto.MenuGroupRequest;
import kitchenpos.menugroup.application.dto.MenuGroupResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    void 메뉴_그룹_등록() {
        // given
        MenuGroupRequest request = new MenuGroupRequest("제이슨 추천 메뉴");

        // when
        MenuGroupResponse savedMenuGroup = menuGroupService.create(request);
        Long menuGroupId = savedMenuGroup.getId();

        // then
        assertThat(menuGroupRepository.findById(menuGroupId)).isPresent();
    }

    @Test
    void 메뉴_그룹_목록_조회() {
        // given
        List<MenuGroup> menuGroups = List.of(
                new MenuGroup("레오 추천 메뉴"),
                new MenuGroup("준팍 추천 메뉴")
        );

        Iterable<MenuGroup> savedMenuGroups = menuGroupRepository.saveAll(menuGroups);
        List<MenuGroupResponse> expected = StreamSupport.stream(savedMenuGroups.spliterator(), false)
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList());

        // when
        List<MenuGroupResponse> result = menuGroupService.list();

        // then
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
