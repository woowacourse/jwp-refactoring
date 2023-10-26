package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.application.dto.request.MenuGroupCreateRequest;
import kitchenpos.application.dto.response.MenuGroupResponse;
import kitchenpos.application.support.domain.MenuGroupTestSupport;
import kitchenpos.domain.MenuGroup;
import kitchenpos.repository.MenuGroupRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Disabled
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    MenuGroupRepository menuGroupRepository;

    @InjectMocks
    MenuGroupService target;

    @DisplayName("메뉴 그룹을 생성하면 저장된 메뉴 그룹을 반환한다.")
    @Test
    void create() {
        //given
        final MenuGroupCreateRequest request = MenuGroupTestSupport.builder().name("허브 허브 메뉴")
                .buildToMenuGroupCreateRequest();

        final var expectedResult = MenuGroupTestSupport.builder().name(request.getName()).build();
        given(menuGroupRepository.save(any(MenuGroup.class))).willReturn(expectedResult);

        //when
        final MenuGroupResponse result = target.create(request);

        //then
        assertThat(request.getName()).isEqualTo(result.getName());
    }

    @DisplayName("존재하는 모든 메뉴 그룹을 조회한다.")
    @Test
    void list() {
        //given
        final MenuGroup menuGroup1 = MenuGroupTestSupport.builder().build();
        final MenuGroup menuGroup2 = MenuGroupTestSupport.builder().build();

        given(menuGroupRepository.findAll()).willReturn(List.of(menuGroup1, menuGroup2));

        //when
        final List<MenuGroupResponse> result = target.list();

        //then
        assertThat(result).extracting(MenuGroupResponse::getId)
                .contains(menuGroup1.getId(), menuGroup2.getId());
    }
}
