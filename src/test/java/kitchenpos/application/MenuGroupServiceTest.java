package kitchenpos.application;

import kitchenpos.application.config.ServiceTestConfig;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MenuGroupServiceTest extends ServiceTestConfig {

    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @DisplayName("메뉴 그룹 생성")
    @Nested
    class Create {
        @DisplayName("성공한다.")
        @Test
        void success() {
            // given
            final MenuGroup menuGroupInput = new MenuGroup();
            menuGroupInput.setName("메뉴 그룹");

            // when
            final MenuGroup actual = menuGroupService.create(menuGroupInput);

            // then
            assertThat(actual.getName()).isEqualTo(menuGroupInput.getName());
        }
    }
}
