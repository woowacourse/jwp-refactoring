package kitchenpos.common;

import java.math.BigDecimal;

public class Constants {

    public static String 야채곱창_이름 = "야채곱창";
    public static String 루나세트_이름 = "루나세트";

    public static long 야채곱창_수량 = 1;
    public static long 메뉴_상품_수량 = 1;

    public static BigDecimal 야채곱창_가격 = BigDecimal.valueOf(10000);
    public static BigDecimal 잘못된_가격 = BigDecimal.valueOf(-1);

    public static int 테이블_손님_수 = 4;

    public static boolean 사용중인_테이블 = false;
    public static boolean 사용가능_테이블 = true;
}
