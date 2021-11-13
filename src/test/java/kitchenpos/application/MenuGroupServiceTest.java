package kitchenpos.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.repository.MenuGroupRepository;
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

    @DisplayName("메뉴 그룹 생성")
    @Test
    void create() {
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("한마리치킨");

        menuGroupService.create(menuGroupRequest);

        verify(menuGroupRepository).save(any(MenuGroup.class));
    }

    @DisplayName("메뉴 그룹 불러오기")
    @Test
    void list() {
        menuGroupService.list();

        verify(menuGroupRepository).findAll();

    }
}
