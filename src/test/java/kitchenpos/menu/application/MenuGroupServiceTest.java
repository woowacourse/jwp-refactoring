package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.common.DataClearExtension;
import kitchenpos.menu.application.dto.response.MenuGroupResponse;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("메뉴 그룹 관련 기능에서")
@ExtendWith(DataClearExtension.class)
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    @DisplayName("메뉴 그룹을 정상적으로 생성한다.")
    void create() {
        MenuGroupResponse response = menuGroupService.create("추천 메뉴");

        assertThat(response.getId()).isNotNull();
    }

    @Test
    @DisplayName("존재하는 메뉴 그룹을 모두 조회한다.")
    void list() {
        menuGroupRepository.save(new MenuGroup("추천 메뉴"));
        menuGroupRepository.save(new MenuGroup("할인 메뉴"));
        menuGroupRepository.save(new MenuGroup("시즌 메뉴"));

        List<MenuGroupResponse> responses = menuGroupService.list();

        assertThat(responses).hasSize(3);
    }
}
