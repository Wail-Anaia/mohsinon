package com.mohsinon.core.reputation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ImpactTransactionTest {

    @Test
    @DisplayName("Should create valid earned impact transaction")
    void shouldCreateEarnedTransaction() {
        UUID userId = UUID.randomUUID();
        UUID refId = UUID.randomUUID();

        ImpactTransaction tx = ImpactTransaction.earned(
                userId,
                50,
                "DONATION",
                refId,
                "Verified clothing donation delivered to local hub"
        );

        assertThat(tx.getUserId()).isEqualTo(userId);
        assertThat(tx.getType()).isEqualTo(ImpactTransactionType.EARNED);
        assertThat(tx.getPoints()).isEqualTo(50);
        assertThat(tx.getReferenceType()).isEqualTo("DONATION");
        assertThat(tx.getReferenceId()).isEqualTo(refId);
        assertThat(tx.getReason()).contains("Verified clothing donation");
    }

    @Test
    @DisplayName("Should create valid spent impact transaction with negative point balance")
    void shouldCreateSpentTransaction() {
        UUID userId = UUID.randomUUID();
        UUID refId = UUID.randomUUID();

        ImpactTransaction tx = ImpactTransaction.spent(
                userId,
                20,
                "COMMUNITY_REWARD",
                refId,
                "Redeemed community reward voucher"
        );

        assertThat(tx.getPoints()).isEqualTo(-20);
        assertThat(tx.getType()).isEqualTo(ImpactTransactionType.SPENT);
    }

    @Test
    @DisplayName("Should reject invalid parameters when creating impact transaction")
    void shouldRejectInvalidParameters() {
        UUID userId = UUID.randomUUID();

        assertThatThrownBy(() -> ImpactTransaction.earned(null, 50, "DONATION", null, "Reason"))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> ImpactTransaction.earned(userId, 0, "DONATION", null, "Reason"))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> ImpactTransaction.earned(userId, 50, "", null, "Reason"))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> ImpactTransaction.earned(userId, 50, "DONATION", null, " "))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
