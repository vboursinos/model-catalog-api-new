package ai.turintech.catalog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import ai.turintech.catalog.IntegrationTest;
import ai.turintech.catalog.domain.Parameter;
import ai.turintech.catalog.repository.EntityManager;
import ai.turintech.catalog.repository.ParameterRepository;
import ai.turintech.catalog.service.dto.ParameterDTO;
import ai.turintech.catalog.service.mapper.ParameterMapper;
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
 * Integration tests for the {@link ParameterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ParameterResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final Boolean DEFAULT_FIXED_VALUE = false;
    private static final Boolean UPDATED_FIXED_VALUE = true;

    private static final Integer DEFAULT_ORDERING = 1;
    private static final Integer UPDATED_ORDERING = 2;

    private static final String ENTITY_API_URL = "/api/parameters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ParameterRepository parameterRepository;

    @Autowired
    private ParameterMapper parameterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Parameter parameter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parameter createEntity(EntityManager em) {
        Parameter parameter = new Parameter()
            .id(UUID.randomUUID())
            .name(DEFAULT_NAME)
            .label(DEFAULT_LABEL)
            .description(DEFAULT_DESCRIPTION)
            .enabled(DEFAULT_ENABLED)
            .fixedValue(DEFAULT_FIXED_VALUE)
            .ordering(DEFAULT_ORDERING);
        return parameter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parameter createUpdatedEntity(EntityManager em) {
        Parameter parameter = new Parameter()
            .id(UUID.randomUUID())
            .name(UPDATED_NAME)
            .label(UPDATED_LABEL)
            .description(UPDATED_DESCRIPTION)
            .enabled(UPDATED_ENABLED)
            .fixedValue(UPDATED_FIXED_VALUE)
            .ordering(UPDATED_ORDERING);
        return parameter;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Parameter.class).block();
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
        parameter = createEntity(em);
    }

    @Test
    void createParameter() throws Exception {
        int databaseSizeBeforeCreate = parameterRepository.findAll().collectList().block().size();
        parameter.setId(null);
        // Create the Parameter
        ParameterDTO parameterDTO = parameterMapper.toDto(parameter);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll().collectList().block();
        assertThat(parameterList).hasSize(databaseSizeBeforeCreate + 1);
        Parameter testParameter = parameterList.get(parameterList.size() - 1);
        assertThat(testParameter.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testParameter.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testParameter.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testParameter.getEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testParameter.getFixedValue()).isEqualTo(DEFAULT_FIXED_VALUE);
        assertThat(testParameter.getOrdering()).isEqualTo(DEFAULT_ORDERING);
    }

    @Test
    void createParameterWithExistingId() throws Exception {
        // Create the Parameter with an existing ID
        parameterRepository.save(parameter).block();
        ParameterDTO parameterDTO = parameterMapper.toDto(parameter);

        int databaseSizeBeforeCreate = parameterRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll().collectList().block();
        assertThat(parameterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = parameterRepository.findAll().collectList().block().size();
        // set the field null
        parameter.setName(null);

        // Create the Parameter, which fails.
        ParameterDTO parameterDTO = parameterMapper.toDto(parameter);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Parameter> parameterList = parameterRepository.findAll().collectList().block();
        assertThat(parameterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = parameterRepository.findAll().collectList().block().size();
        // set the field null
        parameter.setLabel(null);

        // Create the Parameter, which fails.
        ParameterDTO parameterDTO = parameterMapper.toDto(parameter);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Parameter> parameterList = parameterRepository.findAll().collectList().block();
        assertThat(parameterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = parameterRepository.findAll().collectList().block().size();
        // set the field null
        parameter.setEnabled(null);

        // Create the Parameter, which fails.
        ParameterDTO parameterDTO = parameterMapper.toDto(parameter);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Parameter> parameterList = parameterRepository.findAll().collectList().block();
        assertThat(parameterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkFixedValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = parameterRepository.findAll().collectList().block().size();
        // set the field null
        parameter.setFixedValue(null);

        // Create the Parameter, which fails.
        ParameterDTO parameterDTO = parameterMapper.toDto(parameter);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Parameter> parameterList = parameterRepository.findAll().collectList().block();
        assertThat(parameterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkOrderingIsRequired() throws Exception {
        int databaseSizeBeforeTest = parameterRepository.findAll().collectList().block().size();
        // set the field null
        parameter.setOrdering(null);

        // Create the Parameter, which fails.
        ParameterDTO parameterDTO = parameterMapper.toDto(parameter);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Parameter> parameterList = parameterRepository.findAll().collectList().block();
        assertThat(parameterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllParameters() {
        // Initialize the database
        parameter.setId(UUID.randomUUID());
        parameterRepository.save(parameter).block();

        // Get all the parameterList
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
            .value(hasItem(parameter.getId().toString()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].label")
            .value(hasItem(DEFAULT_LABEL))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].enabled")
            .value(hasItem(DEFAULT_ENABLED.booleanValue()))
            .jsonPath("$.[*].fixedValue")
            .value(hasItem(DEFAULT_FIXED_VALUE.booleanValue()))
            .jsonPath("$.[*].ordering")
            .value(hasItem(DEFAULT_ORDERING));
    }

    @Test
    void getParameter() {
        // Initialize the database
        parameter.setId(UUID.randomUUID());
        parameterRepository.save(parameter).block();

        // Get the parameter
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, parameter.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(parameter.getId().toString()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.label")
            .value(is(DEFAULT_LABEL))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.enabled")
            .value(is(DEFAULT_ENABLED.booleanValue()))
            .jsonPath("$.fixedValue")
            .value(is(DEFAULT_FIXED_VALUE.booleanValue()))
            .jsonPath("$.ordering")
            .value(is(DEFAULT_ORDERING));
    }

    @Test
    void getNonExistingParameter() {
        // Get the parameter
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingParameter() throws Exception {
        // Initialize the database
        parameter.setId(UUID.randomUUID());
        parameterRepository.save(parameter).block();

        int databaseSizeBeforeUpdate = parameterRepository.findAll().collectList().block().size();

        // Update the parameter
        Parameter updatedParameter = parameterRepository.findById(parameter.getId()).block();
        updatedParameter
            .name(UPDATED_NAME)
            .label(UPDATED_LABEL)
            .description(UPDATED_DESCRIPTION)
            .enabled(UPDATED_ENABLED)
            .fixedValue(UPDATED_FIXED_VALUE)
            .ordering(UPDATED_ORDERING);
        ParameterDTO parameterDTO = parameterMapper.toDto(updatedParameter);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, parameterDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll().collectList().block();
        assertThat(parameterList).hasSize(databaseSizeBeforeUpdate);
        Parameter testParameter = parameterList.get(parameterList.size() - 1);
        assertThat(testParameter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testParameter.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testParameter.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testParameter.getEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testParameter.getFixedValue()).isEqualTo(UPDATED_FIXED_VALUE);
        assertThat(testParameter.getOrdering()).isEqualTo(UPDATED_ORDERING);
    }

    @Test
    void putNonExistingParameter() throws Exception {
        int databaseSizeBeforeUpdate = parameterRepository.findAll().collectList().block().size();
        parameter.setId(UUID.randomUUID());

        // Create the Parameter
        ParameterDTO parameterDTO = parameterMapper.toDto(parameter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, parameterDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll().collectList().block();
        assertThat(parameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchParameter() throws Exception {
        int databaseSizeBeforeUpdate = parameterRepository.findAll().collectList().block().size();
        parameter.setId(UUID.randomUUID());

        // Create the Parameter
        ParameterDTO parameterDTO = parameterMapper.toDto(parameter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll().collectList().block();
        assertThat(parameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamParameter() throws Exception {
        int databaseSizeBeforeUpdate = parameterRepository.findAll().collectList().block().size();
        parameter.setId(UUID.randomUUID());

        // Create the Parameter
        ParameterDTO parameterDTO = parameterMapper.toDto(parameter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll().collectList().block();
        assertThat(parameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateParameterWithPatch() throws Exception {
        // Initialize the database
        parameter.setId(UUID.randomUUID());
        parameterRepository.save(parameter).block();

        int databaseSizeBeforeUpdate = parameterRepository.findAll().collectList().block().size();

        // Update the parameter using partial update
        Parameter partialUpdatedParameter = new Parameter();
        partialUpdatedParameter.setId(parameter.getId());

        partialUpdatedParameter
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .fixedValue(UPDATED_FIXED_VALUE)
            .ordering(UPDATED_ORDERING);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedParameter.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedParameter))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll().collectList().block();
        assertThat(parameterList).hasSize(databaseSizeBeforeUpdate);
        Parameter testParameter = parameterList.get(parameterList.size() - 1);
        assertThat(testParameter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testParameter.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testParameter.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testParameter.getEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testParameter.getFixedValue()).isEqualTo(UPDATED_FIXED_VALUE);
        assertThat(testParameter.getOrdering()).isEqualTo(UPDATED_ORDERING);
    }

    @Test
    void fullUpdateParameterWithPatch() throws Exception {
        // Initialize the database
        parameter.setId(UUID.randomUUID());
        parameterRepository.save(parameter).block();

        int databaseSizeBeforeUpdate = parameterRepository.findAll().collectList().block().size();

        // Update the parameter using partial update
        Parameter partialUpdatedParameter = new Parameter();
        partialUpdatedParameter.setId(parameter.getId());

        partialUpdatedParameter
            .name(UPDATED_NAME)
            .label(UPDATED_LABEL)
            .description(UPDATED_DESCRIPTION)
            .enabled(UPDATED_ENABLED)
            .fixedValue(UPDATED_FIXED_VALUE)
            .ordering(UPDATED_ORDERING);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedParameter.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedParameter))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll().collectList().block();
        assertThat(parameterList).hasSize(databaseSizeBeforeUpdate);
        Parameter testParameter = parameterList.get(parameterList.size() - 1);
        assertThat(testParameter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testParameter.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testParameter.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testParameter.getEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testParameter.getFixedValue()).isEqualTo(UPDATED_FIXED_VALUE);
        assertThat(testParameter.getOrdering()).isEqualTo(UPDATED_ORDERING);
    }

    @Test
    void patchNonExistingParameter() throws Exception {
        int databaseSizeBeforeUpdate = parameterRepository.findAll().collectList().block().size();
        parameter.setId(UUID.randomUUID());

        // Create the Parameter
        ParameterDTO parameterDTO = parameterMapper.toDto(parameter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, parameterDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll().collectList().block();
        assertThat(parameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchParameter() throws Exception {
        int databaseSizeBeforeUpdate = parameterRepository.findAll().collectList().block().size();
        parameter.setId(UUID.randomUUID());

        // Create the Parameter
        ParameterDTO parameterDTO = parameterMapper.toDto(parameter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll().collectList().block();
        assertThat(parameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamParameter() throws Exception {
        int databaseSizeBeforeUpdate = parameterRepository.findAll().collectList().block().size();
        parameter.setId(UUID.randomUUID());

        // Create the Parameter
        ParameterDTO parameterDTO = parameterMapper.toDto(parameter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll().collectList().block();
        assertThat(parameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteParameter() {
        // Initialize the database
        parameter.setId(UUID.randomUUID());
        parameterRepository.save(parameter).block();

        int databaseSizeBeforeDelete = parameterRepository.findAll().collectList().block().size();

        // Delete the parameter
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, parameter.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Parameter> parameterList = parameterRepository.findAll().collectList().block();
        assertThat(parameterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
