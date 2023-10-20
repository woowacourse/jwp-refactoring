package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.repository.MenuGroupRepository;
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
        MenuGroup menuGroup = new MenuGroup("kong");
        when(menuGroupRepository.save(any())).thenReturn(menuGroup);

        assertThat(menuGroupService.create("kong").getName()).isEqualTo("kong");
    }

    @Test
    @DisplayName("현재 저장된 메뉴 그룹 목록을 확인할 수 있다.")
    void list_success() {
        MenuGroup menuGroup1 = new MenuGroup("kong");
        MenuGroup menuGroup2 = new MenuGroup("wuga");
        when(menuGroupRepository.findAll()).thenReturn(List.of(menuGroup1, menuGroup2));

        assertThat(menuGroupService.list()).containsExactlyInAnyOrder(menuGroup1, menuGroup2);
    }
}
