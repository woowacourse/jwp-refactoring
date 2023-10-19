package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import kitchenpos.application.dto.request.MenuGroupCreateRequest;
import kitchenpos.application.dto.response.MenuGroupResponse;
import kitchenpos.application.mapper.MenuGroupMapper;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹을 성공적으로 생성한다")
    void testCreateSuccess() {
        //given
        final MenuGroup expected = new MenuGroup(1L, "test");

        final MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest("test");

        when(menuGroupDao.save(any()))
                .thenReturn(expected);

        //when
        final MenuGroupResponse result = menuGroupService.create(menuGroupCreateRequest);

        //then
        assertThat(result).isEqualTo(MenuGroupMapper.mapToResponse(expected));
    }

    @Test
    @DisplayName("메뉴 그룹 리스트 조회한다")
    void testListSuccess() {
        //given
        final List<MenuGroup> menuGroups = List.of(new MenuGroup(1L, "test"));
        when(menuGroupDao.findAll())
                .thenReturn(menuGroups);

        //when
        final List<MenuGroupResponse> result = menuGroupService.list();

        //then
        final List<MenuGroupResponse> expected = menuGroups.stream()
                .map(MenuGroupMapper::mapToResponse)
                .collect(Collectors.toList());
        assertThat(result).isEqualTo(expected);
    }
}
