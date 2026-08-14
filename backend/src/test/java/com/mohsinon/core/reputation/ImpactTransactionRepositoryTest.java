package com.mohsinon.core.reputation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ImpactTransactionRepositoryTest {

    @Autowired
    private ImpactTransactionRepository repository;

    @Test
    @DisplayName("Should save transactions and accurately sum user impact points balance")
    void shouldSaveAndSumUserImpactPoints() {
        UUID userId = UUID.randomUUID();

        // 1. User earns 100 points for volunteer work
        repository.save(ImpactTransaction.earned(
                userId,
                100,
                "VOLUNTEERING",
                UUID.randomUUID(),
                "Completed 4 hours of community logistics"
        ));

        // 2. User earns 50 points for verified food donation
        repository.save(ImpactTransaction.earned(
                userId,
                50,
                "DONATION",
                UUID.randomUUID(),
                "Delivered 10kg of staple food packages"
        ));

        // 3. User redeems 30 points
        repository.save(ImpactTransaction.spent(
                userId,
                30,
                "COMMUNITY_REWARD",
                UUID.randomUUID(),
                "Redeemed community reward voucher"
        ));

        int totalBalance = repository.sumPointsByUserId(userId);
        assertThat(totalBalance).isEqualTo(120); // 100 + 50 - 30 = 120

        Page<ImpactTransaction> history = repository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(0, 10));
        assertThat(history.getTotalElements()).isEqualTo(3);
        assertThat(history.getContent()).hasSize(3);
    }

    @Test
    @DisplayName("Should return zero balance for user with no transactions")
    void shouldReturnZeroForUserWithNoTransactions() {
        UUID unknownUser = UUID.randomUUID();
        int balance = repository.sumPointsByUserId(unknownUser);
        assertThat(balance).isEqualTo(0);
    }
}
