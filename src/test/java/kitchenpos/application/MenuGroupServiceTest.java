package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.dto.MenuGroupCreateRequest;
import kitchenpos.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void create() {
        final String name = "1번 메뉴 그룹";
        final MenuGroupCreateRequest request = new MenuGroupCreateRequest(name);

        final MenuGroupResponse response = menuGroupService.create(request);

        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getName()).isEqualTo(name)
        );
    }

    @DisplayName("메뉴 그룹들을 조회할 수 있다.")
    @Test
    void list() {
        final MenuGroupCreateRequest firstRequest = new MenuGroupCreateRequest("1번 메뉴 그룹");
        final MenuGroupCreateRequest secondRequest = new MenuGroupCreateRequest("2번 메뉴 그룹");
        menuGroupService.create(firstRequest);
        menuGroupService.create(secondRequest);

        final List<MenuGroupResponse> response = menuGroupService.list();

        assertThat(response).hasSize(2);
    }
}
