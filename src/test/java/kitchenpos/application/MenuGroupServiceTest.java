package kitchenpos.application;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.support.ServiceTest;
import kitchenpos.menugroup.ui.dto.MenuGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayName("메뉴 그룹 서비스 테스트")
@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_분류를_저장한다() {
        final MenuGroupRequest 새로운_메뉴_분류 = new MenuGroupRequest("괴식");
        final MenuGroup 저장된_메뉴_분류 = menuGroupService.create(새로운_메뉴_분류);

        assertSoftly(soft -> {
            soft.assertThat(저장된_메뉴_분류.getId()).isNotNull();
            soft.assertThat(저장된_메뉴_분류.getName().getName()).isEqualTo(새로운_메뉴_분류.getName());
        });
    }

    @Test
    void 모든_메뉴_분류를_조회한다() {
        menuGroupService.create(new MenuGroupRequest("괴식"));
        final List<MenuGroup> 저장된_메뉴_분류 = menuGroupService.list();

        assertThat(저장된_메뉴_분류).hasSize(1);
    }
}
