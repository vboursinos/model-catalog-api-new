package ai.turintech.catalog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import ai.turintech.catalog.IntegrationTest;
import ai.turintech.catalog.domain.IntegerParameterValue;
import ai.turintech.catalog.repository.EntityManager;
import ai.turintech.catalog.repository.IntegerParameterValueRepository;
import ai.turintech.catalog.service.dto.IntegerParameterValueDTO;
import ai.turintech.catalog.service.mapper.IntegerParameterValueMapper;
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
 * Integration tests for the {@link IntegerParameterValueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class IntegerParameterValueResourceIT {

    private static final Integer DEFAULT_LOWER = 1;
    private static final Integer UPDATED_LOWER = 2;

    private static final Integer DEFAULT_UPPER = 1;
    private static final Integer UPDATED_UPPER = 2;

    private static final String ENTITY_API_URL = "/api/integer-parameter-values";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IntegerParameterValueRepository integerParameterValueRepository;

    @Autowired
    private IntegerParameterValueMapper integerParameterValueMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private IntegerParameterValue integerParameterValue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IntegerParameterValue createEntity(EntityManager em) {
        IntegerParameterValue integerParameterValue = new IntegerParameterValue().lower(DEFAULT_LOWER).upper(DEFAULT_UPPER);
        return integerParameterValue;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IntegerParameterValue createUpdatedEntity(EntityManager em) {
        IntegerParameterValue integerParameterValue = new IntegerParameterValue().lower(UPDATED_LOWER).upper(UPDATED_UPPER);
        return integerParameterValue;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(IntegerParameterValue.class).block();
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
        integerParameterValue = createEntity(em);
    }

    @Test
    void createIntegerParameterValue() throws Exception {
        int databaseSizeBeforeCreate = integerParameterValueRepository.findAll().collectList().block().size();
        // Create the IntegerParameterValue
        IntegerParameterValueDTO integerParameterValueDTO = integerParameterValueMapper.toDto(integerParameterValue);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(integerParameterValueDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the IntegerParameterValue in the database
        List<IntegerParameterValue> integerParameterValueList = integerParameterValueRepository.findAll().collectList().block();
        assertThat(integerParameterValueList).hasSize(databaseSizeBeforeCreate + 1);
        IntegerParameterValue testIntegerParameterValue = integerParameterValueList.get(integerParameterValueList.size() - 1);
        assertThat(testIntegerParameterValue.getLower()).isEqualTo(DEFAULT_LOWER);
        assertThat(testIntegerParameterValue.getUpper()).isEqualTo(DEFAULT_UPPER);
    }

    @Test
    void createIntegerParameterValueWithExistingId() throws Exception {
        // Create the IntegerParameterValue with an existing ID
        integerParameterValue.setId(1L);
        IntegerParameterValueDTO integerParameterValueDTO = integerParameterValueMapper.toDto(integerParameterValue);

        int databaseSizeBeforeCreate = integerParameterValueRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(integerParameterValueDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IntegerParameterValue in the database
        List<IntegerParameterValue> integerParameterValueList = integerParameterValueRepository.findAll().collectList().block();
        assertThat(integerParameterValueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkLowerIsRequired() throws Exception {
        int databaseSizeBeforeTest = integerParameterValueRepository.findAll().collectList().block().size();
        // set the field null
        integerParameterValue.setLower(null);

        // Create the IntegerParameterValue, which fails.
        IntegerParameterValueDTO integerParameterValueDTO = integerParameterValueMapper.toDto(integerParameterValue);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(integerParameterValueDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<IntegerParameterValue> integerParameterValueList = integerParameterValueRepository.findAll().collectList().block();
        assertThat(integerParameterValueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkUpperIsRequired() throws Exception {
        int databaseSizeBeforeTest = integerParameterValueRepository.findAll().collectList().block().size();
        // set the field null
        integerParameterValue.setUpper(null);

        // Create the IntegerParameterValue, which fails.
        IntegerParameterValueDTO integerParameterValueDTO = integerParameterValueMapper.toDto(integerParameterValue);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(integerParameterValueDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<IntegerParameterValue> integerParameterValueList = integerParameterValueRepository.findAll().collectList().block();
        assertThat(integerParameterValueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllIntegerParameterValuesAsStream() {
        // Initialize the database
        integerParameterValueRepository.save(integerParameterValue).block();

        List<IntegerParameterValue> integerParameterValueList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(IntegerParameterValueDTO.class)
            .getResponseBody()
            .map(integerParameterValueMapper::toEntity)
            .filter(integerParameterValue::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(integerParameterValueList).isNotNull();
        assertThat(integerParameterValueList).hasSize(1);
        IntegerParameterValue testIntegerParameterValue = integerParameterValueList.get(0);
        assertThat(testIntegerParameterValue.getLower()).isEqualTo(DEFAULT_LOWER);
        assertThat(testIntegerParameterValue.getUpper()).isEqualTo(DEFAULT_UPPER);
    }

    @Test
    void getAllIntegerParameterValues() {
        // Initialize the database
        integerParameterValueRepository.save(integerParameterValue).block();

        // Get all the integerParameterValueList
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
            .value(hasItem(integerParameterValue.getId().intValue()))
            .jsonPath("$.[*].lower")
            .value(hasItem(DEFAULT_LOWER))
            .jsonPath("$.[*].upper")
            .value(hasItem(DEFAULT_UPPER));
    }

    @Test
    void getIntegerParameterValue() {
        // Initialize the database
        integerParameterValueRepository.save(integerParameterValue).block();

        // Get the integerParameterValue
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, integerParameterValue.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(integerParameterValue.getId().intValue()))
            .jsonPath("$.lower")
            .value(is(DEFAULT_LOWER))
            .jsonPath("$.upper")
            .value(is(DEFAULT_UPPER));
    }

    @Test
    void getNonExistingIntegerParameterValue() {
        // Get the integerParameterValue
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingIntegerParameterValue() throws Exception {
        // Initialize the database
        integerParameterValueRepository.save(integerParameterValue).block();

        int databaseSizeBeforeUpdate = integerParameterValueRepository.findAll().collectList().block().size();

        // Update the integerParameterValue
        IntegerParameterValue updatedIntegerParameterValue = integerParameterValueRepository
            .findById(integerParameterValue.getId())
            .block();
        updatedIntegerParameterValue.lower(UPDATED_LOWER).upper(UPDATED_UPPER);
        IntegerParameterValueDTO integerParameterValueDTO = integerParameterValueMapper.toDto(updatedIntegerParameterValue);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, integerParameterValueDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(integerParameterValueDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IntegerParameterValue in the database
        List<IntegerParameterValue> integerParameterValueList = integerParameterValueRepository.findAll().collectList().block();
        assertThat(integerParameterValueList).hasSize(databaseSizeBeforeUpdate);
        IntegerParameterValue testIntegerParameterValue = integerParameterValueList.get(integerParameterValueList.size() - 1);
        assertThat(testIntegerParameterValue.getLower()).isEqualTo(UPDATED_LOWER);
        assertThat(testIntegerParameterValue.getUpper()).isEqualTo(UPDATED_UPPER);
    }

    @Test
    void putNonExistingIntegerParameterValue() throws Exception {
        int databaseSizeBeforeUpdate = integerParameterValueRepository.findAll().collectList().block().size();
        integerParameterValue.setId(count.incrementAndGet());

        // Create the IntegerParameterValue
        IntegerParameterValueDTO integerParameterValueDTO = integerParameterValueMapper.toDto(integerParameterValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, integerParameterValueDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(integerParameterValueDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IntegerParameterValue in the database
        List<IntegerParameterValue> integerParameterValueList = integerParameterValueRepository.findAll().collectList().block();
        assertThat(integerParameterValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchIntegerParameterValue() throws Exception {
        int databaseSizeBeforeUpdate = integerParameterValueRepository.findAll().collectList().block().size();
        integerParameterValue.setId(count.incrementAndGet());

        // Create the IntegerParameterValue
        IntegerParameterValueDTO integerParameterValueDTO = integerParameterValueMapper.toDto(integerParameterValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(integerParameterValueDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IntegerParameterValue in the database
        List<IntegerParameterValue> integerParameterValueList = integerParameterValueRepository.findAll().collectList().block();
        assertThat(integerParameterValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamIntegerParameterValue() throws Exception {
        int databaseSizeBeforeUpdate = integerParameterValueRepository.findAll().collectList().block().size();
        integerParameterValue.setId(count.incrementAndGet());

        // Create the IntegerParameterValue
        IntegerParameterValueDTO integerParameterValueDTO = integerParameterValueMapper.toDto(integerParameterValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(integerParameterValueDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IntegerParameterValue in the database
        List<IntegerParameterValue> integerParameterValueList = integerParameterValueRepository.findAll().collectList().block();
        assertThat(integerParameterValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateIntegerParameterValueWithPatch() throws Exception {
        // Initialize the database
        integerParameterValueRepository.save(integerParameterValue).block();

        int databaseSizeBeforeUpdate = integerParameterValueRepository.findAll().collectList().block().size();

        // Update the integerParameterValue using partial update
        IntegerParameterValue partialUpdatedIntegerParameterValue = new IntegerParameterValue();
        partialUpdatedIntegerParameterValue.setId(integerParameterValue.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIntegerParameterValue.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIntegerParameterValue))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IntegerParameterValue in the database
        List<IntegerParameterValue> integerParameterValueList = integerParameterValueRepository.findAll().collectList().block();
        assertThat(integerParameterValueList).hasSize(databaseSizeBeforeUpdate);
        IntegerParameterValue testIntegerParameterValue = integerParameterValueList.get(integerParameterValueList.size() - 1);
        assertThat(testIntegerParameterValue.getLower()).isEqualTo(DEFAULT_LOWER);
        assertThat(testIntegerParameterValue.getUpper()).isEqualTo(DEFAULT_UPPER);
    }

    @Test
    void fullUpdateIntegerParameterValueWithPatch() throws Exception {
        // Initialize the database
        integerParameterValueRepository.save(integerParameterValue).block();

        int databaseSizeBeforeUpdate = integerParameterValueRepository.findAll().collectList().block().size();

        // Update the integerParameterValue using partial update
        IntegerParameterValue partialUpdatedIntegerParameterValue = new IntegerParameterValue();
        partialUpdatedIntegerParameterValue.setId(integerParameterValue.getId());

        partialUpdatedIntegerParameterValue.lower(UPDATED_LOWER).upper(UPDATED_UPPER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIntegerParameterValue.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIntegerParameterValue))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IntegerParameterValue in the database
        List<IntegerParameterValue> integerParameterValueList = integerParameterValueRepository.findAll().collectList().block();
        assertThat(integerParameterValueList).hasSize(databaseSizeBeforeUpdate);
        IntegerParameterValue testIntegerParameterValue = integerParameterValueList.get(integerParameterValueList.size() - 1);
        assertThat(testIntegerParameterValue.getLower()).isEqualTo(UPDATED_LOWER);
        assertThat(testIntegerParameterValue.getUpper()).isEqualTo(UPDATED_UPPER);
    }

    @Test
    void patchNonExistingIntegerParameterValue() throws Exception {
        int databaseSizeBeforeUpdate = integerParameterValueRepository.findAll().collectList().block().size();
        integerParameterValue.setId(count.incrementAndGet());

        // Create the IntegerParameterValue
        IntegerParameterValueDTO integerParameterValueDTO = integerParameterValueMapper.toDto(integerParameterValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, integerParameterValueDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(integerParameterValueDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IntegerParameterValue in the database
        List<IntegerParameterValue> integerParameterValueList = integerParameterValueRepository.findAll().collectList().block();
        assertThat(integerParameterValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchIntegerParameterValue() throws Exception {
        int databaseSizeBeforeUpdate = integerParameterValueRepository.findAll().collectList().block().size();
        integerParameterValue.setId(count.incrementAndGet());

        // Create the IntegerParameterValue
        IntegerParameterValueDTO integerParameterValueDTO = integerParameterValueMapper.toDto(integerParameterValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(integerParameterValueDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IntegerParameterValue in the database
        List<IntegerParameterValue> integerParameterValueList = integerParameterValueRepository.findAll().collectList().block();
        assertThat(integerParameterValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamIntegerParameterValue() throws Exception {
        int databaseSizeBeforeUpdate = integerParameterValueRepository.findAll().collectList().block().size();
        integerParameterValue.setId(count.incrementAndGet());

        // Create the IntegerParameterValue
        IntegerParameterValueDTO integerParameterValueDTO = integerParameterValueMapper.toDto(integerParameterValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(integerParameterValueDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IntegerParameterValue in the database
        List<IntegerParameterValue> integerParameterValueList = integerParameterValueRepository.findAll().collectList().block();
        assertThat(integerParameterValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteIntegerParameterValue() {
        // Initialize the database
        integerParameterValueRepository.save(integerParameterValue).block();

        int databaseSizeBeforeDelete = integerParameterValueRepository.findAll().collectList().block().size();

        // Delete the integerParameterValue
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, integerParameterValue.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<IntegerParameterValue> integerParameterValueList = integerParameterValueRepository.findAll().collectList().block();
        assertThat(integerParameterValueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
