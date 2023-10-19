package ai.turintech.catalog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import ai.turintech.catalog.IntegrationTest;
import ai.turintech.catalog.domain.Model;
import ai.turintech.catalog.repository.EntityManager;
import ai.turintech.catalog.repository.ModelRepository;
import ai.turintech.catalog.service.ModelService;
import ai.turintech.catalog.service.dto.ModelDTO;
import ai.turintech.catalog.service.mapper.ModelMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

/**
 * Integration tests for the {@link ModelResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ModelResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DISPLAY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DISPLAY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ADVANTAGES = "AAAAAAAAAA";
    private static final String UPDATED_ADVANTAGES = "BBBBBBBBBB";

    private static final String DEFAULT_DISADVANTAGES = "AAAAAAAAAA";
    private static final String UPDATED_DISADVANTAGES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final Boolean DEFAULT_DECISION_TREE = false;
    private static final Boolean UPDATED_DECISION_TREE = true;

    private static final String ENTITY_API_URL = "/api/models";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ModelRepository modelRepository;

    @Mock
    private ModelRepository modelRepositoryMock;

    @Autowired
    private ModelMapper modelMapper;

    @Mock
    private ModelService modelServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Model model;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Model createEntity(EntityManager em) {
        Model model = new Model()
            .id(UUID.randomUUID())
            .name(DEFAULT_NAME)
            .displayName(DEFAULT_DISPLAY_NAME)
            .description(DEFAULT_DESCRIPTION)
            .advantages(DEFAULT_ADVANTAGES)
            .disadvantages(DEFAULT_DISADVANTAGES)
            .enabled(DEFAULT_ENABLED)
            .decisionTree(DEFAULT_DECISION_TREE);
        return model;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Model createUpdatedEntity(EntityManager em) {
        Model model = new Model()
            .id(UUID.randomUUID())
            .name(UPDATED_NAME)
            .displayName(UPDATED_DISPLAY_NAME)
            .description(UPDATED_DESCRIPTION)
            .advantages(UPDATED_ADVANTAGES)
            .disadvantages(UPDATED_DISADVANTAGES)
            .enabled(UPDATED_ENABLED)
            .decisionTree(UPDATED_DECISION_TREE);
        return model;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll("rel_model__groups").block();
            em.deleteAll("rel_model__incompatible_metrics").block();
            em.deleteAll(Model.class).block();
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
        model = createEntity(em);
    }

    @Test
    void createModel() throws Exception {
        int databaseSizeBeforeCreate = modelRepository.findAll().collectList().block().size();
        model.setId(null);
        // Create the Model
        ModelDTO modelDTO = modelMapper.toDto(model);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll().collectList().block();
        assertThat(modelList).hasSize(databaseSizeBeforeCreate + 1);
        Model testModel = modelList.get(modelList.size() - 1);
        assertThat(testModel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testModel.getDisplayName()).isEqualTo(DEFAULT_DISPLAY_NAME);
        assertThat(testModel.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testModel.getAdvantages()).isEqualTo(DEFAULT_ADVANTAGES);
        assertThat(testModel.getDisadvantages()).isEqualTo(DEFAULT_DISADVANTAGES);
        assertThat(testModel.getEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testModel.getDecisionTree()).isEqualTo(DEFAULT_DECISION_TREE);
    }

    @Test
    void createModelWithExistingId() throws Exception {
        // Create the Model with an existing ID
        modelRepository.save(model).block();
        ModelDTO modelDTO = modelMapper.toDto(model);

        int databaseSizeBeforeCreate = modelRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll().collectList().block();
        assertThat(modelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = modelRepository.findAll().collectList().block().size();
        // set the field null
        model.setName(null);

        // Create the Model, which fails.
        ModelDTO modelDTO = modelMapper.toDto(model);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Model> modelList = modelRepository.findAll().collectList().block();
        assertThat(modelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDisplayNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = modelRepository.findAll().collectList().block().size();
        // set the field null
        model.setDisplayName(null);

        // Create the Model, which fails.
        ModelDTO modelDTO = modelMapper.toDto(model);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Model> modelList = modelRepository.findAll().collectList().block();
        assertThat(modelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = modelRepository.findAll().collectList().block().size();
        // set the field null
        model.setEnabled(null);

        // Create the Model, which fails.
        ModelDTO modelDTO = modelMapper.toDto(model);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Model> modelList = modelRepository.findAll().collectList().block();
        assertThat(modelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDecisionTreeIsRequired() throws Exception {
        int databaseSizeBeforeTest = modelRepository.findAll().collectList().block().size();
        // set the field null
        model.setDecisionTree(null);

        // Create the Model, which fails.
        ModelDTO modelDTO = modelMapper.toDto(model);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Model> modelList = modelRepository.findAll().collectList().block();
        assertThat(modelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllModels() {
        // Initialize the database
        model.setId(UUID.randomUUID());
        modelRepository.save(model).block();

        // Get all the modelList
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
            .value(hasItem(model.getId().toString()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].displayName")
            .value(hasItem(DEFAULT_DISPLAY_NAME))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].advantages")
            .value(hasItem(DEFAULT_ADVANTAGES))
            .jsonPath("$.[*].disadvantages")
            .value(hasItem(DEFAULT_DISADVANTAGES))
            .jsonPath("$.[*].enabled")
            .value(hasItem(DEFAULT_ENABLED.booleanValue()))
            .jsonPath("$.[*].decisionTree")
            .value(hasItem(DEFAULT_DECISION_TREE.booleanValue()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllModelsWithEagerRelationshipsIsEnabled() {
        when(modelServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(modelServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllModelsWithEagerRelationshipsIsNotEnabled() {
        when(modelServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(modelRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getModel() {
        // Initialize the database
        model.setId(UUID.randomUUID());
        modelRepository.save(model).block();

        // Get the model
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, model.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(model.getId().toString()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.displayName")
            .value(is(DEFAULT_DISPLAY_NAME))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.advantages")
            .value(is(DEFAULT_ADVANTAGES))
            .jsonPath("$.disadvantages")
            .value(is(DEFAULT_DISADVANTAGES))
            .jsonPath("$.enabled")
            .value(is(DEFAULT_ENABLED.booleanValue()))
            .jsonPath("$.decisionTree")
            .value(is(DEFAULT_DECISION_TREE.booleanValue()));
    }

    @Test
    void getNonExistingModel() {
        // Get the model
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingModel() throws Exception {
        // Initialize the database
        model.setId(UUID.randomUUID());
        modelRepository.save(model).block();

        int databaseSizeBeforeUpdate = modelRepository.findAll().collectList().block().size();

        // Update the model
        Model updatedModel = modelRepository.findById(model.getId()).block();
        updatedModel
            .name(UPDATED_NAME)
            .displayName(UPDATED_DISPLAY_NAME)
            .description(UPDATED_DESCRIPTION)
            .advantages(UPDATED_ADVANTAGES)
            .disadvantages(UPDATED_DISADVANTAGES)
            .enabled(UPDATED_ENABLED)
            .decisionTree(UPDATED_DECISION_TREE);
        ModelDTO modelDTO = modelMapper.toDto(updatedModel);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, modelDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll().collectList().block();
        assertThat(modelList).hasSize(databaseSizeBeforeUpdate);
        Model testModel = modelList.get(modelList.size() - 1);
        assertThat(testModel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testModel.getDisplayName()).isEqualTo(UPDATED_DISPLAY_NAME);
        assertThat(testModel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testModel.getAdvantages()).isEqualTo(UPDATED_ADVANTAGES);
        assertThat(testModel.getDisadvantages()).isEqualTo(UPDATED_DISADVANTAGES);
        assertThat(testModel.getEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testModel.getDecisionTree()).isEqualTo(UPDATED_DECISION_TREE);
    }

    @Test
    void putNonExistingModel() throws Exception {
        int databaseSizeBeforeUpdate = modelRepository.findAll().collectList().block().size();
        model.setId(UUID.randomUUID());

        // Create the Model
        ModelDTO modelDTO = modelMapper.toDto(model);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, modelDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll().collectList().block();
        assertThat(modelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchModel() throws Exception {
        int databaseSizeBeforeUpdate = modelRepository.findAll().collectList().block().size();
        model.setId(UUID.randomUUID());

        // Create the Model
        ModelDTO modelDTO = modelMapper.toDto(model);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll().collectList().block();
        assertThat(modelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamModel() throws Exception {
        int databaseSizeBeforeUpdate = modelRepository.findAll().collectList().block().size();
        model.setId(UUID.randomUUID());

        // Create the Model
        ModelDTO modelDTO = modelMapper.toDto(model);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll().collectList().block();
        assertThat(modelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateModelWithPatch() throws Exception {
        // Initialize the database
        model.setId(UUID.randomUUID());
        modelRepository.save(model).block();

        int databaseSizeBeforeUpdate = modelRepository.findAll().collectList().block().size();

        // Update the model using partial update
        Model partialUpdatedModel = new Model();
        partialUpdatedModel.setId(model.getId());

        partialUpdatedModel
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .advantages(UPDATED_ADVANTAGES)
            .disadvantages(UPDATED_DISADVANTAGES)
            .decisionTree(UPDATED_DECISION_TREE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedModel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedModel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll().collectList().block();
        assertThat(modelList).hasSize(databaseSizeBeforeUpdate);
        Model testModel = modelList.get(modelList.size() - 1);
        assertThat(testModel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testModel.getDisplayName()).isEqualTo(DEFAULT_DISPLAY_NAME);
        assertThat(testModel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testModel.getAdvantages()).isEqualTo(UPDATED_ADVANTAGES);
        assertThat(testModel.getDisadvantages()).isEqualTo(UPDATED_DISADVANTAGES);
        assertThat(testModel.getEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testModel.getDecisionTree()).isEqualTo(UPDATED_DECISION_TREE);
    }

    @Test
    void fullUpdateModelWithPatch() throws Exception {
        // Initialize the database
        model.setId(UUID.randomUUID());
        modelRepository.save(model).block();

        int databaseSizeBeforeUpdate = modelRepository.findAll().collectList().block().size();

        // Update the model using partial update
        Model partialUpdatedModel = new Model();
        partialUpdatedModel.setId(model.getId());

        partialUpdatedModel
            .name(UPDATED_NAME)
            .displayName(UPDATED_DISPLAY_NAME)
            .description(UPDATED_DESCRIPTION)
            .advantages(UPDATED_ADVANTAGES)
            .disadvantages(UPDATED_DISADVANTAGES)
            .enabled(UPDATED_ENABLED)
            .decisionTree(UPDATED_DECISION_TREE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedModel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedModel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll().collectList().block();
        assertThat(modelList).hasSize(databaseSizeBeforeUpdate);
        Model testModel = modelList.get(modelList.size() - 1);
        assertThat(testModel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testModel.getDisplayName()).isEqualTo(UPDATED_DISPLAY_NAME);
        assertThat(testModel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testModel.getAdvantages()).isEqualTo(UPDATED_ADVANTAGES);
        assertThat(testModel.getDisadvantages()).isEqualTo(UPDATED_DISADVANTAGES);
        assertThat(testModel.getEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testModel.getDecisionTree()).isEqualTo(UPDATED_DECISION_TREE);
    }

    @Test
    void patchNonExistingModel() throws Exception {
        int databaseSizeBeforeUpdate = modelRepository.findAll().collectList().block().size();
        model.setId(UUID.randomUUID());

        // Create the Model
        ModelDTO modelDTO = modelMapper.toDto(model);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, modelDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll().collectList().block();
        assertThat(modelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchModel() throws Exception {
        int databaseSizeBeforeUpdate = modelRepository.findAll().collectList().block().size();
        model.setId(UUID.randomUUID());

        // Create the Model
        ModelDTO modelDTO = modelMapper.toDto(model);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll().collectList().block();
        assertThat(modelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamModel() throws Exception {
        int databaseSizeBeforeUpdate = modelRepository.findAll().collectList().block().size();
        model.setId(UUID.randomUUID());

        // Create the Model
        ModelDTO modelDTO = modelMapper.toDto(model);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Model in the database
        List<Model> modelList = modelRepository.findAll().collectList().block();
        assertThat(modelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteModel() {
        // Initialize the database
        model.setId(UUID.randomUUID());
        modelRepository.save(model).block();

        int databaseSizeBeforeDelete = modelRepository.findAll().collectList().block().size();

        // Delete the model
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, model.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Model> modelList = modelRepository.findAll().collectList().block();
        assertThat(modelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
