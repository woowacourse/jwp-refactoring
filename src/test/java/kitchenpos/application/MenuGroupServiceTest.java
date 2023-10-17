package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.dto.MenuGroupRequest;

@DisplayName("메뉴 그룹 테스트")
class MenuGroupServiceTest extends BaseServiceTest{

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹을 생성한다.")
    void createTest() {
        //given
        final MenuGroupRequest request = new MenuGroupRequest("분식");

        //when
        final MenuGroup createdMenuGroup = menuGroupService.create(request);

        //then
        assertSoftly(softAssertions -> {
            assertThat(createdMenuGroup.getId()).isNotNull();
            assertThat(createdMenuGroup.getName()).isEqualTo(request.getName());
        });
    }

    @Test
    @DisplayName("모든 메뉴그룹을 반환한다.")
    void list() {
        assertThatCode(() -> menuGroupService.list())
                .doesNotThrowAnyException();
    }
}
