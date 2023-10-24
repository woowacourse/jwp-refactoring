package kitchenpos.application;

import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.OrderTableCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    public void setUp() {
        // 필요한 초기화 작업 수행
    }

    @Test
    @DisplayName("테이블을 생성한다.")
    void createTable() {
        //given
        OrderTableCreateRequest request = new OrderTableCreateRequest(4, true);

        //when
        Long createdTableId = tableService.create(request);
        OrderTable savedTable = orderTableRepository.findById(createdTableId).get();

        //then
        assertThat(createdTableId).isNotNull();
        assertThat(savedTable).isNotNull();
        assertThat(savedTable.getNumberOfGuests()).isEqualTo(4);
        assertThat(savedTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("테이블의 인원수변경할 때 잘못된 숫자를 입력 시 예외")
    void changeNumberOfGuests_InvalidRequest_ShouldThrowException() {
        // Arrange
        OrderTableCreateRequest request = new OrderTableCreateRequest(4, true);
        Long tableId = tableService.create(request);

        // Act & Assert
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(tableId, -1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
