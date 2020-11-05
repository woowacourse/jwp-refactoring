package kitchenpos.application;

import static kitchenpos.util.ObjectUtil.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.MenuGroup;
import kitchenpos.repository.MenuGroupRepository;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("id가 없는 메뉴 그룹으로 id가 있는 메뉴 그룹을 정상적으로 생성한다.")
    @Test
    void createTest() {
        final String name = "메뉴";
        final MenuGroup expectedMenuGroup = createMenuGroup(1L, name);

        given(menuGroupRepository.save(any(MenuGroup.class))).willReturn(expectedMenuGroup);

        final MenuGroup persistMenuGroup = menuGroupService.create(name);
        assertThat(persistMenuGroup).isEqualToComparingFieldByField(expectedMenuGroup);
    }

    @DisplayName("메뉴 그룹들을 정상적으로 조회한다.")
    @Test
    void listTest() {
        final List<MenuGroup> expectedMenuGroups = Collections.singletonList(createMenuGroup(1L, "메뉴"));

        given(menuGroupRepository.findAll()).willReturn(expectedMenuGroups);

        final List<MenuGroup> persistMenuGroups = menuGroupService.list();
        assertThat(persistMenuGroups).usingRecursiveComparison().isEqualTo(expectedMenuGroups);
    }
}
