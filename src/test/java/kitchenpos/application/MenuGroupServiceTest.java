package kitchenpos.application;

import static kitchenpos.support.fixture.domain.MenuGroupFixture.KOREAN;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/truncate.sql")
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao;

    @Nested
    @DisplayName("create 메서드는")
    class Create {

        @Test
        @DisplayName("MenuGroup을 생성한다.")
        void success() {
            MenuGroup menuGroup = menuGroupService.create(KOREAN.getMenuGroup());

            MenuGroup actual = jdbcTemplateMenuGroupDao.findById(menuGroup.getId())
                .orElseThrow();
            assertThat(actual).usingRecursiveComparison()
                .isEqualTo(menuGroup);
        }
    }

    @Nested
    @DisplayName("list 메서드는")
    class ListTest {

        @Test
        @DisplayName("Product 전체 목록을 조회한다.")
        void success() {
            List<MenuGroup> menuGroups = menuGroupService.list();

            assertThat(menuGroups).hasSize(4);
        }
    }
}
