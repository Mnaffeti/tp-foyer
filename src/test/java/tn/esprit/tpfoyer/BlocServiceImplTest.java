package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.entity.Bloc;
import tn.esprit.tpfoyer.repository.BlocRepository;
import tn.esprit.tpfoyer.service.BlocServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BlocServiceImplTest {

    @Mock
    BlocRepository blocRepository;

    @InjectMocks
    BlocServiceImpl blocService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllBlocs() {
        Bloc b1 = new Bloc(1L, "A", 100, null, null);
        Bloc b2 = new Bloc(2L, "B", 150, null, null);
        when(blocRepository.findAll()).thenReturn(Arrays.asList(b1, b2));

        List<Bloc> blocs = blocService.retrieveAllBlocs();

        assertEquals(2, blocs.size());
        verify(blocRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveBlocsSelonCapacite() {
        Bloc b1 = new Bloc(1L, "X", 80, null, null);
        Bloc b2 = new Bloc(2L, "Y", 120, null, null);
        Bloc b3 = new Bloc(3L, "Z", 200, null, null);
        when(blocRepository.findAll()).thenReturn(Arrays.asList(b1, b2, b3));

        List<Bloc> result = blocService.retrieveBlocsSelonCapacite(100);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(b -> b.getCapaciteBloc() >= 100));
    }

    @Test
    void testRetrieveBlocById() {
        Bloc bloc = new Bloc(1L, "Bloc1", 100, null, null);
        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));

        Bloc result = blocService.retrieveBloc(1L);

        assertNotNull(result);
        assertEquals("Bloc1", result.getNomBloc());
    }

    @Test
    void testAddBloc() {
        Bloc bloc = new Bloc(0L, "Bloc2", 200, null, null);
        when(blocRepository.save(bloc)).thenReturn(bloc);

        Bloc result = blocService.addBloc(bloc);

        assertEquals("Bloc2", result.getNomBloc());
    }

    @Test
    void testModifyBloc() {
        Bloc bloc = new Bloc(1L, "BlocModified", 300, null, null);
        when(blocRepository.save(bloc)).thenReturn(bloc);

        Bloc result = blocService.modifyBloc(bloc);

        assertEquals("BlocModified", result.getNomBloc());
        assertEquals(300, result.getCapaciteBloc());
    }

    @Test
    void testRemoveBloc() {
        Long blocId = 1L;
        doNothing().when(blocRepository).deleteById(blocId);

        blocService.removeBloc(blocId);

        verify(blocRepository, times(1)).deleteById(blocId);
    }

    @Test
    void testBlocsSansFoyer() {
        Bloc bloc = new Bloc(1L, "SansFoyer", 100, null, null);
        when(blocRepository.findAllByFoyerIsNull()).thenReturn(List.of(bloc));

        List<Bloc> blocs = blocService.trouverBlocsSansFoyer();

        assertEquals(1, blocs.size());
        assertNull(blocs.get(0).getFoyer());
    }

    @Test
    void testBlocsParNomEtCapacite() {
        Bloc bloc = new Bloc(1L, "Alpha", 50, null, null);
        when(blocRepository.findAllByNomBlocAndCapaciteBloc("Alpha", 50))
                .thenReturn(List.of(bloc));

        List<Bloc> result = blocService.trouverBlocsParNomEtCap("Alpha", 50);

        assertEquals(1, result.size());
        assertEquals("Alpha", result.get(0).getNomBloc());
    }
}
