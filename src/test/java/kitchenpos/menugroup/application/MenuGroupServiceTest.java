package kitchenpos.menugroup.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.menugroup.ui.dto.request.MenuGroupRequestDto;
import kitchenpos.menugroup.ui.dto.response.MenuGroupResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DisplayName("메뉴 그룹 서비스 통합 테스트")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("[성공] 새로운 메뉴 그룹을 등록")
    @Test
    void create_Success() {
        // given
        MenuGroupRequestDto menuGroup = newMenuGroup();

        // when
        MenuGroupResponseDto result = menuGroupService.create(menuGroup);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo(menuGroup.getName());
    }

    @DisplayName("[성공] 메뉴 그룹 리스트 조회")
    @Test
    void list_Success() {
        // given
        int previousMenuGroupsCount = menuGroupService.list().size();
        menuGroupService.create(newMenuGroup());

        // when
        List<MenuGroupResponseDto> result = menuGroupService.list();

        // then
        assertThat(result).hasSize(previousMenuGroupsCount + 1);
    }

    private MenuGroupRequestDto newMenuGroup() {
        MenuGroupRequestDto menuGroup = new MenuGroupRequestDto("새로운 메뉴 그룹");
        return menuGroup;
    }
}
