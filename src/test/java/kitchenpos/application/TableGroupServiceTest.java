package kitchenpos.application;

import kitchenpos.dao.TableGroupDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("단체 지정을 등록할 수 있다")
    @Test
    void create() {
    }

    @DisplayName("주문 테이블이 비어있으면 안 된다")
    @Test
    void createExceptionEmpty() {
    }

    @DisplayName("주문 테이블의 목록이 2 보다 작으면 안 된다")
    @Test
    void createExceptionUnderTwo() {
    }

    @DisplayName("주문 테이블로 등록되어 있는 목록과 단체 주문에 있는 주문 테이블 목록과 크기가 같아야 한다")
    @Test
    void createExceptionTablesSize() {
    }

    @DisplayName("등록되어 있는 주문 테이블이 비어 있고, 주문 테이블의 그룹 지정이 있어야 한다")
    @Test
    void createExceptionEmptyAndGroup() {
    }

    @DisplayName("단체 지정을 해제할 수 있다")
    @Test
    void ungroup() {
    }

    @DisplayName("단체 지정의 주문 테이블의 주문이 있고, 주문 상태가 `COOKING`, `MEAL` 가 아니면 예외가 발생한다")
    @Test
    void ungroupExceptionExistsAndStatus() {
    }
}
