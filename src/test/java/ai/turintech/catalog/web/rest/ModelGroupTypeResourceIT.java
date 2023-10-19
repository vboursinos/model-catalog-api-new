package ai.turintech.catalog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import ai.turintech.catalog.IntegrationTest;
import ai.turintech.catalog.domain.ModelGroupType;
import ai.turintech.catalog.repository.EntityManager;
import ai.turintech.catalog.repository.ModelGroupTypeRepository;
import ai.turintech.catalog.service.dto.ModelGroupTypeDTO;
import ai.turintech.catalog.service.mapper.ModelGroupTypeMapper;
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
 * Integration tests for the {@link ModelGroupTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ModelGroupTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/model-group-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ModelGroupTypeRepository modelGroupTypeRepository;

    @Autowired
    private ModelGroupTypeMapper modelGroupTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ModelGroupType modelGroupType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ModelGroupType createEntity(EntityManager em) {
        ModelGroupType modelGroupType = new ModelGroupType().id(UUID.randomUUID()).name(DEFAULT_NAME);
        return modelGroupType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ModelGroupType createUpdatedEntity(EntityManager em) {
        ModelGroupType modelGroupType = new ModelGroupType().id(UUID.randomUUID()).name(UPDATED_NAME);
        return modelGroupType;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ModelGroupType.class).block();
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
        modelGroupType = createEntity(em);
    }

    @Test
    void createModelGroupType() throws Exception {
        int databaseSizeBeforeCreate = modelGroupTypeRepository.findAll().collectList().block().size();
        modelGroupType.setId(null);
        // Create the ModelGroupType
        ModelGroupTypeDTO modelGroupTypeDTO = modelGroupTypeMapper.toDto(modelGroupType);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelGroupTypeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ModelGroupType in the database
        List<ModelGroupType> modelGroupTypeList = modelGroupTypeRepository.findAll().collectList().block();
        assertThat(modelGroupTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ModelGroupType testModelGroupType = modelGroupTypeList.get(modelGroupTypeList.size() - 1);
        assertThat(testModelGroupType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void createModelGroupTypeWithExistingId() throws Exception {
        // Create the ModelGroupType with an existing ID
        modelGroupTypeRepository.save(modelGroupType).block();
        ModelGroupTypeDTO modelGroupTypeDTO = modelGroupTypeMapper.toDto(modelGroupType);

        int databaseSizeBeforeCreate = modelGroupTypeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelGroupTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModelGroupType in the database
        List<ModelGroupType> modelGroupTypeList = modelGroupTypeRepository.findAll().collectList().block();
        assertThat(modelGroupTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = modelGroupTypeRepository.findAll().collectList().block().size();
        // set the field null
        modelGroupType.setName(null);

        // Create the ModelGroupType, which fails.
        ModelGroupTypeDTO modelGroupTypeDTO = modelGroupTypeMapper.toDto(modelGroupType);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelGroupTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ModelGroupType> modelGroupTypeList = modelGroupTypeRepository.findAll().collectList().block();
        assertThat(modelGroupTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllModelGroupTypesAsStream() {
        // Initialize the database
        modelGroupType.setId(UUID.randomUUID());
        modelGroupTypeRepository.save(modelGroupType).block();

        List<ModelGroupType> modelGroupTypeList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ModelGroupTypeDTO.class)
            .getResponseBody()
            .map(modelGroupTypeMapper::toEntity)
            .filter(modelGroupType::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(modelGroupTypeList).isNotNull();
        assertThat(modelGroupTypeList).hasSize(1);
        ModelGroupType testModelGroupType = modelGroupTypeList.get(0);
        assertThat(testModelGroupType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void getAllModelGroupTypes() {
        // Initialize the database
        modelGroupType.setId(UUID.randomUUID());
        modelGroupTypeRepository.save(modelGroupType).block();

        // Get all the modelGroupTypeList
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
            .value(hasItem(modelGroupType.getId().toString()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME));
    }

    @Test
    void getModelGroupType() {
        // Initialize the database
        modelGroupType.setId(UUID.randomUUID());
        modelGroupTypeRepository.save(modelGroupType).block();

        // Get the modelGroupType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, modelGroupType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(modelGroupType.getId().toString()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME));
    }

    @Test
    void getNonExistingModelGroupType() {
        // Get the modelGroupType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingModelGroupType() throws Exception {
        // Initialize the database
        modelGroupType.setId(UUID.randomUUID());
        modelGroupTypeRepository.save(modelGroupType).block();

        int databaseSizeBeforeUpdate = modelGroupTypeRepository.findAll().collectList().block().size();

        // Update the modelGroupType
        ModelGroupType updatedModelGroupType = modelGroupTypeRepository.findById(modelGroupType.getId()).block();
        updatedModelGroupType.name(UPDATED_NAME);
        ModelGroupTypeDTO modelGroupTypeDTO = modelGroupTypeMapper.toDto(updatedModelGroupType);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, modelGroupTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelGroupTypeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ModelGroupType in the database
        List<ModelGroupType> modelGroupTypeList = modelGroupTypeRepository.findAll().collectList().block();
        assertThat(modelGroupTypeList).hasSize(databaseSizeBeforeUpdate);
        ModelGroupType testModelGroupType = modelGroupTypeList.get(modelGroupTypeList.size() - 1);
        assertThat(testModelGroupType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void putNonExistingModelGroupType() throws Exception {
        int databaseSizeBeforeUpdate = modelGroupTypeRepository.findAll().collectList().block().size();
        modelGroupType.setId(UUID.randomUUID());

        // Create the ModelGroupType
        ModelGroupTypeDTO modelGroupTypeDTO = modelGroupTypeMapper.toDto(modelGroupType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, modelGroupTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelGroupTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModelGroupType in the database
        List<ModelGroupType> modelGroupTypeList = modelGroupTypeRepository.findAll().collectList().block();
        assertThat(modelGroupTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchModelGroupType() throws Exception {
        int databaseSizeBeforeUpdate = modelGroupTypeRepository.findAll().collectList().block().size();
        modelGroupType.setId(UUID.randomUUID());

        // Create the ModelGroupType
        ModelGroupTypeDTO modelGroupTypeDTO = modelGroupTypeMapper.toDto(modelGroupType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelGroupTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModelGroupType in the database
        List<ModelGroupType> modelGroupTypeList = modelGroupTypeRepository.findAll().collectList().block();
        assertThat(modelGroupTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamModelGroupType() throws Exception {
        int databaseSizeBeforeUpdate = modelGroupTypeRepository.findAll().collectList().block().size();
        modelGroupType.setId(UUID.randomUUID());

        // Create the ModelGroupType
        ModelGroupTypeDTO modelGroupTypeDTO = modelGroupTypeMapper.toDto(modelGroupType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelGroupTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ModelGroupType in the database
        List<ModelGroupType> modelGroupTypeList = modelGroupTypeRepository.findAll().collectList().block();
        assertThat(modelGroupTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateModelGroupTypeWithPatch() throws Exception {
        // Initialize the database
        modelGroupType.setId(UUID.randomUUID());
        modelGroupTypeRepository.save(modelGroupType).block();

        int databaseSizeBeforeUpdate = modelGroupTypeRepository.findAll().collectList().block().size();

        // Update the modelGroupType using partial update
        ModelGroupType partialUpdatedModelGroupType = new ModelGroupType();
        partialUpdatedModelGroupType.setId(modelGroupType.getId());

        partialUpdatedModelGroupType.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedModelGroupType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedModelGroupType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ModelGroupType in the database
        List<ModelGroupType> modelGroupTypeList = modelGroupTypeRepository.findAll().collectList().block();
        assertThat(modelGroupTypeList).hasSize(databaseSizeBeforeUpdate);
        ModelGroupType testModelGroupType = modelGroupTypeList.get(modelGroupTypeList.size() - 1);
        assertThat(testModelGroupType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void fullUpdateModelGroupTypeWithPatch() throws Exception {
        // Initialize the database
        modelGroupType.setId(UUID.randomUUID());
        modelGroupTypeRepository.save(modelGroupType).block();

        int databaseSizeBeforeUpdate = modelGroupTypeRepository.findAll().collectList().block().size();

        // Update the modelGroupType using partial update
        ModelGroupType partialUpdatedModelGroupType = new ModelGroupType();
        partialUpdatedModelGroupType.setId(modelGroupType.getId());

        partialUpdatedModelGroupType.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedModelGroupType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedModelGroupType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ModelGroupType in the database
        List<ModelGroupType> modelGroupTypeList = modelGroupTypeRepository.findAll().collectList().block();
        assertThat(modelGroupTypeList).hasSize(databaseSizeBeforeUpdate);
        ModelGroupType testModelGroupType = modelGroupTypeList.get(modelGroupTypeList.size() - 1);
        assertThat(testModelGroupType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void patchNonExistingModelGroupType() throws Exception {
        int databaseSizeBeforeUpdate = modelGroupTypeRepository.findAll().collectList().block().size();
        modelGroupType.setId(UUID.randomUUID());

        // Create the ModelGroupType
        ModelGroupTypeDTO modelGroupTypeDTO = modelGroupTypeMapper.toDto(modelGroupType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, modelGroupTypeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelGroupTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModelGroupType in the database
        List<ModelGroupType> modelGroupTypeList = modelGroupTypeRepository.findAll().collectList().block();
        assertThat(modelGroupTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchModelGroupType() throws Exception {
        int databaseSizeBeforeUpdate = modelGroupTypeRepository.findAll().collectList().block().size();
        modelGroupType.setId(UUID.randomUUID());

        // Create the ModelGroupType
        ModelGroupTypeDTO modelGroupTypeDTO = modelGroupTypeMapper.toDto(modelGroupType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelGroupTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModelGroupType in the database
        List<ModelGroupType> modelGroupTypeList = modelGroupTypeRepository.findAll().collectList().block();
        assertThat(modelGroupTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamModelGroupType() throws Exception {
        int databaseSizeBeforeUpdate = modelGroupTypeRepository.findAll().collectList().block().size();
        modelGroupType.setId(UUID.randomUUID());

        // Create the ModelGroupType
        ModelGroupTypeDTO modelGroupTypeDTO = modelGroupTypeMapper.toDto(modelGroupType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelGroupTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ModelGroupType in the database
        List<ModelGroupType> modelGroupTypeList = modelGroupTypeRepository.findAll().collectList().block();
        assertThat(modelGroupTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteModelGroupType() {
        // Initialize the database
        modelGroupType.setId(UUID.randomUUID());
        modelGroupTypeRepository.save(modelGroupType).block();

        int databaseSizeBeforeDelete = modelGroupTypeRepository.findAll().collectList().block().size();

        // Delete the modelGroupType
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, modelGroupType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ModelGroupType> modelGroupTypeList = modelGroupTypeRepository.findAll().collectList().block();
        assertThat(modelGroupTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
