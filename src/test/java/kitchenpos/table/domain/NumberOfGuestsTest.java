package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class NumberOfGuestsTest {

    @Test
    @DisplayName("NumberOfGuests 생성 성공 테스트")
    public void createTest() throws Exception {
        //given
        int number = 5;

        //when
        NumberOfGuests actual = NumberOfGuests.create(number);

        //then
        assertEquals(actual.getValue(), number);
    }

    @Test
    @DisplayName("NumberOfGuests 생성 시 음수가 들어올 경우 예외 테스트")
    public void validateNegativeTest_WithNegativeValue() throws Exception {
        //given
        int number = -5;

        //then
        assertThatIllegalArgumentException().isThrownBy(() -> {
            NumberOfGuests.create(number);
        }).withMessage("손님은 0명 이상이어야 합니다.");
    }
}
