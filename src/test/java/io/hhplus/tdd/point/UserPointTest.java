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
}