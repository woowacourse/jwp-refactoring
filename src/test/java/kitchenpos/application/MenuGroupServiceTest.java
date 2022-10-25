package kitchenpos.application;

import static kitchenpos.support.fixture.domain.MenuGroupFixture.KOREAN;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.NestedApplicationTest;
import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao;

    @NestedApplicationTest
    @DisplayName("create 메서드는")
    class Create {

        @Test
        @DisplayName("MenuGroup을 생성한다.")
        void success() {
            MenuGroup menuGroup = KOREAN.getMenuGroup();

            MenuGroup actual = menuGroupService.create(menuGroup);

            assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(menuGroup);
        }
    }

    @NestedApplicationTest
    @DisplayName("list 메서드는")
    class ListTest {

        @BeforeEach
        void setUp() {
            jdbcTemplateMenuGroupDao.save(KOREAN.getMenuGroup());
            jdbcTemplateMenuGroupDao.save(KOREAN.getMenuGroup());
        }

        @Test
        @DisplayName("MenuGroup 전체 목록을 조회한다.")
        void success() {
            List<MenuGroup> menuGroups = menuGroupService.list();

            assertThat(menuGroups).hasSize(2);
        }
    }
}
