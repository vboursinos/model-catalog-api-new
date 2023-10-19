package ai.turintech.catalog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import ai.turintech.catalog.IntegrationTest;
import ai.turintech.catalog.domain.IntegerParameter;
import ai.turintech.catalog.repository.EntityManager;
import ai.turintech.catalog.repository.IntegerParameterRepository;
import ai.turintech.catalog.service.dto.IntegerParameterDTO;
import ai.turintech.catalog.service.mapper.IntegerParameterMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link IntegerParameterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class IntegerParameterResourceIT {

    private static final Integer DEFAULT_DEFAULT_VALUE = 1;
    private static final Integer UPDATED_DEFAULT_VALUE = 2;

    private static final String ENTITY_API_URL = "/api/integer-parameters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IntegerParameterRepository integerParameterRepository;

    @Autowired
    private IntegerParameterMapper integerParameterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private IntegerParameter integerParameter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IntegerParameter createEntity(EntityManager em) {
        IntegerParameter integerParameter = new IntegerParameter().defaultValue(DEFAULT_DEFAULT_VALUE);
        return integerParameter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IntegerParameter createUpdatedEntity(EntityManager em) {
        IntegerParameter integerParameter = new IntegerParameter().defaultValue(UPDATED_DEFAULT_VALUE);
        return integerParameter;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(IntegerParameter.class).block();
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
        integerParameter = createEntity(em);
    }

    @Test
    void createIntegerParameter() throws Exception {
        int databaseSizeBeforeCreate = integerParameterRepository.findAll().collectList().block().size();
        // Create the IntegerParameter
        IntegerParameterDTO integerParameterDTO = integerParameterMapper.toDto(integerParameter);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(integerParameterDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the IntegerParameter in the database
        List<IntegerParameter> integerParameterList = integerParameterRepository.findAll().collectList().block();
        assertThat(integerParameterList).hasSize(databaseSizeBeforeCreate + 1);
        IntegerParameter testIntegerParameter = integerParameterList.get(integerParameterList.size() - 1);
        assertThat(testIntegerParameter.getDefaultValue()).isEqualTo(DEFAULT_DEFAULT_VALUE);
    }

    @Test
    void createIntegerParameterWithExistingId() throws Exception {
        // Create the IntegerParameter with an existing ID
        integerParameter.setId(1L);
        IntegerParameterDTO integerParameterDTO = integerParameterMapper.toDto(integerParameter);

        int databaseSizeBeforeCreate = integerParameterRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(integerParameterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IntegerParameter in the database
        List<IntegerParameter> integerParameterList = integerParameterRepository.findAll().collectList().block();
        assertThat(integerParameterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllIntegerParametersAsStream() {
        // Initialize the database
        integerParameterRepository.save(integerParameter).block();

        List<IntegerParameter> integerParameterList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(IntegerParameterDTO.class)
            .getResponseBody()
            .map(integerParameterMapper::toEntity)
            .filter(integerParameter::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(integerParameterList).isNotNull();
        assertThat(integerParameterList).hasSize(1);
        IntegerParameter testIntegerParameter = integerParameterList.get(0);
        assertThat(testIntegerParameter.getDefaultValue()).isEqualTo(DEFAULT_DEFAULT_VALUE);
    }

    @Test
    void getAllIntegerParameters() {
        // Initialize the database
        integerParameterRepository.save(integerParameter).block();

        // Get all the integerParameterList
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
            .value(hasItem(integerParameter.getId().intValue()))
            .jsonPath("$.[*].defaultValue")
            .value(hasItem(DEFAULT_DEFAULT_VALUE));
    }

    @Test
    void getIntegerParameter() {
        // Initialize the database
        integerParameterRepository.save(integerParameter).block();

        // Get the integerParameter
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, integerParameter.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(integerParameter.getId().intValue()))
            .jsonPath("$.defaultValue")
            .value(is(DEFAULT_DEFAULT_VALUE));
    }

    @Test
    void getNonExistingIntegerParameter() {
        // Get the integerParameter
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingIntegerParameter() throws Exception {
        // Initialize the database
        integerParameterRepository.save(integerParameter).block();

        int databaseSizeBeforeUpdate = integerParameterRepository.findAll().collectList().block().size();

        // Update the integerParameter
        IntegerParameter updatedIntegerParameter = integerParameterRepository.findById(integerParameter.getId()).block();
        updatedIntegerParameter.defaultValue(UPDATED_DEFAULT_VALUE);
        IntegerParameterDTO integerParameterDTO = integerParameterMapper.toDto(updatedIntegerParameter);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, integerParameterDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(integerParameterDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IntegerParameter in the database
        List<IntegerParameter> integerParameterList = integerParameterRepository.findAll().collectList().block();
        assertThat(integerParameterList).hasSize(databaseSizeBeforeUpdate);
        IntegerParameter testIntegerParameter = integerParameterList.get(integerParameterList.size() - 1);
        assertThat(testIntegerParameter.getDefaultValue()).isEqualTo(UPDATED_DEFAULT_VALUE);
    }

    @Test
    void putNonExistingIntegerParameter() throws Exception {
        int databaseSizeBeforeUpdate = integerParameterRepository.findAll().collectList().block().size();
        integerParameter.setId(count.incrementAndGet());

        // Create the IntegerParameter
        IntegerParameterDTO integerParameterDTO = integerParameterMapper.toDto(integerParameter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, integerParameterDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(integerParameterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IntegerParameter in the database
        List<IntegerParameter> integerParameterList = integerParameterRepository.findAll().collectList().block();
        assertThat(integerParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchIntegerParameter() throws Exception {
        int databaseSizeBeforeUpdate = integerParameterRepository.findAll().collectList().block().size();
        integerParameter.setId(count.incrementAndGet());

        // Create the IntegerParameter
        IntegerParameterDTO integerParameterDTO = integerParameterMapper.toDto(integerParameter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(integerParameterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IntegerParameter in the database
        List<IntegerParameter> integerParameterList = integerParameterRepository.findAll().collectList().block();
        assertThat(integerParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamIntegerParameter() throws Exception {
        int databaseSizeBeforeUpdate = integerParameterRepository.findAll().collectList().block().size();
        integerParameter.setId(count.incrementAndGet());

        // Create the IntegerParameter
        IntegerParameterDTO integerParameterDTO = integerParameterMapper.toDto(integerParameter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(integerParameterDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IntegerParameter in the database
        List<IntegerParameter> integerParameterList = integerParameterRepository.findAll().collectList().block();
        assertThat(integerParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateIntegerParameterWithPatch() throws Exception {
        // Initialize the database
        integerParameterRepository.save(integerParameter).block();

        int databaseSizeBeforeUpdate = integerParameterRepository.findAll().collectList().block().size();

        // Update the integerParameter using partial update
        IntegerParameter partialUpdatedIntegerParameter = new IntegerParameter();
        partialUpdatedIntegerParameter.setId(integerParameter.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIntegerParameter.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIntegerParameter))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IntegerParameter in the database
        List<IntegerParameter> integerParameterList = integerParameterRepository.findAll().collectList().block();
        assertThat(integerParameterList).hasSize(databaseSizeBeforeUpdate);
        IntegerParameter testIntegerParameter = integerParameterList.get(integerParameterList.size() - 1);
        assertThat(testIntegerParameter.getDefaultValue()).isEqualTo(DEFAULT_DEFAULT_VALUE);
    }

    @Test
    void fullUpdateIntegerParameterWithPatch() throws Exception {
        // Initialize the database
        integerParameterRepository.save(integerParameter).block();

        int databaseSizeBeforeUpdate = integerParameterRepository.findAll().collectList().block().size();

        // Update the integerParameter using partial update
        IntegerParameter partialUpdatedIntegerParameter = new IntegerParameter();
        partialUpdatedIntegerParameter.setId(integerParameter.getId());

        partialUpdatedIntegerParameter.defaultValue(UPDATED_DEFAULT_VALUE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIntegerParameter.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIntegerParameter))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IntegerParameter in the database
        List<IntegerParameter> integerParameterList = integerParameterRepository.findAll().collectList().block();
        assertThat(integerParameterList).hasSize(databaseSizeBeforeUpdate);
        IntegerParameter testIntegerParameter = integerParameterList.get(integerParameterList.size() - 1);
        assertThat(testIntegerParameter.getDefaultValue()).isEqualTo(UPDATED_DEFAULT_VALUE);
    }

    @Test
    void patchNonExistingIntegerParameter() throws Exception {
        int databaseSizeBeforeUpdate = integerParameterRepository.findAll().collectList().block().size();
        integerParameter.setId(count.incrementAndGet());

        // Create the IntegerParameter
        IntegerParameterDTO integerParameterDTO = integerParameterMapper.toDto(integerParameter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, integerParameterDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(integerParameterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IntegerParameter in the database
        List<IntegerParameter> integerParameterList = integerParameterRepository.findAll().collectList().block();
        assertThat(integerParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchIntegerParameter() throws Exception {
        int databaseSizeBeforeUpdate = integerParameterRepository.findAll().collectList().block().size();
        integerParameter.setId(count.incrementAndGet());

        // Create the IntegerParameter
        IntegerParameterDTO integerParameterDTO = integerParameterMapper.toDto(integerParameter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(integerParameterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IntegerParameter in the database
        List<IntegerParameter> integerParameterList = integerParameterRepository.findAll().collectList().block();
        assertThat(integerParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamIntegerParameter() throws Exception {
        int databaseSizeBeforeUpdate = integerParameterRepository.findAll().collectList().block().size();
        integerParameter.setId(count.incrementAndGet());

        // Create the IntegerParameter
        IntegerParameterDTO integerParameterDTO = integerParameterMapper.toDto(integerParameter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(integerParameterDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IntegerParameter in the database
        List<IntegerParameter> integerParameterList = integerParameterRepository.findAll().collectList().block();
        assertThat(integerParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteIntegerParameter() {
        // Initialize the database
        integerParameterRepository.save(integerParameter).block();

        int databaseSizeBeforeDelete = integerParameterRepository.findAll().collectList().block().size();

        // Delete the integerParameter
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, integerParameter.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<IntegerParameter> integerParameterList = integerParameterRepository.findAll().collectList().block();
        assertThat(integerParameterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
