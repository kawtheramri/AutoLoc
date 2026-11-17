package tn.esprit.autoloc;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.service.IPaiementService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@Disabled("Tests des anciens ateliers")
class PaiementTests {

    @Autowired
    private IPaiementService paiementService;

    @Test
    void findAllPaiements() {
        List<Paiement> paiements = paiementService.findAll();

        fail("Paiements : " + paiements.size());
    }

    @Test
    void findPaiementParId() {
        Paiement paiement = paiementService.findById(1L);

        fail("Paiement : " + paiement.getIdPaiement());
    }

    @Test
    void findPaiementInexistant() {
        assertThrows(ResourceNotFoundException.class, () -> paiementService.findById(9999L));
    }
}
