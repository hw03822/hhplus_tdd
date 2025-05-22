package io.hhplus.tdd.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserPointTest {
    @Test
    @DisplayName("포인트 누적 값을 계산한다.")
    void calculate_sumPoint() {
        //given
        long id = 1L;

        //시나리오 : 100L 포인트가 있는데 200L를 충전했을 때 누적 값을 계산
        UserPoint userPoint = new UserPoint(id, 100L, System.currentTimeMillis());

        //when
        long point = userPoint.charge(200L).point(); // 결과 : 300L

        //then
        assertThat(point).isEqualTo(300L);
    }

    @Test
    @DisplayName("처음 포인트 충전이면 충전값을 반환한다.")
    void chargePoint() {
        //given
        //시나리오 : 처음 100L 포인트를 충전했을 때 100L 포인트 반환
        UserPoint userPoint = UserPoint.empty(1L);

        //when
        long point = userPoint.charge(100L).point(); // 결과 : 100L

        //then
        assertThat(point).isEqualTo(100L);
    }

    @Test
    @DisplayName("0보다 작은 값 충전시 예외처리한다.")
    void chargeMinusPoint_Exception() {
        //given
        //시나리오 : 0보다 작은 -100L 포인트를 충전시도 했을 때 예외 반환
        UserPoint userPoint = UserPoint.empty(1L);

        //when

        //then
        assertThrows(IllegalArgumentException.class, () -> userPoint.charge(-100L).point());
    }

    @Test
    @DisplayName("포인트 사용하면 나머지 값을 반환한다.")
    void usePoint_ReturnRemainder() {
        //given
        long id = 1L;

        //시나리오 : 처음 300L 포인트를 있을 때 200L 포인트 사용 -> 나머지 값 100L 반환
        UserPoint userPoint = new UserPoint(id, 300L, System.currentTimeMillis());

        //when
        long point = userPoint.use(200L).point(); // 결과 : 100L

        // then
        assertThat(point).isEqualTo(100L);
    }

    @Test
    @DisplayName("포인트가 0이거나 사용할 값보다 작으면 예외처리한다.")
    void useMinusPoint_ReturnException() {
        //given
        //시나리오 : 100L 포인트가 있을 때 200L 포인트 사용 -> -100L 이 되므로 예외처리
        UserPoint userPoint1 = new UserPoint(1L, 100L, System.currentTimeMillis());

        //시나리오 : 0L 포인트가 있을 때 50L 포인트 사용 -> -50L 이 되므로 예외처리
        UserPoint userPoint2 = new UserPoint(2L, 0L, System.currentTimeMillis());

        //when


        // then
        assertThrows(IllegalArgumentException.class, () -> userPoint1.use(200L).point());
        assertThrows(IllegalArgumentException.class, () -> userPoint2.use(50L).point());
    }
}