package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.ui.jpa.dto.menugroup.MenuGroupCreateRequest;
import kitchenpos.ui.jpa.dto.menugroup.MenuGroupCreateResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("menuGroup을 생성한다.")
    @Test
    void create() {
        MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest("이름");

        MenuGroupCreateResponse menuGroupCreateResponse = menuGroupService.create(menuGroupCreateRequest);

        assertThat(menuGroupCreateResponse.getId()).isNotNull();
        assertThat(menuGroupCreateResponse.getName()).isEqualTo(menuGroupCreateRequest.getName());
    }

    @DisplayName("menuGroup을 모두 조회한다.")
    @Test
    void list() {
        int numberOfMenuGroupBeforeCreate = menuGroupService.list().size();
        menuGroupService.create(new MenuGroupCreateRequest("이름"));

        int numberOfMenuGroup = menuGroupService.list().size();

        assertThat(numberOfMenuGroupBeforeCreate + 1).isEqualTo(numberOfMenuGroup);
    }
}
