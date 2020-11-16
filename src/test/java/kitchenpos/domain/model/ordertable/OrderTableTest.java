package kitchenpos.domain.model.ordertable;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderTableTest {
    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        orderTable1 = new OrderTable(1L, null, 0, false);
        orderTable2 = new OrderTable(2L, 1L, 0, true);
    }

    @DisplayName("테이블 손님 여부 변경")
    @Test
    void changeEmpty() {
        OrderTable orderTable = new OrderTable(null, null, 0, true);
        orderTable.changeEmpty(false);

        assertThat(orderTable.isEmpty()).isFalse();
    }

    @DisplayName("테이블 손님 수 변경")
    @TestFactory
    Stream<DynamicTest> changeNumberOfGuests() {
        return Stream.of(
                dynamicTest("손님 수 변경 성공", () -> {
                    int numberOfGuests = orderTable1.getNumberOfGuests();
                    orderTable1.changeNumberOfGuests(orderTable1.getNumberOfGuests() + 1);

                    assertThat(orderTable1.getNumberOfGuests()).isEqualTo(numberOfGuests + 1);
                }),
                dynamicTest("빈 테이블에 손님 수 변경을 요청할때 IllegalArgumentException 발생", () -> {
                    assertThatIllegalArgumentException()
                            .isThrownBy(() -> orderTable2.changeNumberOfGuests(
                                    orderTable2.getNumberOfGuests() + 1));
                })
        );
    }

    @DisplayName("단체 지정 생성")
    @Test
    void makeTableGroup() {
        Long tableGroupId = 1L;
        OrderTable orderTable = new OrderTable(null, null, 0, true);
        orderTable.makeTableGroup(tableGroupId);

        assertAll(
                () -> assertThat(orderTable.getTableGroupId()).isEqualTo(tableGroupId),
                () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }
}