package kitchenpos.application;

import kitchenpos.config.IsolatedTest;
import kitchenpos.ui.dto.menugroup.MenuGroupRequest;
import kitchenpos.ui.dto.menugroup.MenuGroupResponse;
import kitchenpos.ui.dto.menugroup.MenuGroupResponses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class MenuGroupServiceTest extends IsolatedTest {

    @Autowired
    private MenuGroupService service;

    @DisplayName("메뉴 그룹 생성")
    @Test
    public void createMenuGroup() {
        MenuGroupRequest request = new MenuGroupRequest("패스트 푸드");
        final MenuGroupResponse response = service.create(request);

        assertThat(response.getName()).isEqualTo("패스트 푸드");
    }

    @DisplayName("메뉴 그룹 목록 조회")
    @Test
    public void readMenuGroup() {
        MenuGroupRequest request = new MenuGroupRequest("패스트 푸드");
        final MenuGroupResponse response = service.create(request);

        final MenuGroupResponses responses = service.list();

        assertThat(responses.getMenuGroupResponses()).hasSize(1);
        assertThat(responses.getMenuGroupResponses().get(0).getName()).isEqualTo("패스트 푸드");
    }
}
