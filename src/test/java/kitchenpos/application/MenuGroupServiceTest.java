package kitchenpos.application;

import static org.junit.jupiter.api.Assertions.*;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.DomainEvents;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록할 수 있다")
    @Test
    void create() {
    }

    @DisplayName("메뉴 그룹의 목록을 조회할 수 있다")
    @Test
    void list() {

    }
}
