package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.MenuGroupRequest;
import kitchenpos.application.dto.response.MenuGroupResponse;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() {
        //given
        MenuGroup menuGroup = new MenuGroup.MenuGroupBuilder()
                                .setName("치킨")
                                .build();

        //when
        MenuGroupResponse actual = menuGroupService
                .create(MenuGroupRequest.create(menuGroup));

        MenuGroupResponse expected = MenuGroupResponse.create(new MenuGroup.MenuGroupBuilder()
                                        .setId(actual.getId())
                                        .setName("치킨")
                                        .build());

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("메뉴 그룹 목록을 불러온다.")
    @Test
    void list() {
        //given
        MenuGroup menuGroup1 = new MenuGroup.MenuGroupBuilder()
                                    .setId(1L)
                                    .setName("치킨")
                                    .build();

        MenuGroup menuGroup2 = new MenuGroup.MenuGroupBuilder()
                                    .setId(2L)
                                    .setName("한마리메뉴")
                                    .build();

        menuGroupService.create(MenuGroupRequest.create(menuGroup1));
        menuGroupService.create(MenuGroupRequest.create(menuGroup2));

        List<MenuGroupResponse> expected = List.of(menuGroup1, menuGroup2).stream()
                                            .map(MenuGroupResponse::create)
                                            .collect(Collectors.toList());

        //when
        List<MenuGroupResponse> actual = menuGroupService.list();

        //then
        assertThat(actual).hasSize(2);
        assertThat(actual.get(0).getName()).isEqualTo(expected.get(0).getName());
        assertThat(actual.get(1).getName()).isEqualTo(expected.get(1).getName());
    }
}
