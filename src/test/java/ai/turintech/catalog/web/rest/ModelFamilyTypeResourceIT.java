package ai.turintech.catalog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import ai.turintech.catalog.IntegrationTest;
import ai.turintech.catalog.domain.ModelFamilyType;
import ai.turintech.catalog.repository.EntityManager;
import ai.turintech.catalog.repository.ModelFamilyTypeRepository;
import ai.turintech.catalog.service.dto.ModelFamilyTypeDTO;
import ai.turintech.catalog.service.mapper.ModelFamilyTypeMapper;
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
 * Integration tests for the {@link ModelFamilyTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ModelFamilyTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/model-family-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ModelFamilyTypeRepository modelFamilyTypeRepository;

    @Autowired
    private ModelFamilyTypeMapper modelFamilyTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ModelFamilyType modelFamilyType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ModelFamilyType createEntity(EntityManager em) {
        ModelFamilyType modelFamilyType = new ModelFamilyType().id(UUID.randomUUID()).name(DEFAULT_NAME);
        return modelFamilyType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ModelFamilyType createUpdatedEntity(EntityManager em) {
        ModelFamilyType modelFamilyType = new ModelFamilyType().id(UUID.randomUUID()).name(UPDATED_NAME);
        return modelFamilyType;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ModelFamilyType.class).block();
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
        modelFamilyType = createEntity(em);
    }

    @Test
    void createModelFamilyType() throws Exception {
        int databaseSizeBeforeCreate = modelFamilyTypeRepository.findAll().collectList().block().size();
        modelFamilyType.setId(null);
        // Create the ModelFamilyType
        ModelFamilyTypeDTO modelFamilyTypeDTO = modelFamilyTypeMapper.toDto(modelFamilyType);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelFamilyTypeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ModelFamilyType in the database
        List<ModelFamilyType> modelFamilyTypeList = modelFamilyTypeRepository.findAll().collectList().block();
        assertThat(modelFamilyTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ModelFamilyType testModelFamilyType = modelFamilyTypeList.get(modelFamilyTypeList.size() - 1);
        assertThat(testModelFamilyType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void createModelFamilyTypeWithExistingId() throws Exception {
        // Create the ModelFamilyType with an existing ID
        modelFamilyTypeRepository.save(modelFamilyType).block();
        ModelFamilyTypeDTO modelFamilyTypeDTO = modelFamilyTypeMapper.toDto(modelFamilyType);

        int databaseSizeBeforeCreate = modelFamilyTypeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelFamilyTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModelFamilyType in the database
        List<ModelFamilyType> modelFamilyTypeList = modelFamilyTypeRepository.findAll().collectList().block();
        assertThat(modelFamilyTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = modelFamilyTypeRepository.findAll().collectList().block().size();
        // set the field null
        modelFamilyType.setName(null);

        // Create the ModelFamilyType, which fails.
        ModelFamilyTypeDTO modelFamilyTypeDTO = modelFamilyTypeMapper.toDto(modelFamilyType);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelFamilyTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ModelFamilyType> modelFamilyTypeList = modelFamilyTypeRepository.findAll().collectList().block();
        assertThat(modelFamilyTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllModelFamilyTypesAsStream() {
        // Initialize the database
        modelFamilyType.setId(UUID.randomUUID());
        modelFamilyTypeRepository.save(modelFamilyType).block();

        List<ModelFamilyType> modelFamilyTypeList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ModelFamilyTypeDTO.class)
            .getResponseBody()
            .map(modelFamilyTypeMapper::toEntity)
            .filter(modelFamilyType::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(modelFamilyTypeList).isNotNull();
        assertThat(modelFamilyTypeList).hasSize(1);
        ModelFamilyType testModelFamilyType = modelFamilyTypeList.get(0);
        assertThat(testModelFamilyType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void getAllModelFamilyTypes() {
        // Initialize the database
        modelFamilyType.setId(UUID.randomUUID());
        modelFamilyTypeRepository.save(modelFamilyType).block();

        // Get all the modelFamilyTypeList
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
            .value(hasItem(modelFamilyType.getId().toString()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME));
    }

    @Test
    void getModelFamilyType() {
        // Initialize the database
        modelFamilyType.setId(UUID.randomUUID());
        modelFamilyTypeRepository.save(modelFamilyType).block();

        // Get the modelFamilyType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, modelFamilyType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(modelFamilyType.getId().toString()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME));
    }

    @Test
    void getNonExistingModelFamilyType() {
        // Get the modelFamilyType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingModelFamilyType() throws Exception {
        // Initialize the database
        modelFamilyType.setId(UUID.randomUUID());
        modelFamilyTypeRepository.save(modelFamilyType).block();

        int databaseSizeBeforeUpdate = modelFamilyTypeRepository.findAll().collectList().block().size();

        // Update the modelFamilyType
        ModelFamilyType updatedModelFamilyType = modelFamilyTypeRepository.findById(modelFamilyType.getId()).block();
        updatedModelFamilyType.name(UPDATED_NAME);
        ModelFamilyTypeDTO modelFamilyTypeDTO = modelFamilyTypeMapper.toDto(updatedModelFamilyType);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, modelFamilyTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelFamilyTypeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ModelFamilyType in the database
        List<ModelFamilyType> modelFamilyTypeList = modelFamilyTypeRepository.findAll().collectList().block();
        assertThat(modelFamilyTypeList).hasSize(databaseSizeBeforeUpdate);
        ModelFamilyType testModelFamilyType = modelFamilyTypeList.get(modelFamilyTypeList.size() - 1);
        assertThat(testModelFamilyType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void putNonExistingModelFamilyType() throws Exception {
        int databaseSizeBeforeUpdate = modelFamilyTypeRepository.findAll().collectList().block().size();
        modelFamilyType.setId(UUID.randomUUID());

        // Create the ModelFamilyType
        ModelFamilyTypeDTO modelFamilyTypeDTO = modelFamilyTypeMapper.toDto(modelFamilyType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, modelFamilyTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelFamilyTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModelFamilyType in the database
        List<ModelFamilyType> modelFamilyTypeList = modelFamilyTypeRepository.findAll().collectList().block();
        assertThat(modelFamilyTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchModelFamilyType() throws Exception {
        int databaseSizeBeforeUpdate = modelFamilyTypeRepository.findAll().collectList().block().size();
        modelFamilyType.setId(UUID.randomUUID());

        // Create the ModelFamilyType
        ModelFamilyTypeDTO modelFamilyTypeDTO = modelFamilyTypeMapper.toDto(modelFamilyType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelFamilyTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModelFamilyType in the database
        List<ModelFamilyType> modelFamilyTypeList = modelFamilyTypeRepository.findAll().collectList().block();
        assertThat(modelFamilyTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamModelFamilyType() throws Exception {
        int databaseSizeBeforeUpdate = modelFamilyTypeRepository.findAll().collectList().block().size();
        modelFamilyType.setId(UUID.randomUUID());

        // Create the ModelFamilyType
        ModelFamilyTypeDTO modelFamilyTypeDTO = modelFamilyTypeMapper.toDto(modelFamilyType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelFamilyTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ModelFamilyType in the database
        List<ModelFamilyType> modelFamilyTypeList = modelFamilyTypeRepository.findAll().collectList().block();
        assertThat(modelFamilyTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateModelFamilyTypeWithPatch() throws Exception {
        // Initialize the database
        modelFamilyType.setId(UUID.randomUUID());
        modelFamilyTypeRepository.save(modelFamilyType).block();

        int databaseSizeBeforeUpdate = modelFamilyTypeRepository.findAll().collectList().block().size();

        // Update the modelFamilyType using partial update
        ModelFamilyType partialUpdatedModelFamilyType = new ModelFamilyType();
        partialUpdatedModelFamilyType.setId(modelFamilyType.getId());

        partialUpdatedModelFamilyType.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedModelFamilyType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedModelFamilyType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ModelFamilyType in the database
        List<ModelFamilyType> modelFamilyTypeList = modelFamilyTypeRepository.findAll().collectList().block();
        assertThat(modelFamilyTypeList).hasSize(databaseSizeBeforeUpdate);
        ModelFamilyType testModelFamilyType = modelFamilyTypeList.get(modelFamilyTypeList.size() - 1);
        assertThat(testModelFamilyType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void fullUpdateModelFamilyTypeWithPatch() throws Exception {
        // Initialize the database
        modelFamilyType.setId(UUID.randomUUID());
        modelFamilyTypeRepository.save(modelFamilyType).block();

        int databaseSizeBeforeUpdate = modelFamilyTypeRepository.findAll().collectList().block().size();

        // Update the modelFamilyType using partial update
        ModelFamilyType partialUpdatedModelFamilyType = new ModelFamilyType();
        partialUpdatedModelFamilyType.setId(modelFamilyType.getId());

        partialUpdatedModelFamilyType.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedModelFamilyType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedModelFamilyType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ModelFamilyType in the database
        List<ModelFamilyType> modelFamilyTypeList = modelFamilyTypeRepository.findAll().collectList().block();
        assertThat(modelFamilyTypeList).hasSize(databaseSizeBeforeUpdate);
        ModelFamilyType testModelFamilyType = modelFamilyTypeList.get(modelFamilyTypeList.size() - 1);
        assertThat(testModelFamilyType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void patchNonExistingModelFamilyType() throws Exception {
        int databaseSizeBeforeUpdate = modelFamilyTypeRepository.findAll().collectList().block().size();
        modelFamilyType.setId(UUID.randomUUID());

        // Create the ModelFamilyType
        ModelFamilyTypeDTO modelFamilyTypeDTO = modelFamilyTypeMapper.toDto(modelFamilyType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, modelFamilyTypeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelFamilyTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModelFamilyType in the database
        List<ModelFamilyType> modelFamilyTypeList = modelFamilyTypeRepository.findAll().collectList().block();
        assertThat(modelFamilyTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchModelFamilyType() throws Exception {
        int databaseSizeBeforeUpdate = modelFamilyTypeRepository.findAll().collectList().block().size();
        modelFamilyType.setId(UUID.randomUUID());

        // Create the ModelFamilyType
        ModelFamilyTypeDTO modelFamilyTypeDTO = modelFamilyTypeMapper.toDto(modelFamilyType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelFamilyTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModelFamilyType in the database
        List<ModelFamilyType> modelFamilyTypeList = modelFamilyTypeRepository.findAll().collectList().block();
        assertThat(modelFamilyTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamModelFamilyType() throws Exception {
        int databaseSizeBeforeUpdate = modelFamilyTypeRepository.findAll().collectList().block().size();
        modelFamilyType.setId(UUID.randomUUID());

        // Create the ModelFamilyType
        ModelFamilyTypeDTO modelFamilyTypeDTO = modelFamilyTypeMapper.toDto(modelFamilyType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelFamilyTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ModelFamilyType in the database
        List<ModelFamilyType> modelFamilyTypeList = modelFamilyTypeRepository.findAll().collectList().block();
        assertThat(modelFamilyTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteModelFamilyType() {
        // Initialize the database
        modelFamilyType.setId(UUID.randomUUID());
        modelFamilyTypeRepository.save(modelFamilyType).block();

        int databaseSizeBeforeDelete = modelFamilyTypeRepository.findAll().collectList().block().size();

        // Delete the modelFamilyType
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, modelFamilyType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ModelFamilyType> modelFamilyTypeList = modelFamilyTypeRepository.findAll().collectList().block();
        assertThat(modelFamilyTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
