package ai.turintech.catalog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import ai.turintech.catalog.IntegrationTest;
import ai.turintech.catalog.domain.ModelType;
import ai.turintech.catalog.repository.EntityManager;
import ai.turintech.catalog.repository.ModelTypeRepository;
import ai.turintech.catalog.service.dto.ModelTypeDTO;
import ai.turintech.catalog.service.mapper.ModelTypeMapper;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link ModelTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ModelTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/model-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ModelTypeRepository modelTypeRepository;

    @Autowired
    private ModelTypeMapper modelTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ModelType modelType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ModelType createEntity(EntityManager em) {
        ModelType modelType = new ModelType().id(UUID.randomUUID()).name(DEFAULT_NAME);
        return modelType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ModelType createUpdatedEntity(EntityManager em) {
        ModelType modelType = new ModelType().id(UUID.randomUUID()).name(UPDATED_NAME);
        return modelType;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ModelType.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        modelType = createEntity(em);
    }

    @Test
    void createModelType() throws Exception {
        int databaseSizeBeforeCreate = modelTypeRepository.findAll().collectList().block().size();
        modelType.setId(null);
        // Create the ModelType
        ModelTypeDTO modelTypeDTO = modelTypeMapper.toDto(modelType);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelTypeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ModelType in the database
        List<ModelType> modelTypeList = modelTypeRepository.findAll().collectList().block();
        assertThat(modelTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ModelType testModelType = modelTypeList.get(modelTypeList.size() - 1);
        assertThat(testModelType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void createModelTypeWithExistingId() throws Exception {
        // Create the ModelType with an existing ID
        modelTypeRepository.save(modelType).block();
        ModelTypeDTO modelTypeDTO = modelTypeMapper.toDto(modelType);

        int databaseSizeBeforeCreate = modelTypeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModelType in the database
        List<ModelType> modelTypeList = modelTypeRepository.findAll().collectList().block();
        assertThat(modelTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = modelTypeRepository.findAll().collectList().block().size();
        // set the field null
        modelType.setName(null);

        // Create the ModelType, which fails.
        ModelTypeDTO modelTypeDTO = modelTypeMapper.toDto(modelType);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ModelType> modelTypeList = modelTypeRepository.findAll().collectList().block();
        assertThat(modelTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllModelTypesAsStream() {
        // Initialize the database
        modelType.setId(UUID.randomUUID());
        modelTypeRepository.save(modelType).block();

        List<ModelType> modelTypeList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ModelTypeDTO.class)
            .getResponseBody()
            .map(modelTypeMapper::toEntity)
            .filter(modelType::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(modelTypeList).isNotNull();
        assertThat(modelTypeList).hasSize(1);
        ModelType testModelType = modelTypeList.get(0);
        assertThat(testModelType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void getAllModelTypes() {
        // Initialize the database
        modelType.setId(UUID.randomUUID());
        modelTypeRepository.save(modelType).block();

        // Get all the modelTypeList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(modelType.getId().toString()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME));
    }

    @Test
    void getModelType() {
        // Initialize the database
        modelType.setId(UUID.randomUUID());
        modelTypeRepository.save(modelType).block();

        // Get the modelType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, modelType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(modelType.getId().toString()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME));
    }

    @Test
    void getNonExistingModelType() {
        // Get the modelType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingModelType() throws Exception {
        // Initialize the database
        modelType.setId(UUID.randomUUID());
        modelTypeRepository.save(modelType).block();

        int databaseSizeBeforeUpdate = modelTypeRepository.findAll().collectList().block().size();

        // Update the modelType
        ModelType updatedModelType = modelTypeRepository.findById(modelType.getId()).block();
        updatedModelType.name(UPDATED_NAME);
        ModelTypeDTO modelTypeDTO = modelTypeMapper.toDto(updatedModelType);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, modelTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelTypeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ModelType in the database
        List<ModelType> modelTypeList = modelTypeRepository.findAll().collectList().block();
        assertThat(modelTypeList).hasSize(databaseSizeBeforeUpdate);
        ModelType testModelType = modelTypeList.get(modelTypeList.size() - 1);
        assertThat(testModelType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void putNonExistingModelType() throws Exception {
        int databaseSizeBeforeUpdate = modelTypeRepository.findAll().collectList().block().size();
        modelType.setId(UUID.randomUUID());

        // Create the ModelType
        ModelTypeDTO modelTypeDTO = modelTypeMapper.toDto(modelType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, modelTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModelType in the database
        List<ModelType> modelTypeList = modelTypeRepository.findAll().collectList().block();
        assertThat(modelTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchModelType() throws Exception {
        int databaseSizeBeforeUpdate = modelTypeRepository.findAll().collectList().block().size();
        modelType.setId(UUID.randomUUID());

        // Create the ModelType
        ModelTypeDTO modelTypeDTO = modelTypeMapper.toDto(modelType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModelType in the database
        List<ModelType> modelTypeList = modelTypeRepository.findAll().collectList().block();
        assertThat(modelTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamModelType() throws Exception {
        int databaseSizeBeforeUpdate = modelTypeRepository.findAll().collectList().block().size();
        modelType.setId(UUID.randomUUID());

        // Create the ModelType
        ModelTypeDTO modelTypeDTO = modelTypeMapper.toDto(modelType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ModelType in the database
        List<ModelType> modelTypeList = modelTypeRepository.findAll().collectList().block();
        assertThat(modelTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateModelTypeWithPatch() throws Exception {
        // Initialize the database
        modelType.setId(UUID.randomUUID());
        modelTypeRepository.save(modelType).block();

        int databaseSizeBeforeUpdate = modelTypeRepository.findAll().collectList().block().size();

        // Update the modelType using partial update
        ModelType partialUpdatedModelType = new ModelType();
        partialUpdatedModelType.setId(modelType.getId());

        partialUpdatedModelType.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedModelType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedModelType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ModelType in the database
        List<ModelType> modelTypeList = modelTypeRepository.findAll().collectList().block();
        assertThat(modelTypeList).hasSize(databaseSizeBeforeUpdate);
        ModelType testModelType = modelTypeList.get(modelTypeList.size() - 1);
        assertThat(testModelType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void fullUpdateModelTypeWithPatch() throws Exception {
        // Initialize the database
        modelType.setId(UUID.randomUUID());
        modelTypeRepository.save(modelType).block();

        int databaseSizeBeforeUpdate = modelTypeRepository.findAll().collectList().block().size();

        // Update the modelType using partial update
        ModelType partialUpdatedModelType = new ModelType();
        partialUpdatedModelType.setId(modelType.getId());

        partialUpdatedModelType.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedModelType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedModelType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ModelType in the database
        List<ModelType> modelTypeList = modelTypeRepository.findAll().collectList().block();
        assertThat(modelTypeList).hasSize(databaseSizeBeforeUpdate);
        ModelType testModelType = modelTypeList.get(modelTypeList.size() - 1);
        assertThat(testModelType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void patchNonExistingModelType() throws Exception {
        int databaseSizeBeforeUpdate = modelTypeRepository.findAll().collectList().block().size();
        modelType.setId(UUID.randomUUID());

        // Create the ModelType
        ModelTypeDTO modelTypeDTO = modelTypeMapper.toDto(modelType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, modelTypeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModelType in the database
        List<ModelType> modelTypeList = modelTypeRepository.findAll().collectList().block();
        assertThat(modelTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchModelType() throws Exception {
        int databaseSizeBeforeUpdate = modelTypeRepository.findAll().collectList().block().size();
        modelType.setId(UUID.randomUUID());

        // Create the ModelType
        ModelTypeDTO modelTypeDTO = modelTypeMapper.toDto(modelType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModelType in the database
        List<ModelType> modelTypeList = modelTypeRepository.findAll().collectList().block();
        assertThat(modelTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamModelType() throws Exception {
        int databaseSizeBeforeUpdate = modelTypeRepository.findAll().collectList().block().size();
        modelType.setId(UUID.randomUUID());

        // Create the ModelType
        ModelTypeDTO modelTypeDTO = modelTypeMapper.toDto(modelType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ModelType in the database
        List<ModelType> modelTypeList = modelTypeRepository.findAll().collectList().block();
        assertThat(modelTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteModelType() {
        // Initialize the database
        modelType.setId(UUID.randomUUID());
        modelTypeRepository.save(modelType).block();

        int databaseSizeBeforeDelete = modelTypeRepository.findAll().collectList().block().size();

        // Delete the modelType
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, modelType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ModelType> modelTypeList = modelTypeRepository.findAll().collectList().block();
        assertThat(modelTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
