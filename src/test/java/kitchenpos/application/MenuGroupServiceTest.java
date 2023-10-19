package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import kitchenpos.application.dto.request.MenuGroupCreateRequest;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

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
        final MenuGroup result = menuGroupService.create(menuGroupCreateRequest);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("메뉴 그룹 리스트 조회한다")
    void testListSuccess() {
        //given
        final List<MenuGroup> expected = List.of(new MenuGroup(1L, "test"));
        when(menuGroupDao.findAll())
                .thenReturn(expected);

        //when
        final List<MenuGroup> result = menuGroupService.list();

        //then
        assertThat(result).isEqualTo(expected);
    }
}
