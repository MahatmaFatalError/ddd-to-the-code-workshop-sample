package com.github.cstettler.dddttc.accounting.domain;

import com.github.cstettler.dddttc.stereotype.DomainEventHandler;
import com.github.cstettler.dddttc.stereotype.DomainService;

import java.math.BigDecimal;

import static com.github.cstettler.dddttc.accounting.domain.Amount.amount;
import static com.github.cstettler.dddttc.accounting.domain.TransactionReference.transactionReference;

@DomainService
class ChargeWelcomeAmountToWalletService {

    private final WalletRepository walletRepository;
    private final Amount welcomeAmount;

    ChargeWelcomeAmountToWalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
        this.welcomeAmount = amount(new BigDecimal("5.00"));
    }

    @DomainEventHandler
    void chargeWelcomeAmountToWallet(WalletInitializedEvent event) {
        try {
            UserId walletId = event.walletId();
            TransactionReference welcomeAmountTransactionReference = transactionReference("welcome-" + walletId.value());

            Wallet wallet = this.walletRepository.get(walletId);
            wallet.chargeAmount(this.welcomeAmount, welcomeAmountTransactionReference);

            this.walletRepository.update(wallet);
        } catch (TransactionWithSameReferenceAlreadyAppliedException e) {
            // ignored
        }
    }

}
