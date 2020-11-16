package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import kitchenpos.repository.MenuGroupRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static kitchenpos.fixture.MenuGroupFixture.createMenuGroupWithoutId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @DisplayName("MenuGroup 등록 성공")
    @Test
    void create() {
        MenuGroup menuGroup = createMenuGroupWithoutId();

        MenuGroup actual = menuGroupService.create(menuGroup);

        assertThat(actual.getId()).isNotNull();
    }

    @DisplayName("MenuGroup 전체 조회")
    @Test
    void findAll() {
        MenuGroup menuGroup = menuGroupRepository.save(createMenuGroupWithoutId());

        List<MenuGroup> actual = menuGroupService.list();

        assertAll(() -> {
            assertThat(actual).hasSize(1);
            assertThat(actual.get(0)).isEqualToIgnoringNullFields(menuGroup);
        });
    }

    @AfterEach
    void tearDown() {
        menuGroupRepository.deleteAll();
    }
}