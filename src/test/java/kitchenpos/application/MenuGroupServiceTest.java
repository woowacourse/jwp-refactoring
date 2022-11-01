package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dto.request.MenuGroupCreateRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    @Test
    void create() {
        MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest("햄버거");

        menuGroupService.create(menuGroupCreateRequest);

        List<MenuGroupResponse> menuGroupResponses = menuGroupService.list();
        List<String> menuGroupNames = menuGroupResponses.stream()
                .map(MenuGroupResponse::getName)
                .collect(Collectors.toUnmodifiableList());
        assertAll(
                () -> assertThat(menuGroupResponses).hasSize(1),
                () -> assertThat(menuGroupNames).contains("햄버거")
        );
    }
}
