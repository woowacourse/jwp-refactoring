package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import kitchenpos.application.dto.request.menu.MenuGroupRequestDto;
import kitchenpos.application.dto.response.menu.MenuGroupResponseDto;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("MenuGroupService 단위 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @DisplayName("create 메서드는")
    @Nested
    class Describe_create {

        @DisplayName("주어진 MenuGroup을 DB에 저장하고 ID를 포함한 Entity를 반환한다.")
        @Test
        void it_saves_and_returns_menuGroupEntity() {
            // given
            MenuGroupRequestDto menuGroupRequestDto = new MenuGroupRequestDto("분식류");
            MenuGroup expected = new MenuGroup(1L, "분식류");
            given(menuGroupRepository.save(any(MenuGroup.class))).willReturn(expected);

            // when
            MenuGroupResponseDto response = menuGroupService.create(menuGroupRequestDto);

            // then
            assertThat(response).usingRecursiveComparison()
                .isEqualTo(new MenuGroupResponseDto(1L, "분식류"));

            verify(menuGroupRepository, times(1)).save(any(MenuGroup.class));
        }
    }

    @DisplayName("list 메서드는")
    @Nested
    class Describe_list {

        @DisplayName("DB에 저장된 MenuGroup 목록을 반환한다.")
        @Test
        void it_returns_menuGroupList() {
            // given
            MenuGroup menuGroup = new MenuGroup(1L, "분식류");
            MenuGroup menuGroup2 = new MenuGroup(2L, "안주류");
            List<MenuGroup> expected = Arrays.asList(menuGroup, menuGroup2);
            given(menuGroupRepository.findAll()).willReturn(expected);

            // when
            List<MenuGroupResponseDto> response = menuGroupService.list();

            // then
            assertThat(response).extracting("id", "name")
                .contains(tuple(1L, "분식류"), tuple(2L, "안주류"));

            verify(menuGroupRepository, times(1)).findAll();
        }
    }
}
