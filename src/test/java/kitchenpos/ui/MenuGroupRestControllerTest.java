package kitchenpos.ui;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

import java.util.List;
import kitchenpos.application.dto.request.MenuGroupCreateRequest;
import kitchenpos.application.dto.response.MenuGroupResponse;
import kitchenpos.application.support.RequestFixture;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class MenuGroupRestControllerTest {

    @Autowired
    MenuGroupRestController menuGroupRestController;

    @DisplayName("POST {{host}}/api/menu-groups")
    @Test
    void menu_group_create() {
        //given
        final MenuGroupCreateRequest request = RequestFixture.menuGroupCreateRequest();
        //when
        final ResponseEntity<MenuGroupResponse> response = menuGroupRestController.create(
                request);
        //then
        SoftAssertions.assertSoftly(
                soft -> {
                    soft.assertThat(response.getBody().getName()).isEqualTo(request.getName());
                }
        );
    }

    @DisplayName("GET {{host}}/api/menu-groups")
    @Test
    void menu_group_list() {
        //given

        //when
        final ResponseEntity<List<MenuGroupResponse>> response = menuGroupRestController.list();

        //then
        SoftAssertions.assertSoftly(
                soft -> {
                    soft.assertThat(response.getBody().size()).isEqualTo(4);
                }
        );
    }
}
