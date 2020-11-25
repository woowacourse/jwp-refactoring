package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.dto.MenuGroupCreateRequest;
import kitchenpos.dto.MenuGroupResponse;
import kitchenpos.utils.TestObjectFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest
@Sql(scripts = "classpath:/init-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:/truncate.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void create() {
        MenuGroupCreateRequest menuGroupCreateRequest
            = TestObjectFactory.createMenuGroupCreateRequest("세마리메뉴");
        MenuGroupResponse menuGroupResponse = menuGroupService.create(menuGroupCreateRequest);

        assertAll(() -> {
            assertThat(menuGroupResponse).isInstanceOf(MenuGroupResponse.class);
            assertThat(menuGroupResponse).isNotNull();
            assertThat(menuGroupResponse.getId()).isNotNull();
            assertThat(menuGroupResponse.getName()).isNotNull();
            assertThat(menuGroupResponse.getName()).isEqualTo("세마리메뉴");
        });
    }

    @DisplayName("메뉴 그룹 리스트를 조회한다.")
    @Test
    void list() {
        List<MenuGroupResponse> menuGroupList = menuGroupService.list();

        assertAll(() -> {
            assertThat(menuGroupList).isNotEmpty();
            assertThat(menuGroupList).hasSize(4);
        });
    }
}
