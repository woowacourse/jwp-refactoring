package kitchenpos.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import kitchenpos.application.dto.MenuGroupCreateRequest;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹_생성할_수_있다() {
        MenuGroupCreateRequest 분식메뉴그룹_요청 = MenuGroupFixtures.분식메뉴그룹_요청();
        menuGroupService.create(분식메뉴그룹_요청);
        verify(menuGroupRepository).save(any(MenuGroup.class));
    }

    @Test
    void 전체_메뉴_그룹_조회할_수_있다() {
        menuGroupService.list();
        verify(menuGroupRepository).findAll();
    }
}
