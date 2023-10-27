package kitchenpos.application;

import kitchenpos.dto.MenuGroupCreateRequest;
import kitchenpos.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MenuGroupServiceTest extends ServiceTest {

    private MenuGroupCreateRequest menuGroupCreateRequest;

    @BeforeEach
    void setUp() {
        menuGroupCreateRequest = makeMenuGroupCreate();
    }

    @Test
    void 메뉴그룹을_생성한다() {
        MenuGroupResponse saved = menuGroupService.create(menuGroupCreateRequest);

        assertThat(saved.getName()).isEqualTo(menuGroupCreateRequest.getName());
    }

    @Test
    void 전체_메뉴그룹을_조회한다() {
        List<MenuGroupResponse> responses = menuGroupService.list();

        assertThat(responses.size()).isEqualTo(4);
    }

    private MenuGroupCreateRequest makeMenuGroupCreate(){
        return new MenuGroupCreateRequest("한마리통닭");
    }
}
