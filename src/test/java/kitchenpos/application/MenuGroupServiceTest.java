package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.dto.request.MenuGroupRequest;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.support.DatabaseCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void setUp() {
        databaseCleaner.execute();
    }

    @DisplayName("menuGroup을 생성한다.")
    @Test
    void create() {
        // given
        final MenuGroupRequest request = new MenuGroupRequest("메뉴 그룹");

        // when
        final MenuGroup savedMenuGroup = menuGroupService.create(request);

        // then
        assertThat(savedMenuGroup.getId()).isNotNull();
    }

    @DisplayName("menuGroup들을 조회한다.")
    @Test
    void list() {
        // given
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));

        // when
        final List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups.size()).isEqualTo(1);
    }
}
