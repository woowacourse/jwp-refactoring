package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.repository.MenuGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class MenuGroupServiceTest {
    @MockBean
    MenuGroupRepository menuGroupRepository;

    @Autowired
    MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹을 저장할 수 있다.")
    void create() {
        // given
        String menuGroupName = "menuGroup";

        MenuGroup menuGroup = new MenuGroup(1L, menuGroupName);

        given(menuGroupRepository.save(any(MenuGroup.class)))
                .willReturn(menuGroup);

        MenuGroupRequest menuGroupRequest = new MenuGroupRequest(menuGroupName);

        // when
        MenuGroup actual = menuGroupService.create(menuGroupRequest);

        // then
        assertThat(actual).isEqualTo(menuGroup);
    }

    @Test
    @DisplayName("등록된 메뉴 그룹 목록을 불러올 수 있다.")
    void list() {
        // given
        MenuGroup menuGroup1 = new MenuGroup(1L, "menuGroup1");
        MenuGroup menuGroup2 = new MenuGroup(2L, "menuGroup2");

        List<MenuGroup> expected = Arrays.asList(menuGroup1, menuGroup2);
        given(menuGroupRepository.findAll())
                .willReturn(expected);

        // when
        List<MenuGroup> actual = menuGroupRepository.findAll();

        // then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }
}
