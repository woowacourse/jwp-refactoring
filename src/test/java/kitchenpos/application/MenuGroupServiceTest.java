package kitchenpos.application;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.dto.MenuGroupRequest;
import kitchenpos.domain.model.MenuGroupRepository;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    void create() {
        MenuGroupRequest request = new MenuGroupRequest("추천 메뉴");

        menuGroupService.create(request);

        verify(menuGroupRepository).save(request.toEntity());
    }

    @Test
    void list() {
        menuGroupService.list();

        verify(menuGroupRepository).findAll();
    }
}