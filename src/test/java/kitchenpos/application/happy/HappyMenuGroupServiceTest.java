package kitchenpos.application.happy;

import kitchenpos.ui.dto.request.MenuGroupRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class HappyMenuGroupServiceTest extends HappyServiceTest {

    @Test
    void 메뉴_그룹_생성() {
        final MenuGroupRequest 터놓고가_추천하는_제육볶음 = new MenuGroupRequest("터놓고가_추천하는_제육볶음");
        final Long 터놓고가_추천하는_제육볶음_ID = menuGroupService.create(터놓고가_추천하는_제육볶음).getId();

        Assertions.assertThat(터놓고가_추천하는_제육볶음_ID).isNotNull();
    }

    @Test
    void 메뉴_그룹_목록_조회() {
        final MenuGroupRequest 터놓고가_추천하는_제육볶음 = new MenuGroupRequest("터놓고가_추천하는_제육볶음");
        final MenuGroupRequest 우아한_형제들_추천 = new MenuGroupRequest("우아한_형제들_추천");
        menuGroupService.create(터놓고가_추천하는_제육볶음).getId();
        menuGroupService.create(우아한_형제들_추천).getId();

        Assertions.assertThat(menuGroupService.list()).hasSize(2);
    }
}
