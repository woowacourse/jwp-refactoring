package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.exception.FieldNotValidException;
import kitchenpos.ui.dto.MenuGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@DisplayName("메뉴 그룹 서비스 테스트")
class MenuGroupServiceTest extends ServiceTest {

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @DisplayName("메뉴 그룹을 생성한다. - 실패, 메뉴그룹명이 null 또는 empty")
    @ParameterizedTest
    @NullAndEmptySource
    void create(String name) {
        // given
        MenuGroupRequest menuGroupRequest = CREATE_MENU_GROUP_REQUEST(name);

        // when - then
        assertThatThrownBy(() -> menuGroupService.create(menuGroupRequest))
                .isInstanceOf(FieldNotValidException.class);
        then(menuGroupRepository).should(never())
                .save(any(MenuGroup.class));
    }
}
