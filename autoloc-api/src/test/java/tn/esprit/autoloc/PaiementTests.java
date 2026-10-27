package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.ModePaiement;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.service.IPaiementService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class PaiementTests {

    @Autowired
    private IPaiementService paiementService;

    private Paiement paiementLectureSeule() {
        return Paiement.builder()
                .montant(new BigDecimal("150"))
                .datePaiement(LocalDate.now())
                .modePaiement(ModePaiement.CARTE)
                .build();
    }

    @Test
    public void createPaiementEstLectureSeule() {
        Paiement paiement = paiementLectureSeule();

        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> paiementService.create(paiement)
        );

        assertEquals("Le paiement est en lecture seule", exception.getMessage());
    }

    @Test
    public void updatePaiementEstLectureSeule() {
        Paiement paiement = paiementLectureSeule();

        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> paiementService.update(1L, paiement)
        );

        assertEquals("Le paiement est en lecture seule", exception.getMessage());
    }

    @Test
    public void deletePaiementEstLectureSeule() {
        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> paiementService.deleteById(1L)
        );

        assertEquals("Le paiement est en lecture seule", exception.getMessage());
    }
}
