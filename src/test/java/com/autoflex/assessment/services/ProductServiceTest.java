package com.autoflex.assessment.services;

import com.autoflex.assessment.dtos.product.request.ProductCreateRequest;
import com.autoflex.assessment.dtos.product.request.ProductUpdateRequest;
import com.autoflex.assessment.dtos.product.response.ProductResponse;
import com.autoflex.assessment.dtos.product.response.ProductSummaryResponse;
import com.autoflex.assessment.dtos.product.response.ProductionReportResponse;
import com.autoflex.assessment.dtos.raw_material.response.RawMaterialResponse;
import com.autoflex.assessment.entities.ProductEntity;
import com.autoflex.assessment.entities.RawMaterialEntity;
import com.autoflex.assessment.enums.UnitType;
import com.autoflex.assessment.exceptions.*;
import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import java.awt.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class ProductServiceTest {

    @Inject
    ProductService productService;

    @InjectMock
    Session session;

    @InjectMock
    RawMaterialService rawMaterialService;

    UUID productId = UUID.randomUUID();
    String productCode = "P-001";
    String productName = "Suporte Metálico Industrial";

    @Nested
    class ReadOperationsTests {

        @BeforeEach
        void setup() {
            PanacheMock.mock(ProductEntity.class);
        }

        @Nested
        class ListTest {

            @Test
            void shouldReturnProductList() {
                ProductEntity product1 = new ProductEntity();
                product1.setCode(productCode);
                product1.setName(productName);
                product1.setPrice(BigDecimal.valueOf(185.70));

                ProductEntity product2 = new ProductEntity();
                product2.setCode("P-002");
                product2.setName("Painel Metálico Pintado");
                product2.setPrice(BigDecimal.valueOf(240.50));

                Mockito.when(ProductEntity.listAll()).thenReturn(List.of(product1, product2));

                var productList = productService.list();

                assertEquals(2, productList.size());

                assertEquals(productCode, productList.get(0).getCode());

                assertEquals("Painel Metálico Pintado", productList.get(1).getName());
                assertEquals(BigDecimal.valueOf(240.50), productList.get(1).getPrice());
            }
        }

        @Nested
        class FindByIdTests {

            @Test
            @SuppressWarnings("static-access")
            void shouldReturnProductWhenIdExists() {
                ProductEntity product = new ProductEntity();
                product.setCode(productCode);
                product.setName(productName);
                product.setPrice(BigDecimal.valueOf(185.70));

                Mockito.when(ProductEntity.findByIdOptional(product.getId())).thenReturn(Optional.of(product));

                ProductResponse productFound = productService.findById(product.getId());

                assertNotNull(productFound);
                assertEquals(productName, productFound.getName());

                PanacheMock.verify(ProductEntity.class).findByIdOptional(product.getId());
            }

            @Test
            @SuppressWarnings("static-access")
            void shouldThrowExceptionWhenProductNotFound() {
                Mockito.when(ProductEntity.findByIdOptional(productId)).thenReturn(Optional.empty());

                assertThrows(ProductNotFoundException.class, () -> {
                    productService.findById(productId);
                });

                PanacheMock.verify(ProductEntity.class).findByIdOptional(productId);
            }
        }

        @Nested
        class GetSummary {

            @Test
            void shouldReturnProductSummary() {
                PanacheMock.mock(ProductEntity.class);

                ProductSummaryResponse summary = new ProductSummaryResponse();
                summary.setTotal(10);
                summary.setActive(7);
                summary.setInactive(3);

                Mockito.when(ProductEntity.getSummary()).thenReturn(summary);

                ProductSummaryResponse result = productService.getSummary();

                assertEquals(10, result.getTotal());
                PanacheMock.verify(ProductEntity.class).getSummary();
            }
        }
    }

    @Nested
    class WriteOperationsTests {

        @BeforeEach
        void setup() {
            Mockito.doNothing().when(session).persist(Mockito.any());
            Mockito.doNothing().when(session).flush();

            PanacheMock.mock(ProductEntity.class);
        }

        @Nested
        class CreateTests {

            @Test
            void shouldCreateWithSuccess() {
                ProductCreateRequest product = new ProductCreateRequest();
                product.setCode(productCode);
                product.setName(productName);
                product.setPrice(BigDecimal.valueOf(185.70));

                Mockito.when(ProductEntity.existsByCode(productCode)).thenReturn(false);
                Mockito.when(ProductEntity.existsByName(productName)).thenReturn(false);

                productService.create(product);

                Mockito.verify(session, Mockito.atLeastOnce()).persist(Mockito.any(ProductEntity.class));
                Mockito.verify(session, Mockito.atLeastOnce()).flush();
            }

            @Test
            void shouldThrowExceptionWhenCodeAlreadyExists() {
                ProductCreateRequest product = new ProductCreateRequest();
                product.setCode(productCode);

                Mockito.when(ProductEntity.existsByCode(productCode)).thenReturn(true);

                assertThrows(CodeAlreadyInUseException.class, () -> {
                    productService.create(product);
                });

                PanacheMock.verify(ProductEntity.class, Mockito.never()).persistAndFlush();
            }

            @Test
            void shouldThrowExceptionWhenNameAlreadyExists() {
                ProductCreateRequest product = new ProductCreateRequest();
                product.setCode(productCode);
                product.setName(productName);

                Mockito.when(ProductEntity.existsByCode(productCode)).thenReturn(false);
                Mockito.when(ProductEntity.existsByName(productName)).thenReturn(true);

                assertThrows(NameAlreadyInUseException.class, () -> {
                    productService.create(product);
                });

                PanacheMock.verify(ProductEntity.class, Mockito.never()).persistAndFlush();
            }

            @Test
            void shouldThrowExceptionWhenRawMaterialNotFound() {
                UUID invalidRawMaterialId = UUID.randomUUID();

                ProductCreateRequest.RawMaterialQuantity materialItem = new ProductCreateRequest.RawMaterialQuantity();
                materialItem.setRawMaterialId(invalidRawMaterialId);
                materialItem.setQuantityNeeded(BigDecimal.TEN);

                List<ProductCreateRequest.RawMaterialQuantity> materialList = new ArrayList<>();
                materialList.add(materialItem);

                ProductCreateRequest product = new ProductCreateRequest();
                product.setCode(productCode);
                product.setName(productName);
                product.setMaterials(materialList);

                Mockito.when(rawMaterialService.findEntityById(invalidRawMaterialId)).thenReturn(null);

                Mockito.when(ProductEntity.existsByCode(productCode)).thenReturn(false);
                Mockito.when(ProductEntity.existsByName(productName)).thenReturn(false);

                assertThrows(RawMaterialNotFoundException.class, () -> {
                    productService.create(product);
                });

                Mockito.verify(rawMaterialService).findEntityById(invalidRawMaterialId);
                PanacheMock.verify(ProductEntity.class, Mockito.never()).persistAndFlush();
            }

            @Test
            void shouldThrowExceptionWhenRawMaterialAlreadyExists() {
                UUID rawMaterialId = UUID.randomUUID();

                ProductCreateRequest.RawMaterialQuantity materialItem = new ProductCreateRequest.RawMaterialQuantity();
                materialItem.setRawMaterialId(rawMaterialId);
                materialItem.setQuantityNeeded(BigDecimal.TEN);

                ProductCreateRequest product = new ProductCreateRequest();
                product.setCode(productCode);
                product.setName(productName);
                product.setMaterials(List.of(materialItem, materialItem));

                Mockito.when(ProductEntity.existsByCode(productCode)).thenReturn(false);
                Mockito.when(ProductEntity.existsByName(productName)).thenReturn(false);

                RawMaterialEntity rawMaterial = new RawMaterialEntity();
                rawMaterial.setId(rawMaterialId);

                Mockito.when(rawMaterialService.findEntityById(rawMaterialId)).thenReturn(rawMaterial);

                assertThrows(RawMaterialAlreadyExistsException.class, () -> {
                    productService.create(product);
                });
            }

            @Test
            void shouldAddRawMaterialsToProductDuringCreation() {
                UUID rawMaterialId = UUID.randomUUID();

                RawMaterialEntity rawMaterial = new RawMaterialEntity();
                rawMaterial.setId(rawMaterialId);
                rawMaterial.setName("Matéria-prima");
                rawMaterial.setCode("RM-001");
                rawMaterial.setStockQuantity(new BigDecimal("10.6"));
                rawMaterial.setUnitType(UnitType.KG);

                ProductCreateRequest.RawMaterialQuantity materialItem = new ProductCreateRequest.RawMaterialQuantity();
                materialItem.setRawMaterialId(rawMaterialId);
                materialItem.setQuantityNeeded(BigDecimal.TEN);

                List<ProductCreateRequest.RawMaterialQuantity> materialList = new ArrayList<>();
                materialList.add(materialItem);

                ProductCreateRequest product = new ProductCreateRequest();
                product.setCode(productCode);
                product.setName(productName);
                product.setPrice(new BigDecimal("185.70"));
                product.setMaterials(materialList);

                Mockito.when(ProductEntity.existsByCode(productCode)).thenReturn(false);
                Mockito.when(ProductEntity.existsByName(productName)).thenReturn(false);

                Mockito.when(rawMaterialService.findEntityById(rawMaterialId)).thenReturn(rawMaterial);

                ProductResponse createdProduct = productService.create(product);

                assertNotNull(createdProduct.getRawMaterials());
                assertEquals(1, createdProduct.getRawMaterials().size());
                assertEquals(rawMaterialId, createdProduct.getRawMaterials().get(0).getRawMaterialId());

                Mockito.verify(rawMaterialService).findEntityById(rawMaterialId);
                Mockito.verify(session, Mockito.atLeastOnce()).persist(Mockito.any(ProductEntity.class));
                Mockito.verify(session, Mockito.atLeastOnce()).flush();
            }
        }

        @Nested
        class CalculateProductionTests {

            @Test
            void shouldCalculateProductionBasedOnAvailableStock() {
                UUID ironId = UUID.randomUUID();
                String rawMaterialName = "Ferro";
                String rawMaterialCode = "RM-001";

                RawMaterialResponse iron = createRawMaterialResponse(ironId, rawMaterialName, new BigDecimal("10.00"));

                Mockito.when(rawMaterialService.list()).thenReturn(List.of(iron));

                RawMaterialEntity ironEntity = new RawMaterialEntity();
                ironEntity.setId(ironId);
                ironEntity.setName(rawMaterialName);
                ironEntity.setCode(rawMaterialCode);

                ProductEntity productEntity = new ProductEntity();
                productEntity.setId(productId);
                productEntity.setCode(productCode);
                productEntity.setName(productName);
                productEntity.setPrice(new BigDecimal("100.00"));

                productEntity.addRawMaterial(ironEntity, new BigDecimal("2.00"));

                PanacheMock.mock(ProductEntity.class);
                Mockito.when(ProductEntity.listAll()).thenReturn(List.of(productEntity));

                List<ProductionReportResponse> report = productService.calculateProduction();

                assertFalse(report.isEmpty());
                assertEquals(1, report.size());

                ProductionReportResponse firstProductReport = report.get(0);

                assertEquals(productId, firstProductReport.getProductId());
                assertEquals(5, firstProductReport.getProduceQuantity());
                assertEquals(new BigDecimal("500.00"), firstProductReport.getTotalValue());

                ProductionReportResponse.RawMaterial materialInfo = firstProductReport.getRawMaterials().get(0);

                assertEquals(new BigDecimal("10.00"), materialInfo.getInitialStock());
                assertEquals(new BigDecimal("10.00"), materialInfo.getConsumedQuantity());
                assertEquals(0, BigDecimal.ZERO.compareTo(materialInfo.getRemainingStock()));
            }

            @Test
            void shouldDecreaseVirtualStockForSubsequentProducts() {
                UUID woodId = UUID.randomUUID();

                UUID tableId = UUID.randomUUID();
                UUID chairId = UUID.randomUUID();

                RawMaterialResponse wood = new RawMaterialResponse();
                wood.setId(woodId);
                wood.setName("Madeira");
                wood.setStockQuantity(new BigDecimal("10"));

                Mockito.when(rawMaterialService.list()).thenReturn(List.of(wood));

                RawMaterialEntity woodEntity = new RawMaterialEntity();
                woodEntity.setId(woodId);
                woodEntity.setName("Madeira");

                ProductEntity tableEntity = new ProductEntity();
                tableEntity.setId(tableId);
                tableEntity.setName("Mesa");
                tableEntity.setPrice(new BigDecimal("50"));
                tableEntity.addRawMaterial(woodEntity, new BigDecimal("6"));

                ProductEntity chairEntity = new ProductEntity();
                chairEntity.setId(chairId);
                chairEntity.setName("Cadeira");
                chairEntity.setPrice(new BigDecimal("20"));
                chairEntity.addRawMaterial(woodEntity, new BigDecimal("2"));

                PanacheMock.mock(ProductEntity.class);
                Mockito.when(ProductEntity.listAll()).thenReturn(List.of(tableEntity, chairEntity));

                List<ProductionReportResponse> report = productService.calculateProduction();

                assertEquals(2, report.size());

                assertEquals("Mesa", report.get(0).getProductName());
                assertEquals(1, report.get(0).getProduceQuantity());
                assertEquals(new BigDecimal("50"), report.get(0).getTotalValue());

                assertEquals("Cadeira", report.get(1).getProductName());
                assertEquals(2, report.get(1).getProduceQuantity());
                assertEquals(new BigDecimal("40"), report.get(1).getTotalValue());
            }

            private RawMaterialResponse createRawMaterialResponse(UUID id, String name, BigDecimal stockQuantity) {
                RawMaterialResponse rawMaterial = new RawMaterialResponse();
                rawMaterial.setId(id);
                rawMaterial.setName(name);
                rawMaterial.setStockQuantity(stockQuantity);

                return rawMaterial;
            }
        }

        @Nested
        class UpdateTests {

            @Test
            void shouldUpdateWithSuccess() {
                String newProductCode = "NEW-CODE";
                String newProductName = "NEW-NAME";
                BigDecimal productPrice = BigDecimal.valueOf(100);

                ProductEntity existingProduct = new ProductEntity();
                existingProduct.setId(productId);
                existingProduct.setCode(productCode);
                existingProduct.setName(productName);
                existingProduct.setPrice(productPrice);

                ProductUpdateRequest newProductData = new ProductUpdateRequest();
                newProductData.setCode(newProductCode);
                newProductData.setName(newProductName);

                Mockito.when(ProductEntity.findByIdOptional(productId)).thenReturn(Optional.of(existingProduct));

                Mockito.when(ProductEntity.existsByCode(productCode)).thenReturn(false);
                Mockito.when(ProductEntity.existsByName(productName)).thenReturn(false);

                ProductResponse updatedProduct = productService.update(productId, newProductData);

                assertEquals(newProductCode, updatedProduct.getCode());
                assertEquals(newProductName, updatedProduct.getName());
                // Test the old price to see if the data has been partially updated
                assertEquals(productPrice, updatedProduct.getPrice());

                Mockito.verify(session).persist(existingProduct);
            }

            @Test
            void shouldSyncMaterialsCorrectly_AddingUpdatingAndRemoving() {
                UUID rawMaterialIdToKeep = UUID.randomUUID();
                UUID rawMaterialIdToRemove = UUID.randomUUID();
                UUID rawMaterialIdToCreate = UUID.randomUUID();

                ProductEntity existingProduct = new ProductEntity();
                existingProduct.setId(productId);

                RawMaterialEntity rawMaterialKeep = new RawMaterialEntity();
                rawMaterialKeep.setId(rawMaterialIdToKeep);

                RawMaterialEntity rawMaterialToRemove = new RawMaterialEntity();
                rawMaterialToRemove.setId(rawMaterialIdToRemove);

                existingProduct.addRawMaterial(rawMaterialKeep, BigDecimal.ONE);
                existingProduct.addRawMaterial(rawMaterialToRemove, BigDecimal.ONE);

                ProductUpdateRequest newProductData = new ProductUpdateRequest();

                ProductUpdateRequest.RawMaterialQuantity materialKeep = new ProductUpdateRequest.RawMaterialQuantity();
                materialKeep.setRawMaterialId(rawMaterialIdToKeep);
                materialKeep.setQuantityNeeded(BigDecimal.TEN);

                ProductUpdateRequest.RawMaterialQuantity materialToCreate = new ProductUpdateRequest.RawMaterialQuantity();
                materialToCreate.setRawMaterialId(rawMaterialIdToCreate);
                materialToCreate.setQuantityNeeded(BigDecimal.ONE);

                newProductData.setMaterials(List.of(materialKeep, materialToCreate));

                Mockito.when(ProductEntity.findByIdOptional(productId))
                        .thenReturn(Optional.of(existingProduct));

                RawMaterialEntity newRawMaterial = new RawMaterialEntity();
                newRawMaterial.setId(rawMaterialIdToCreate);

                Mockito.when(rawMaterialService.findEntityById(rawMaterialIdToKeep)).thenReturn(rawMaterialKeep);
                Mockito.when(rawMaterialService.findEntityById(rawMaterialIdToCreate)).thenReturn(newRawMaterial);

                productService.update(productId, newProductData);

                boolean wasRemoved = existingProduct.getMaterials().stream()
                                .anyMatch(material -> material.getRawMaterial()
                                .getId().equals(rawMaterialIdToRemove));

                assertFalse(wasRemoved);
                assertEquals(2, existingProduct.getMaterials().size());
                Mockito.verify(session).persist(existingProduct);
            }

            @Test
            void shouldThrowExceptionWhenProductNotFound() {
                Mockito.when(ProductEntity.findByIdOptional(productId)).thenReturn(Optional.empty());

                assertThrows(ProductNotFoundException.class, () -> {
                    productService.update(productId, new ProductUpdateRequest());
                });

                Mockito.verify(session, Mockito.never()).persist(any());

            }

            @Test
            void shouldThrowExceptionWhenUpdateCodeAlreadyInUse() {
                String existingCode = "EXISTING-CODE";

                ProductEntity existingProduct = new ProductEntity();
                existingProduct.setId(productId);

                ProductUpdateRequest newProductData = new ProductUpdateRequest();
                newProductData.setCode(existingCode);

                Mockito.when(ProductEntity.findByIdOptional(productId)).thenReturn(Optional.of(existingProduct));

                Mockito.when(ProductEntity.existsByCode(existingCode)).thenReturn(true);

                assertThrows(CodeAlreadyInUseException.class, () -> {
                    productService.update(productId, newProductData);
                });

                Mockito.verify(session, Mockito.never()).persist(any());
            }

            @Test
            void shouldThrowExceptionWhenUpdateNameAlreadyInUse() {
                String existingName = "EXISTING-NAME";

                ProductEntity existingProduct = new ProductEntity();
                existingProduct.setId(productId);

                ProductUpdateRequest newProductData = new ProductUpdateRequest();
                newProductData.setName(existingName);

                Mockito.when(ProductEntity.findByIdOptional(productId)).thenReturn(Optional.of(existingProduct));

                Mockito.when(ProductEntity.existsByCode(productCode)).thenReturn(false);
                Mockito.when(ProductEntity.existsByName(existingName)).thenReturn(true);

                assertThrows(NameAlreadyInUseException.class, () -> {
                    productService.update(productId, newProductData);
                });

                Mockito.verify(session, Mockito.never()).persist(any());
            }

            @Test
            void shouldThrowExceptionWhenCodeExceedsTheLimit() {
                ProductEntity existingProduct = new ProductEntity();
                existingProduct.setId(productId);

                ProductUpdateRequest newProductData = new ProductUpdateRequest();
                newProductData.setCode("a".repeat(21)); // 21

                Mockito.when(ProductEntity.findByIdOptional(productId)).thenReturn(Optional.of(existingProduct));

                assertThrows(ExceedsCodeLengthException.class, () ->
                        productService.update(productId, newProductData));

                Mockito.verify(session, Mockito.never()).persist(any());
            }

            @Test
            void shouldThrowExceptionWhenDescriptionExceedsTheLimit() {
                ProductEntity existingProduct = new ProductEntity();
                existingProduct.setId(productId);

                ProductUpdateRequest newProductData = new ProductUpdateRequest();
                newProductData.setDescription("a".repeat(501)); // 501

                Mockito.when(ProductEntity.findByIdOptional(productId)).thenReturn(Optional.of(existingProduct));

                assertThrows(ExceedsDescriptionLengthException.class, () ->
                        productService.update(productId, newProductData));

                Mockito.verify(session, Mockito.never()).persist(any());
            }

            @Test
            void shouldThrowExceptionWhenStockPriceIsInvalid() {
                ProductEntity existingProduct = new ProductEntity();
                existingProduct.setId(productId);

                ProductUpdateRequest newProductData = new ProductUpdateRequest();
                newProductData.setPrice(new BigDecimal("0.005"));

                Mockito.when(ProductEntity.findByIdOptional(productId)).thenReturn(Optional.of(existingProduct));

                assertThrows(MinimumPriceException.class, () ->
                        productService.update(productId, newProductData));

                Mockito.verify(session, Mockito.never()).persist(any());
            }
        }

        @Nested
        class DeleteTests {

            @BeforeEach
            void setup() {
                PanacheMock.mock(ProductEntity.class);
            }

            @Test
            void shouldDeleteWithSuccess() {
                ProductEntity existingProduct = new ProductEntity();
                existingProduct.setId(productId);

                Mockito.when(ProductEntity.findByIdOptional(productId))
                        .thenReturn(Optional.of(existingProduct));

                Mockito.when(ProductEntity.deleteById(productId)).thenReturn(true);

                productService.delete(productId);

                PanacheMock.verify(ProductEntity.class).deleteById(productId);
            }

            @Test
            void shouldThrowExceptionWhenDeletingNonExistentProduct() {
                Mockito.when(ProductEntity.findByIdOptional(productId)).thenReturn(Optional.empty());

                assertThrows(ProductNotFoundException.class, () -> {
                    productService.delete(productId);
                });

                PanacheMock.verify(ProductEntity.class, Mockito.never());
                ProductEntity.deleteById(Mockito.any());
            }
        }
    }
}
