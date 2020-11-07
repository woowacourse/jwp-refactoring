package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.fixture.MenuGroupFixture;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @DisplayName("Menu Group 생성")
    @Test
    void create() {
        MenuGroup menuGroupWithoutId = MenuGroupFixture.createWithoutId();
        MenuGroup menuGroupWithId = MenuGroupFixture.createWithId(1L);

        when(menuGroupDao.save(menuGroupWithoutId)).thenReturn(menuGroupWithId);

        assertThat(menuGroupService.create(menuGroupWithoutId))
            .isEqualToComparingFieldByField(menuGroupWithId);
    }

    @DisplayName("Menu Group 조회")
    @Test
    void list() {
        MenuGroup menuGroupWithId1 = MenuGroupFixture.createWithId(1L);
        MenuGroup menuGroupWithId2 = MenuGroupFixture.createWithId(2L);
        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(menuGroupWithId1, menuGroupWithId2));

        assertThat(menuGroupService.list())
            .usingRecursiveComparison()
            .isEqualTo(Arrays.asList(menuGroupWithId1, menuGroupWithId2));
    }
}
