package kitchenpos.application;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.dto.request.menu.CreateMenuGroupRequest;
import kitchenpos.dto.response.menu.MenuGroupResponse;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static kitchenpos.fixture.MenuGroupFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("MenuGroupService 단위 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹 이름을 통해 메뉴 그룹을 생성할 수 있다.")
    void create() {
        // given
        CreateMenuGroupRequest 추천메뉴 = new CreateMenuGroupRequest("추천 메뉴");
        MenuGroup expected = new MenuGroup(1L, "추천 메뉴");
        given(menuGroupRepository.save(any(MenuGroup.class))).willReturn(expected);

        // when
        MenuGroupResponse actual = menuGroupService.create(추천메뉴);

        // then
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    @DisplayName("저장되어 있는 모든 메뉴 그룹을 조회할 수 있다.")
    void list() {
        // given
        List<MenuGroup> expected = Arrays.asList(추천메뉴, 신메뉴, 할인메뉴);
        given(menuGroupRepository.findAll()).willReturn(expected);

        // when
        List<MenuGroupResponse> actual = menuGroupService.list();

        // then
        assertEquals(3, actual.size());
        assertThat(actual.get(0).getName()).isEqualTo(추천메뉴.getName());
        assertThat(actual.get(1).getName()).isEqualTo(신메뉴.getName());
        assertThat(actual.get(2).getName()).isEqualTo(할인메뉴.getName());
    }
}
