package kitchenpos.application;

import static kitchenpos.Fixture.MENU_GROUP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.menugroup.application.dto.MenuGroupCreateRequest;
import kitchenpos.menugroup.application.dto.MenuGroupResponse;
import kitchenpos.menugroup.dao.MenuGroupDao;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.application.MenuGroupService;
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
        MenuGroupResponse savedManuGroup = menuGroupService.create(dto);

        //then
        assertAll(
                () -> assertThat(savedManuGroup.getId()).isEqualTo(MENU_GROUP.getId()),
                () -> assertThat(savedManuGroup.getName()).isEqualTo(MENU_GROUP.getName())
        );
    }

    @Test
    void list() {
        //given
        given(menuGroupDao.findAll())
                .willReturn(List.of(MENU_GROUP));

        //when
        List<MenuGroupResponse> menuGroups = menuGroupService.list();

        //then
        assertThat(menuGroups).hasSize(1);
    }
}
