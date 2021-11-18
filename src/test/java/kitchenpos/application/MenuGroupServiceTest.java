package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.MenuGroupRequest;
import kitchenpos.application.dto.response.MenuGroupResponse;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Transactional
class MenuGroupServiceTest {

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

        MenuGroupResponse expected = MenuGroupResponse.toDto(new MenuGroup.MenuGroupBuilder()
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
                                    .setName("두마리메뉴")
                                    .build();

        MenuGroup menuGroup2 = new MenuGroup.MenuGroupBuilder()
                                    .setId(2L)
                                    .setName("한마리메뉴")
                                    .build();

        MenuGroup menuGroup3 = new MenuGroup.MenuGroupBuilder()
                                    .setId(3L)
                                    .setName("순살파닭두마리메뉴")
                                    .build();

        MenuGroup menuGroup4 = new MenuGroup.MenuGroupBuilder()
                                    .setId(4L)
                                    .setName("신메뉴")
                                    .build();

        List<MenuGroupResponse> expected = List.of(menuGroup1, menuGroup2, menuGroup3, menuGroup4).stream()
                                            .map(MenuGroupResponse::toDto)
                                            .collect(Collectors.toList());

        //when
        List<MenuGroupResponse> actual = menuGroupService.list();
        //then

        assertThat(actual).isEqualTo(expected);
    }
}