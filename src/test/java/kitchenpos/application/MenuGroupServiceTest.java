package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
@Rollback
public class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("menuGroup을 생성한다.")
    @Test
    void create() {
        // given
        final MenuGroup menuGroup = new MenuGroup("세마리메뉴");

        // when
        final MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertThat(savedMenuGroup.getId()).isNotNull();
    }
}
