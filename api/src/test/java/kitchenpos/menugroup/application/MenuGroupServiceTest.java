package kitchenpos.menugroup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.application.dto.MenuGroupRequest;
import kitchenpos.menugroup.application.dto.MenuGroupResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Test
    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    void create_success() {
        MenuGroup menuGroup = MenuGroup.from("kong");
        when(menuGroupRepository.save(any())).thenReturn(menuGroup);

        assertThat(menuGroupService.create(new MenuGroupRequest("kong")).getName()).isEqualTo("kong");
    }

    @Test
    @DisplayName("현재 저장된 메뉴 그룹 목록을 확인할 수 있다.")
    void list_success() {
        MenuGroup menuGroup1 = MenuGroup.from("kong");
        MenuGroup menuGroup2 = MenuGroup.from("wuga");
        when(menuGroupRepository.findAll()).thenReturn(List.of(menuGroup1, menuGroup2));

        assertThat(menuGroupService.list()).containsExactlyInAnyOrder(MenuGroupResponse.from(menuGroup1),
                MenuGroupResponse.from(menuGroup2));
    }
}
