package kitchenpos.repository;

import kitchenpos.domain.MenuGroup;
import kitchenpos.fixture.MenuGroupFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql(scripts = "/clear.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DisplayName("MenuGroupRepository 테스트")
class MenuGroupRepositoryTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @DisplayName("메뉴 그룹 추가")
    @Test
    void create() {
        //given
        MenuGroup menuGroup = MenuGroupFixture.create();
        //when
        MenuGroup created = menuGroupRepository.save(menuGroup);
        //then
        assertThat(created.getId()).isNotNull();
    }

    @DisplayName("메뉴 그룹 반환")
    @Test
    void list() {
        //given
        MenuGroup menuGroup = MenuGroupFixture.create();
        menuGroupRepository.save(menuGroup);
        //when
        List<MenuGroup> menuGroups = menuGroupRepository.findAll();
        //then
        assertThat(menuGroups).hasSize(1);
    }
}
