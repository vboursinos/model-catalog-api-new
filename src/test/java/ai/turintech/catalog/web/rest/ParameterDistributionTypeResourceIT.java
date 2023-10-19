package ai.turintech.catalog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import ai.turintech.catalog.IntegrationTest;
import ai.turintech.catalog.domain.ParameterDistributionType;
import ai.turintech.catalog.repository.EntityManager;
import ai.turintech.catalog.repository.ParameterDistributionTypeRepository;
import ai.turintech.catalog.service.dto.ParameterDistributionTypeDTO;
import ai.turintech.catalog.service.mapper.ParameterDistributionTypeMapper;
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
 * Integration tests for the {@link ParameterDistributionTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ParameterDistributionTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/parameter-distribution-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ParameterDistributionTypeRepository parameterDistributionTypeRepository;

    @Autowired
    private ParameterDistributionTypeMapper parameterDistributionTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ParameterDistributionType parameterDistributionType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParameterDistributionType createEntity(EntityManager em) {
        ParameterDistributionType parameterDistributionType = new ParameterDistributionType().id(UUID.randomUUID()).name(DEFAULT_NAME);
        return parameterDistributionType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParameterDistributionType createUpdatedEntity(EntityManager em) {
        ParameterDistributionType parameterDistributionType = new ParameterDistributionType().id(UUID.randomUUID()).name(UPDATED_NAME);
        return parameterDistributionType;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ParameterDistributionType.class).block();
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
        parameterDistributionType = createEntity(em);
    }

    @Test
    void createParameterDistributionType() throws Exception {
        int databaseSizeBeforeCreate = parameterDistributionTypeRepository.findAll().collectList().block().size();
        parameterDistributionType.setId(null);
        // Create the ParameterDistributionType
        ParameterDistributionTypeDTO parameterDistributionTypeDTO = parameterDistributionTypeMapper.toDto(parameterDistributionType);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterDistributionTypeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ParameterDistributionType in the database
        List<ParameterDistributionType> parameterDistributionTypeList = parameterDistributionTypeRepository.findAll().collectList().block();
        assertThat(parameterDistributionTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ParameterDistributionType testParameterDistributionType = parameterDistributionTypeList.get(
            parameterDistributionTypeList.size() - 1
        );
        assertThat(testParameterDistributionType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void createParameterDistributionTypeWithExistingId() throws Exception {
        // Create the ParameterDistributionType with an existing ID
        parameterDistributionTypeRepository.save(parameterDistributionType).block();
        ParameterDistributionTypeDTO parameterDistributionTypeDTO = parameterDistributionTypeMapper.toDto(parameterDistributionType);

        int databaseSizeBeforeCreate = parameterDistributionTypeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterDistributionTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ParameterDistributionType in the database
        List<ParameterDistributionType> parameterDistributionTypeList = parameterDistributionTypeRepository.findAll().collectList().block();
        assertThat(parameterDistributionTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = parameterDistributionTypeRepository.findAll().collectList().block().size();
        // set the field null
        parameterDistributionType.setName(null);

        // Create the ParameterDistributionType, which fails.
        ParameterDistributionTypeDTO parameterDistributionTypeDTO = parameterDistributionTypeMapper.toDto(parameterDistributionType);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterDistributionTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ParameterDistributionType> parameterDistributionTypeList = parameterDistributionTypeRepository.findAll().collectList().block();
        assertThat(parameterDistributionTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllParameterDistributionTypesAsStream() {
        // Initialize the database
        parameterDistributionType.setId(UUID.randomUUID());
        parameterDistributionTypeRepository.save(parameterDistributionType).block();

        List<ParameterDistributionType> parameterDistributionTypeList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ParameterDistributionTypeDTO.class)
            .getResponseBody()
            .map(parameterDistributionTypeMapper::toEntity)
            .filter(parameterDistributionType::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(parameterDistributionTypeList).isNotNull();
        assertThat(parameterDistributionTypeList).hasSize(1);
        ParameterDistributionType testParameterDistributionType = parameterDistributionTypeList.get(0);
        assertThat(testParameterDistributionType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void getAllParameterDistributionTypes() {
        // Initialize the database
        parameterDistributionType.setId(UUID.randomUUID());
        parameterDistributionTypeRepository.save(parameterDistributionType).block();

        // Get all the parameterDistributionTypeList
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
            .value(hasItem(parameterDistributionType.getId().toString()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME));
    }

    @Test
    void getParameterDistributionType() {
        // Initialize the database
        parameterDistributionType.setId(UUID.randomUUID());
        parameterDistributionTypeRepository.save(parameterDistributionType).block();

        // Get the parameterDistributionType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, parameterDistributionType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(parameterDistributionType.getId().toString()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME));
    }

    @Test
    void getNonExistingParameterDistributionType() {
        // Get the parameterDistributionType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingParameterDistributionType() throws Exception {
        // Initialize the database
        parameterDistributionType.setId(UUID.randomUUID());
        parameterDistributionTypeRepository.save(parameterDistributionType).block();

        int databaseSizeBeforeUpdate = parameterDistributionTypeRepository.findAll().collectList().block().size();

        // Update the parameterDistributionType
        ParameterDistributionType updatedParameterDistributionType = parameterDistributionTypeRepository
            .findById(parameterDistributionType.getId())
            .block();
        updatedParameterDistributionType.name(UPDATED_NAME);
        ParameterDistributionTypeDTO parameterDistributionTypeDTO = parameterDistributionTypeMapper.toDto(updatedParameterDistributionType);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, parameterDistributionTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterDistributionTypeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ParameterDistributionType in the database
        List<ParameterDistributionType> parameterDistributionTypeList = parameterDistributionTypeRepository.findAll().collectList().block();
        assertThat(parameterDistributionTypeList).hasSize(databaseSizeBeforeUpdate);
        ParameterDistributionType testParameterDistributionType = parameterDistributionTypeList.get(
            parameterDistributionTypeList.size() - 1
        );
        assertThat(testParameterDistributionType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void putNonExistingParameterDistributionType() throws Exception {
        int databaseSizeBeforeUpdate = parameterDistributionTypeRepository.findAll().collectList().block().size();
        parameterDistributionType.setId(UUID.randomUUID());

        // Create the ParameterDistributionType
        ParameterDistributionTypeDTO parameterDistributionTypeDTO = parameterDistributionTypeMapper.toDto(parameterDistributionType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, parameterDistributionTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterDistributionTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ParameterDistributionType in the database
        List<ParameterDistributionType> parameterDistributionTypeList = parameterDistributionTypeRepository.findAll().collectList().block();
        assertThat(parameterDistributionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchParameterDistributionType() throws Exception {
        int databaseSizeBeforeUpdate = parameterDistributionTypeRepository.findAll().collectList().block().size();
        parameterDistributionType.setId(UUID.randomUUID());

        // Create the ParameterDistributionType
        ParameterDistributionTypeDTO parameterDistributionTypeDTO = parameterDistributionTypeMapper.toDto(parameterDistributionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterDistributionTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ParameterDistributionType in the database
        List<ParameterDistributionType> parameterDistributionTypeList = parameterDistributionTypeRepository.findAll().collectList().block();
        assertThat(parameterDistributionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamParameterDistributionType() throws Exception {
        int databaseSizeBeforeUpdate = parameterDistributionTypeRepository.findAll().collectList().block().size();
        parameterDistributionType.setId(UUID.randomUUID());

        // Create the ParameterDistributionType
        ParameterDistributionTypeDTO parameterDistributionTypeDTO = parameterDistributionTypeMapper.toDto(parameterDistributionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterDistributionTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ParameterDistributionType in the database
        List<ParameterDistributionType> parameterDistributionTypeList = parameterDistributionTypeRepository.findAll().collectList().block();
        assertThat(parameterDistributionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateParameterDistributionTypeWithPatch() throws Exception {
        // Initialize the database
        parameterDistributionType.setId(UUID.randomUUID());
        parameterDistributionTypeRepository.save(parameterDistributionType).block();

        int databaseSizeBeforeUpdate = parameterDistributionTypeRepository.findAll().collectList().block().size();

        // Update the parameterDistributionType using partial update
        ParameterDistributionType partialUpdatedParameterDistributionType = new ParameterDistributionType();
        partialUpdatedParameterDistributionType.setId(parameterDistributionType.getId());

        partialUpdatedParameterDistributionType.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedParameterDistributionType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedParameterDistributionType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ParameterDistributionType in the database
        List<ParameterDistributionType> parameterDistributionTypeList = parameterDistributionTypeRepository.findAll().collectList().block();
        assertThat(parameterDistributionTypeList).hasSize(databaseSizeBeforeUpdate);
        ParameterDistributionType testParameterDistributionType = parameterDistributionTypeList.get(
            parameterDistributionTypeList.size() - 1
        );
        assertThat(testParameterDistributionType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void fullUpdateParameterDistributionTypeWithPatch() throws Exception {
        // Initialize the database
        parameterDistributionType.setId(UUID.randomUUID());
        parameterDistributionTypeRepository.save(parameterDistributionType).block();

        int databaseSizeBeforeUpdate = parameterDistributionTypeRepository.findAll().collectList().block().size();

        // Update the parameterDistributionType using partial update
        ParameterDistributionType partialUpdatedParameterDistributionType = new ParameterDistributionType();
        partialUpdatedParameterDistributionType.setId(parameterDistributionType.getId());

        partialUpdatedParameterDistributionType.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedParameterDistributionType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedParameterDistributionType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ParameterDistributionType in the database
        List<ParameterDistributionType> parameterDistributionTypeList = parameterDistributionTypeRepository.findAll().collectList().block();
        assertThat(parameterDistributionTypeList).hasSize(databaseSizeBeforeUpdate);
        ParameterDistributionType testParameterDistributionType = parameterDistributionTypeList.get(
            parameterDistributionTypeList.size() - 1
        );
        assertThat(testParameterDistributionType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void patchNonExistingParameterDistributionType() throws Exception {
        int databaseSizeBeforeUpdate = parameterDistributionTypeRepository.findAll().collectList().block().size();
        parameterDistributionType.setId(UUID.randomUUID());

        // Create the ParameterDistributionType
        ParameterDistributionTypeDTO parameterDistributionTypeDTO = parameterDistributionTypeMapper.toDto(parameterDistributionType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, parameterDistributionTypeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterDistributionTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ParameterDistributionType in the database
        List<ParameterDistributionType> parameterDistributionTypeList = parameterDistributionTypeRepository.findAll().collectList().block();
        assertThat(parameterDistributionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchParameterDistributionType() throws Exception {
        int databaseSizeBeforeUpdate = parameterDistributionTypeRepository.findAll().collectList().block().size();
        parameterDistributionType.setId(UUID.randomUUID());

        // Create the ParameterDistributionType
        ParameterDistributionTypeDTO parameterDistributionTypeDTO = parameterDistributionTypeMapper.toDto(parameterDistributionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterDistributionTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ParameterDistributionType in the database
        List<ParameterDistributionType> parameterDistributionTypeList = parameterDistributionTypeRepository.findAll().collectList().block();
        assertThat(parameterDistributionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamParameterDistributionType() throws Exception {
        int databaseSizeBeforeUpdate = parameterDistributionTypeRepository.findAll().collectList().block().size();
        parameterDistributionType.setId(UUID.randomUUID());

        // Create the ParameterDistributionType
        ParameterDistributionTypeDTO parameterDistributionTypeDTO = parameterDistributionTypeMapper.toDto(parameterDistributionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterDistributionTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ParameterDistributionType in the database
        List<ParameterDistributionType> parameterDistributionTypeList = parameterDistributionTypeRepository.findAll().collectList().block();
        assertThat(parameterDistributionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteParameterDistributionType() {
        // Initialize the database
        parameterDistributionType.setId(UUID.randomUUID());
        parameterDistributionTypeRepository.save(parameterDistributionType).block();

        int databaseSizeBeforeDelete = parameterDistributionTypeRepository.findAll().collectList().block().size();

        // Delete the parameterDistributionType
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, parameterDistributionType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ParameterDistributionType> parameterDistributionTypeList = parameterDistributionTypeRepository.findAll().collectList().block();
        assertThat(parameterDistributionTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
