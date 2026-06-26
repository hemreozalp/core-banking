package com.example.core_banking.audit;

import com.example.core_banking.dto.MoneyTransferRequest;
import com.example.core_banking.audit.AuditLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AuditLoggingAspect {

    private final AuditLogRepository auditLogRepository;

    @AfterReturning(
            pointcut = "within(com.example.core_banking.service..*) && args(request)",
            returning = "result"
    )
    public void logMoneyTransfer(MoneyTransferRequest request, Object result) {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = (authentication != null) ? authentication.getName() : "SYSTEM/ANONYMOUS";

            AuditLog auditLog = getAuditLog(request, username);

            auditLogRepository.save(auditLog);

            log.info("[AUDIT LOG] Action: TRANSFER_SUCCESS, PerformedBy: {}", username);

        } catch (Exception e) {
            log.error("Audit log kaydedilirken hata oluştu: {}", e.getMessage());
        }
    }

    private static AuditLog getAuditLog(MoneyTransferRequest request, String username) {
        String logDetails = String.format(
                "Para Transferi Başarılı: %s kaynaklı hesaptan %s alıcılı hesaba %s TRY transfer edildi. Açıklama: %s",
                request.sourceIban(),
                request.targetIban(),
                request.amount(),
                (request.description() != null ? request.description() : "-")
        );

        AuditLog auditLog = new AuditLog();
        auditLog.setAction("TRANSFER_SUCCESS");
        auditLog.setDetails(logDetails);
        auditLog.setPerformedBy(username);
        return auditLog;
    }
}