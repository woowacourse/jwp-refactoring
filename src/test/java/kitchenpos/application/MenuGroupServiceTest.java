package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.fixture.MenuGroupFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@Sql(scripts = {"/truncate.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성 후 조회한다.")
    @Test
    void createAndList() {
        // given
        MenuGroup menuGroup = MenuGroupFixtures.두마리_메뉴;

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).hasSize(1);
        assertThat(menuGroups.get(0).getName()).isEqualTo(savedMenuGroup.getName());
    }
}
