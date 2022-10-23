package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@DisplayName("MenuGroup 테스트")
@JdbcTest
class MenuGroupDaoTest {

    @Autowired
    private DataSource dataSource;

    private MenuGroupDao menuGroupDao;

    @BeforeEach
    void setUp() {
        this.menuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
    }

    @DisplayName("save 메서드는")
    @Nested
    class save {

        @DisplayName("메뉴 그룹이 주어지면")
        @Nested
        class givenMenuGroup {

            private final MenuGroup menuGroup = new MenuGroup("세마리메뉴");

            @DisplayName("저장한다.")
            @Test
            void saves() {
                final MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

                assertThat(savedMenuGroup.getId()).isNotNull();
            }
        }
    }

    @DisplayName("findById 메서드는")
    @Nested
    class findById {

        @DisplayName("id가 주어지면")
        @Nested
        class givenMenuGroupId {

            private final MenuGroup menuGroup = new MenuGroup("세마리메뉴");
            private MenuGroup savedMenuGroup;

            @BeforeEach
            void setUp() {
                savedMenuGroup = menuGroupDao.save(menuGroup);
            }

            @DisplayName("해당하는 MenuGroup을 반환한다.")
            @Test
            void returnsMenuGroup() {
                final Optional<MenuGroup> foundMenuGroup = menuGroupDao.findById(savedMenuGroup.getId());

                assertAll(
                        () -> assertThat(foundMenuGroup).isPresent(),
                        () -> assertThat(foundMenuGroup.get()).usingRecursiveComparison()
                                .isEqualTo(savedMenuGroup)
                );
            }
        }
    }


    @DisplayName("findAll 메서드는")
    @Nested
    class findAll {

        @DisplayName("호출되면")
        @Nested
        class whenCalled {

            private final MenuGroup menuGroup = new MenuGroup("세마리메뉴");
            private MenuGroup savedMenuGroup;

            @BeforeEach
            void setUp() {
                savedMenuGroup = menuGroupDao.save(menuGroup);
            }

            @DisplayName("모든 MenuGroup들을 반환한다.")
            @Test
            void returnsAllMenuGroups() {
                final List<MenuGroup> menuGroups = menuGroupDao.findAll();

                assertThat(menuGroups).usingFieldByFieldElementComparator()
                        .containsAll(List.of(savedMenuGroup));
            }
        }
    }

    @DisplayName("existsById 메서드는")
    @Nested
    class existsById {

        private MenuGroup menuGroup;

        @BeforeEach
        void setUp() {
            this.menuGroup = menuGroupDao.save(new MenuGroup("세마리메뉴"));
        }

        @DisplayName("만약 존재하는 메뉴 그룹이라면")
        @Nested
        class withExistingMenu {

            @DisplayName("true를 반환한다.")
            @Test
            void returnsTrue() {
                final boolean actual = menuGroupDao.existsById(menuGroup.getId());
                assertThat(actual).isTrue();
            }
        }

        @DisplayName("만약 존재하지 않는 그룹이라면")
        @Nested
        class withoutExistingMenu {

            @DisplayName("false를 반환한다.")
            @Test
            void returnsFalse() {
                final boolean actual = menuGroupDao.existsById(Long.MAX_VALUE);
                assertThat(actual).isFalse();
            }
        }
    }
}
