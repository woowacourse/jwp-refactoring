package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import javax.transaction.Transactional;
import kitchenpos.application.dto.MenuGroupRequest;
import kitchenpos.application.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다")
    @Test
    void create() {
        final MenuGroupRequest menuGroupRequest = new MenuGroupRequest("반반");

        final MenuGroupResponse menuGroupResponse = menuGroupService.create(menuGroupRequest);

        assertThat(menuGroupResponse.getId()).isNotNull();
    }

    @DisplayName("전체 메뉴 그룹을 조회한다")
    @Test
    void findAll() {
        final List<MenuGroupResponse> menuGroupResponses = menuGroupService.list();

        assertThat(menuGroupResponses).hasSize(4);
    }
}
