package com.main.repository;

import com.main.models.entities.HistorialCalculosEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("dev")
@TestPropertySource(properties = {
        "FeignApi.url=http://localhost:8080",
        "kafka.enabled=false"
})
class ICalculosRepositoryTest {

    @Autowired
    private ICalculosRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("findAllByOrderByFechaDesc - debe retornar lista ordenada por fecha descendente")
    void findAllByOrderByFechaDesc() {
        LocalDateTime fecha1 = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime fecha2 = LocalDateTime.of(2024, 1, 2, 10, 0);
        LocalDateTime fecha3 = LocalDateTime.of(2024, 1, 3, 10, 0);

        HistorialCalculosEntity entity1 = createEntity(fecha1, "/api/test1");
        HistorialCalculosEntity entity2 = createEntity(fecha2, "/api/test2");
        HistorialCalculosEntity entity3 = createEntity(fecha3, "/api/test3");

        entityManager.persist(entity1);
        entityManager.persist(entity2);
        entityManager.persist(entity3);
        entityManager.flush();

        List<HistorialCalculosEntity> result = repository.findAllByOrderByFechaDesc();

        assertEquals(3, result.size());
        assertEquals(fecha3, result.get(0).getFecha());
        assertEquals(fecha2, result.get(1).getFecha());
        assertEquals(fecha1, result.get(2).getFecha());
    }

    @Test
    @DisplayName("findAllByOrderByFechaDesc - debe retornar lista vacía cuando no hay datos")
    void findAllByOrderByFechaDesc_ListaVacia() {
        List<HistorialCalculosEntity> result = repository.findAllByOrderByFechaDesc();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("save - debe guardar entidad correctamente")
    void save() {
        HistorialCalculosEntity entity = createEntity(LocalDateTime.now(), "/api/save");

        HistorialCalculosEntity saved = repository.save(entity);

        assertNotNull(saved.getId());
        assertEquals("/api/save", saved.getEndpoint());
    }

    @Test
    @DisplayName("findById - debe encontrar entidad por ID")
    void findById() {
        HistorialCalculosEntity entity = createEntity(LocalDateTime.now(), "/api/findById");
        HistorialCalculosEntity saved = entityManager.persist(entity);
        entityManager.flush();

        Optional<HistorialCalculosEntity> result = repository.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals(saved.getId(), result.get().getId());
    }

    @Test
    @DisplayName("findById - debe retornar empty cuando no existe")
    void findById_NoExiste() {
        Optional<HistorialCalculosEntity> result = repository.findById(999L);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("findAll - debe retornar todas las entidades")
    void findAll() {
        entityManager.persist(createEntity(LocalDateTime.now(), "/api/test1"));
        entityManager.persist(createEntity(LocalDateTime.now(), "/api/test2"));
        entityManager.flush();

        List<HistorialCalculosEntity> result = repository.findAll();

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("delete - debe eliminar entidad")
    void delete() {
        HistorialCalculosEntity entity = createEntity(LocalDateTime.now(), "/api/delete");
        HistorialCalculosEntity saved = entityManager.persist(entity);
        entityManager.flush();

        repository.delete(saved);

        Optional<HistorialCalculosEntity> result = repository.findById(saved.getId());
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("count - debe contar entidades")
    void count() {
        entityManager.persist(createEntity(LocalDateTime.now(), "/api/test1"));
        entityManager.persist(createEntity(LocalDateTime.now(), "/api/test2"));
        entityManager.persist(createEntity(LocalDateTime.now(), "/api/test3"));
        entityManager.flush();

        long count = repository.count();

        assertEquals(3, count);
    }

    @Test
    @DisplayName("existsById - debe retornar true si existe")
    void existsById() {
        HistorialCalculosEntity entity = createEntity(LocalDateTime.now(), "/api/exists");
        HistorialCalculosEntity saved = entityManager.persist(entity);
        entityManager.flush();

        boolean exists = repository.existsById(saved.getId());

        assertTrue(exists);
    }

    @Test
    @DisplayName("existsById - debe retornar false si no existe")
    void existsById_NoExiste() {
        boolean exists = repository.existsById(999L);

        assertFalse(exists);
    }

    @Test
    @DisplayName("findAllByOrderByFechaDesc - con una sola entidad")
    void findAllByOrderByFechaDesc_UnaSolaEntidad() {
        HistorialCalculosEntity entity = createEntity(LocalDateTime.now(), "/api/single");
        entityManager.persist(entity);
        entityManager.flush();

        List<HistorialCalculosEntity> result = repository.findAllByOrderByFechaDesc();

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("save - debe actualizar entidad existente")
    void save_ActualizarEntidad() {
        HistorialCalculosEntity entity = createEntity(LocalDateTime.now(), "/api/original");
        HistorialCalculosEntity saved = entityManager.persist(entity);
        entityManager.flush();

        saved.setEndpoint("/api/updated");
        HistorialCalculosEntity updated = repository.save(saved);

        assertEquals("/api/updated", updated.getEndpoint());
    }

    private HistorialCalculosEntity createEntity(LocalDateTime fecha, String endpoint) {
        return HistorialCalculosEntity.builder()
                .fecha(fecha)
                .endpoint(endpoint)
                .parametros("numero1=100&numero2=50")
                .respuesta("{\"resultado\": 150}")
                .error(false)
                .mensajeError(null)
                .build();
    }
}
