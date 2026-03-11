package com.forgeplan.services;

import com.forgeplan.dtos.raw_material.request.RawMaterialCreateRequest;
import com.forgeplan.dtos.raw_material.request.RawMaterialUpdateRequest;
import com.forgeplan.dtos.raw_material.response.RawMaterialResponse;
import com.forgeplan.entities.ProductMaterialEntity;
import com.forgeplan.entities.RawMaterialEntity;
import com.forgeplan.enums.UnitType;
import com.forgeplan.exceptions.*;
import com.forgeplan.exceptions.*;
import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
public class RawMaterialServiceTest {

    @Inject
    RawMaterialService rawMaterialService;

    @InjectMock
    Session session;

    UUID rawMaterialId = UUID.randomUUID();
    String rawMaterialCode = "RM-001";
    String rawMaterialName = "Aço Galvanizado";

    @BeforeEach
    void setup() {
        PanacheMock.mock(RawMaterialEntity.class);
    }

    @Nested
    class ReadOperationsTests {

        @Nested
        class ListTest {

            @Test
            void shouldReturnRawMaterialList() {
                RawMaterialEntity rawMaterial1 = new RawMaterialEntity();
                rawMaterial1.setCode(rawMaterialCode);
                rawMaterial1.setName(rawMaterialName);

                RawMaterialEntity rawMaterial2 = new RawMaterialEntity();
                rawMaterial2.setCode("RM-002");
                rawMaterial2.setName("Ferro");

                Mockito.when(RawMaterialEntity.listAll()).thenReturn(List.of(rawMaterial1, rawMaterial2));

                List<RawMaterialResponse> rawMaterialList = rawMaterialService.list();

                assertEquals(2, rawMaterialList.size());
                assertEquals(rawMaterialCode, rawMaterialList.get(0).getCode());
                assertEquals("Ferro", rawMaterialList.get(1).getName());
            }
        }

        @Nested
        class FindByIdTests {

            @Test
            void shouldReturnByIdWhenExists() {
                RawMaterialEntity rawMaterial = new RawMaterialEntity();
                rawMaterial.setId(rawMaterialId);
                rawMaterial.setName(rawMaterialName);
                rawMaterial.setCode(rawMaterialCode);

                Mockito.when(RawMaterialEntity.findByIdOptional(rawMaterialId)).thenReturn(Optional.of(rawMaterial));

                RawMaterialResponse rawMaterialFound = rawMaterialService.findById(rawMaterialId);

                assertNotNull(rawMaterialFound);
                assertEquals(rawMaterialName, rawMaterialFound.getName());
            }

            @Test
            void shouldThrowExceptionWhenNotFound() {
                Mockito.when(RawMaterialEntity.findByIdOptional(rawMaterialId)).thenReturn(Optional.empty());

                assertThrows(RawMaterialNotFoundException.class, () ->
                    rawMaterialService.findById(rawMaterialId)
                );
            }
        }
    }

    @Nested
    class WriteOperationsTests {

        @BeforeEach
        void setup() {
            Mockito.doNothing().when(session).persist(Mockito.any());
            Mockito.doNothing().when(session).flush();

            PanacheMock.mock(RawMaterialEntity.class);
        }

        @Nested
        class CreateTests {
            @Test
            void shouldCreateWithSuccess() {
                RawMaterialCreateRequest request = new RawMaterialCreateRequest();
                request.setCode(rawMaterialCode);
                request.setName(rawMaterialName);
                request.setStockQuantity(BigDecimal.TEN);
                request.setUnitType(UnitType.KG);

                Mockito.when(RawMaterialEntity.existsByCode(rawMaterialCode)).thenReturn(false);
                Mockito.when(RawMaterialEntity.existsByName(rawMaterialName)).thenReturn(false);

                rawMaterialService.create(request);

                Mockito.verify(session, Mockito.atLeastOnce()).persist(Mockito.any(RawMaterialEntity.class));
                Mockito.verify(session, Mockito.atLeastOnce()).flush();
            }

            @Test
            void shouldThrowExceptionWhenCodeAlreadyInUse() {
                RawMaterialCreateRequest request = new RawMaterialCreateRequest();
                request.setCode(rawMaterialCode);

                Mockito.when(RawMaterialEntity.existsByCode(rawMaterialCode)).thenReturn(true);

                assertThrows(CodeAlreadyInUseException.class, () -> rawMaterialService.create(request));
            }

            @Test
            void shouldThrowExceptionWhenNameAlreadyInUse() {
                RawMaterialCreateRequest request = new RawMaterialCreateRequest();
                request.setCode(rawMaterialCode);
                request.setName(rawMaterialName);

                Mockito.when(RawMaterialEntity.existsByCode(rawMaterialCode)).thenReturn(false);
                Mockito.when(RawMaterialEntity.existsByName(rawMaterialName)).thenReturn(true);

                assertThrows(NameAlreadyInUseException.class, () -> rawMaterialService.create(request));
            }
        }

        @Nested
        class UpdateTests {

            @Test
            void shouldUpdateWithSuccess() {
                String newRawMaterialName = "NEW-NAME";
                BigDecimal newRawMaterialStockQuantity = new BigDecimal("10.00");

                RawMaterialEntity existingRawMaterial = new RawMaterialEntity();
                existingRawMaterial.setId(rawMaterialId);
                existingRawMaterial.setCode(rawMaterialCode);
                existingRawMaterial.setName(rawMaterialName);
                existingRawMaterial.setStockQuantity(new BigDecimal("6.00"));

                RawMaterialUpdateRequest newRawMaterialData = new RawMaterialUpdateRequest();
                newRawMaterialData.setName(newRawMaterialName);
                newRawMaterialData.setStockQuantity(newRawMaterialStockQuantity);

                Mockito.when(RawMaterialEntity.findByIdOptional(rawMaterialId)).thenReturn(Optional.of(existingRawMaterial));

                RawMaterialResponse updatedRawMaterial = rawMaterialService.update(rawMaterialId, newRawMaterialData);

                assertEquals(newRawMaterialName, updatedRawMaterial.getName());
                assertEquals(newRawMaterialStockQuantity, updatedRawMaterial.getStockQuantity());
                assertEquals(rawMaterialCode, updatedRawMaterial.getCode());
            }

            @Test
            void shouldThrowExceptionWhenRawMaterialNotFound() {
                Mockito.when(RawMaterialEntity.findByIdOptional(rawMaterialId)).thenReturn(Optional.empty());

                assertThrows(RawMaterialNotFoundException.class, () -> {
                    rawMaterialService.update(rawMaterialId, new RawMaterialUpdateRequest());
                });

                Mockito.verify(session, Mockito.never()).persist(any());
            }

            @Test
            void shouldThrowExceptionWhenStockQuantityIsInvalid() {
                RawMaterialEntity existingRawMaterial = new RawMaterialEntity();
                existingRawMaterial.setId(rawMaterialId);

                RawMaterialUpdateRequest newRawMaterialData = new RawMaterialUpdateRequest();
                newRawMaterialData.setStockQuantity(new BigDecimal("0.005"));

                Mockito.when(RawMaterialEntity.findByIdOptional(rawMaterialId)).thenReturn(Optional.of(existingRawMaterial));

                assertThrows(MinimumStockQuantityException.class, () ->
                        rawMaterialService.update(rawMaterialId, newRawMaterialData));

                Mockito.verify(session, Mockito.never()).persist(any());
            }

            @Test
            void shouldThrowExceptionWhenUpdateCodeAlreadyInUse() {
                String newRawMaterialCode = "NEW-CODE";

                RawMaterialEntity existingRawMaterial = new RawMaterialEntity();
                existingRawMaterial.setId(rawMaterialId);
                existingRawMaterial.setCode(rawMaterialCode);
                existingRawMaterial.setName(rawMaterialName);
                existingRawMaterial.setStockQuantity(new BigDecimal("10.00"));

                RawMaterialUpdateRequest newRawMaterialData = new RawMaterialUpdateRequest();
                newRawMaterialData.setCode(newRawMaterialCode);
                newRawMaterialData.setStockQuantity(new BigDecimal("1.00"));

                Mockito.when(RawMaterialEntity.findByIdOptional(rawMaterialId)).thenReturn(Optional.of(existingRawMaterial));
                Mockito.when(RawMaterialEntity.existsByCode(newRawMaterialCode)).thenReturn(true);

                assertThrows(CodeAlreadyInUseException.class, () ->
                        rawMaterialService.update(rawMaterialId, newRawMaterialData));

                Mockito.verify(session, Mockito.never()).persist(any());
            }

            @Test
            void shouldThrowExceptionWhenUpdateNameAlreadyInUse() {
                String newRawMaterialName = "NEW-NAME";

                RawMaterialEntity existingRawMaterial = new RawMaterialEntity();
                existingRawMaterial.setId(rawMaterialId);
                existingRawMaterial.setCode(rawMaterialCode);
                existingRawMaterial.setName(rawMaterialName);
                existingRawMaterial.setStockQuantity(new BigDecimal("10.00"));

                RawMaterialUpdateRequest newRawMaterialData = new RawMaterialUpdateRequest();
                newRawMaterialData.setName(newRawMaterialName);
                newRawMaterialData.setStockQuantity(new BigDecimal("9.00"));

                Mockito.when(RawMaterialEntity.findByIdOptional(rawMaterialId)).thenReturn(Optional.of(existingRawMaterial));
                Mockito.when(RawMaterialEntity.existsByCode(newRawMaterialName)).thenReturn(false);
                Mockito.when(RawMaterialEntity.existsByName(newRawMaterialName)).thenReturn(true);

                assertThrows(NameAlreadyInUseException.class, () ->
                        rawMaterialService.update(rawMaterialId, newRawMaterialData));

                Mockito.verify(session, Mockito.never()).persist(any());
            }

            @Test
            void shouldThrowExceptionWhenCodeExceedsTheLimit() {
                RawMaterialEntity existingRawMaterial = new RawMaterialEntity();
                existingRawMaterial.setId(rawMaterialId);

                RawMaterialUpdateRequest newRawMaterialData = new RawMaterialUpdateRequest();
                newRawMaterialData.setCode("a".repeat(21)); // 21

                Mockito.when(RawMaterialEntity.findByIdOptional(rawMaterialId)).thenReturn(Optional.of(existingRawMaterial));

                assertThrows(ExceedsCodeLengthException.class, () ->
                        rawMaterialService.update(rawMaterialId, newRawMaterialData));

                Mockito.verify(session, Mockito.never()).persist(any());
            }
        }

        @Nested
        class DeleteTests {

            @BeforeEach
            void setup() {
                PanacheMock.mock(RawMaterialEntity.class);
                PanacheMock.mock(ProductMaterialEntity.class);
            }

            @Test
            void shouldDeleteWithSuccess() {
                RawMaterialEntity existingRawMaterial = new RawMaterialEntity();
                existingRawMaterial.setId(rawMaterialId);

                Mockito.when(RawMaterialEntity.findByIdOptional(rawMaterialId)).thenReturn(Optional.of(existingRawMaterial));
                Mockito.when(ProductMaterialEntity.count("rawMaterial.id", rawMaterialId)).thenReturn(0L);
                Mockito.when(RawMaterialEntity.deleteById(rawMaterialId)).thenReturn(true);

                rawMaterialService.delete(rawMaterialId);

                PanacheMock.verify(RawMaterialEntity.class).deleteById(rawMaterialId);
            }

            @Test
            void shouldThrowExceptionWhenRawMaterialNotFound() {
                Mockito.when(RawMaterialEntity.findByIdOptional(rawMaterialId)).thenReturn(Optional.empty());
                Mockito.when(ProductMaterialEntity.count("rawMaterial.id", rawMaterialId)).thenReturn(0L);

                assertThrows(RawMaterialNotFoundException.class, () -> rawMaterialService.delete(rawMaterialId));
            }

            @Test
            void shouldThrowExceptionWhenRawMaterialAlreadyInUse() {
                RawMaterialEntity existingRawMaterial = new RawMaterialEntity();
                existingRawMaterial.setId(rawMaterialId);

                Mockito.when(RawMaterialEntity.findByIdOptional(rawMaterialId)).thenReturn(Optional.of(existingRawMaterial));
                Mockito.when(ProductMaterialEntity.count("rawMaterial.id", rawMaterialId)).thenReturn(1L);

                assertThrows(RawMaterialInUseException.class, () -> rawMaterialService.delete(rawMaterialId));
            }
        }
    }
}