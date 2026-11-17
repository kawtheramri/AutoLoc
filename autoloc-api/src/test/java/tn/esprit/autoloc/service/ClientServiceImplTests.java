package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IClientRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTests {

    private static final long ID_CLIENT = 5L;

    @Mock
    private IClientRepository clientRepository;

    private ClientServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ClientServiceImpl(clientRepository);
    }

    private Client clientValide() {
        return Client.builder()
                .nom("Ben Ali")
                .prenom("Sami")
                .email("sami.benali@mail.tn")
                .telephone("71234567")
                .numPermis("95123456")
                .build();
    }

    @Test
    void createAvecClientValide() {
        Client client = clientValide();
        when(clientRepository.save(client)).thenReturn(client);

        assertSame(client, service.create(client));
        verify(clientRepository).save(client);
    }

    @Test
    void createAvecIdentifiantRefuse() {
        Client client = clientValide();
        client.setIdClient(ID_CLIENT);

        assertThrows(IllegalArgumentException.class, () -> service.create(client));
        verify(clientRepository, never()).save(any());
    }

    @Test
    void createAvecEmailManquantEstRefuse() {
        Client client = clientValide();
        client.setEmail(null);

        assertThrows(IllegalArgumentException.class, () -> service.create(client));
        verify(clientRepository, never()).save(any());
    }

    @Test
    void findByIdAvecClientExistant() {
        Client client = clientValide();
        when(clientRepository.findById(ID_CLIENT)).thenReturn(Optional.of(client));

        assertSame(client, service.findById(ID_CLIENT));
    }

    @Test
    void findByIdIntrouvableLeveResourceNotFound() {
        when(clientRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(999L));
    }

    @Test
    void updateAvecClientValide() {
        Client existant = clientValide();
        existant.setIdClient(ID_CLIENT);
        Client modifie = clientValide();
        modifie.setEmail("nouveau@mail.tn");
        when(clientRepository.findById(ID_CLIENT)).thenReturn(Optional.of(existant));
        when(clientRepository.save(existant)).thenReturn(existant);

        Client updated = service.update(ID_CLIENT, modifie);

        assertEquals("nouveau@mail.tn", existant.getEmail());
        assertSame(existant, updated);
    }

    @Test
    void deleteByIdAvecClientExistant() {
        when(clientRepository.existsById(ID_CLIENT)).thenReturn(true);

        assertDoesNotThrow(() -> service.deleteById(ID_CLIENT));
        verify(clientRepository).deleteById(ID_CLIENT);
    }
}
