package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MenuGroupServiceTest {

    @Nested
    class 메뉴_그룹_생성 extends IntegrationTest {
        @Test
        void 요청을_할_수_있다() {
            // given
            final MenuGroup menuGroup = new MenuGroup("세트 1번");

            // when
            final MenuGroup extract = menuGroupService.create(menuGroup);

            // then
            assertThat(extract).isNotNull();
        }
    }

    @Nested
    class 메뉴_리스트_조회 extends IntegrationTest {
        @Test
        void 요청을_할_수_있다() {
            // given
            final MenuGroup menuGroup = new MenuGroup("두마리메뉴");
            menuGroupService.create(menuGroup);

            // when
            final List<MenuGroup> extract = menuGroupService.list();

            // then
            assertThat(extract).hasSize(1);
        }
    }
}