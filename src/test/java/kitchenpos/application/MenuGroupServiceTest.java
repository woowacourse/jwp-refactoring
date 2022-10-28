package kitchenpos.application;

import static kitchenpos.Fixture.MENU_GROUP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.application.dto.request.MenuGroupCreateRequest;
import kitchenpos.application.dto.response.MenuGroupResponseDto;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
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
    private MenuGroupDao menuGroupDao;

    @Test
    void create() {
        //given
        given(menuGroupDao.save(any(MenuGroup.class)))
                .willReturn(MENU_GROUP);

        //when
        MenuGroupCreateRequest dto = new MenuGroupCreateRequest("추천메뉴");
        MenuGroupResponseDto savedManuGroup = menuGroupService.create(dto);

        //then
        assertThat(savedManuGroup).isEqualTo(MENU_GROUP);
    }

    @Test
    void list() {
        //given
        given(menuGroupDao.findAll())
                .willReturn(List.of(MENU_GROUP));

        //when
        List<MenuGroupResponseDto> menuGroups = menuGroupService.list();

        //then
        assertThat(menuGroups).hasSize(1);
    }
}
