package kitchenpos.menuGroup.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@Transactional
@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_생성한다() {
        // given
        MenuGroupRequest request = new MenuGroupRequest("kitchenpos/menuGroup");

        // when
        MenuGroupResponse response = menuGroupService.create(request);

        // then
        assertThat(response.getId()).isNotNull();
    }

    @Test
    void 메뉴_그룹을_전체_조회한다() {
        // when, then
        assertThat(menuGroupService.list()).isInstanceOf(List.class);
    }
}
