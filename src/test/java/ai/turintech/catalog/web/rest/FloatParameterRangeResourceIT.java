package ai.turintech.catalog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import ai.turintech.catalog.IntegrationTest;
import ai.turintech.catalog.domain.FloatParameterRange;
import ai.turintech.catalog.repository2.EntityManager;
import ai.turintech.catalog.repository2.FloatParameterRangeRepository;
import ai.turintech.catalog.service.dto.FloatParameterRangeDTO;
import ai.turintech.catalog.service.mapper.FloatParameterRangeMapper;
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
 * Integration tests for the {@link FloatParameterRangeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class FloatParameterRangeResourceIT {

    private static final Boolean DEFAULT_IS_LEFT_OPEN = false;
    private static final Boolean UPDATED_IS_LEFT_OPEN = true;

    private static final Boolean DEFAULT_IS_RIGHT_OPEN = false;
    private static final Boolean UPDATED_IS_RIGHT_OPEN = true;

    private static final Double DEFAULT_LOWER = 1D;
    private static final Double UPDATED_LOWER = 2D;

    private static final Double DEFAULT_UPPER = 1D;
    private static final Double UPDATED_UPPER = 2D;

    private static final String ENTITY_API_URL = "/api/float-parameter-ranges";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FloatParameterRangeRepository floatParameterRangeRepository;

    @Autowired
    private FloatParameterRangeMapper floatParameterRangeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private FloatParameterRange floatParameterRange;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FloatParameterRange createEntity(EntityManager em) {
        FloatParameterRange floatParameterRange = new FloatParameterRange()
            .isLeftOpen(DEFAULT_IS_LEFT_OPEN)
            .isRightOpen(DEFAULT_IS_RIGHT_OPEN)
            .lower(DEFAULT_LOWER)
            .upper(DEFAULT_UPPER);
        return floatParameterRange;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FloatParameterRange createUpdatedEntity(EntityManager em) {
        FloatParameterRange floatParameterRange = new FloatParameterRange()
            .isLeftOpen(UPDATED_IS_LEFT_OPEN)
            .isRightOpen(UPDATED_IS_RIGHT_OPEN)
            .lower(UPDATED_LOWER)
            .upper(UPDATED_UPPER);
        return floatParameterRange;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(FloatParameterRange.class).block();
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
        floatParameterRange = createEntity(em);
    }

    @Test
    void createFloatParameterRange() throws Exception {
        int databaseSizeBeforeCreate = floatParameterRangeRepository.findAll().collectList().block().size();
        // Create the FloatParameterRange
        FloatParameterRangeDTO floatParameterRangeDTO = floatParameterRangeMapper.toDto(floatParameterRange);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(floatParameterRangeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the FloatParameterRange in the database
        List<FloatParameterRange> floatParameterRangeList = floatParameterRangeRepository.findAll().collectList().block();
        assertThat(floatParameterRangeList).hasSize(databaseSizeBeforeCreate + 1);
        FloatParameterRange testFloatParameterRange = floatParameterRangeList.get(floatParameterRangeList.size() - 1);
        assertThat(testFloatParameterRange.getIsLeftOpen()).isEqualTo(DEFAULT_IS_LEFT_OPEN);
        assertThat(testFloatParameterRange.getIsRightOpen()).isEqualTo(DEFAULT_IS_RIGHT_OPEN);
        assertThat(testFloatParameterRange.getLower()).isEqualTo(DEFAULT_LOWER);
        assertThat(testFloatParameterRange.getUpper()).isEqualTo(DEFAULT_UPPER);
    }

    @Test
    void createFloatParameterRangeWithExistingId() throws Exception {
        // Create the FloatParameterRange with an existing ID
        floatParameterRange.setId(1L);
        FloatParameterRangeDTO floatParameterRangeDTO = floatParameterRangeMapper.toDto(floatParameterRange);

        int databaseSizeBeforeCreate = floatParameterRangeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(floatParameterRangeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FloatParameterRange in the database
        List<FloatParameterRange> floatParameterRangeList = floatParameterRangeRepository.findAll().collectList().block();
        assertThat(floatParameterRangeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkIsLeftOpenIsRequired() throws Exception {
        int databaseSizeBeforeTest = floatParameterRangeRepository.findAll().collectList().block().size();
        // set the field null
        floatParameterRange.setIsLeftOpen(null);

        // Create the FloatParameterRange, which fails.
        FloatParameterRangeDTO floatParameterRangeDTO = floatParameterRangeMapper.toDto(floatParameterRange);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(floatParameterRangeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<FloatParameterRange> floatParameterRangeList = floatParameterRangeRepository.findAll().collectList().block();
        assertThat(floatParameterRangeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIsRightOpenIsRequired() throws Exception {
        int databaseSizeBeforeTest = floatParameterRangeRepository.findAll().collectList().block().size();
        // set the field null
        floatParameterRange.setIsRightOpen(null);

        // Create the FloatParameterRange, which fails.
        FloatParameterRangeDTO floatParameterRangeDTO = floatParameterRangeMapper.toDto(floatParameterRange);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(floatParameterRangeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<FloatParameterRange> floatParameterRangeList = floatParameterRangeRepository.findAll().collectList().block();
        assertThat(floatParameterRangeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkLowerIsRequired() throws Exception {
        int databaseSizeBeforeTest = floatParameterRangeRepository.findAll().collectList().block().size();
        // set the field null
        floatParameterRange.setLower(null);

        // Create the FloatParameterRange, which fails.
        FloatParameterRangeDTO floatParameterRangeDTO = floatParameterRangeMapper.toDto(floatParameterRange);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(floatParameterRangeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<FloatParameterRange> floatParameterRangeList = floatParameterRangeRepository.findAll().collectList().block();
        assertThat(floatParameterRangeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkUpperIsRequired() throws Exception {
        int databaseSizeBeforeTest = floatParameterRangeRepository.findAll().collectList().block().size();
        // set the field null
        floatParameterRange.setUpper(null);

        // Create the FloatParameterRange, which fails.
        FloatParameterRangeDTO floatParameterRangeDTO = floatParameterRangeMapper.toDto(floatParameterRange);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(floatParameterRangeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<FloatParameterRange> floatParameterRangeList = floatParameterRangeRepository.findAll().collectList().block();
        assertThat(floatParameterRangeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllFloatParameterRangesAsStream() {
        // Initialize the database
        floatParameterRangeRepository.save(floatParameterRange).block();

        List<FloatParameterRange> floatParameterRangeList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(FloatParameterRangeDTO.class)
            .getResponseBody()
            .map(floatParameterRangeMapper::toEntity)
            .filter(floatParameterRange::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(floatParameterRangeList).isNotNull();
        assertThat(floatParameterRangeList).hasSize(1);
        FloatParameterRange testFloatParameterRange = floatParameterRangeList.get(0);
        assertThat(testFloatParameterRange.getIsLeftOpen()).isEqualTo(DEFAULT_IS_LEFT_OPEN);
        assertThat(testFloatParameterRange.getIsRightOpen()).isEqualTo(DEFAULT_IS_RIGHT_OPEN);
        assertThat(testFloatParameterRange.getLower()).isEqualTo(DEFAULT_LOWER);
        assertThat(testFloatParameterRange.getUpper()).isEqualTo(DEFAULT_UPPER);
    }

    @Test
    void getAllFloatParameterRanges() {
        // Initialize the database
        floatParameterRangeRepository.save(floatParameterRange).block();

        // Get all the floatParameterRangeList
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
            .value(hasItem(floatParameterRange.getId().intValue()))
            .jsonPath("$.[*].isLeftOpen")
            .value(hasItem(DEFAULT_IS_LEFT_OPEN.booleanValue()))
            .jsonPath("$.[*].isRightOpen")
            .value(hasItem(DEFAULT_IS_RIGHT_OPEN.booleanValue()))
            .jsonPath("$.[*].lower")
            .value(hasItem(DEFAULT_LOWER.doubleValue()))
            .jsonPath("$.[*].upper")
            .value(hasItem(DEFAULT_UPPER.doubleValue()));
    }

    @Test
    void getFloatParameterRange() {
        // Initialize the database
        floatParameterRangeRepository.save(floatParameterRange).block();

        // Get the floatParameterRange
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, floatParameterRange.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(floatParameterRange.getId().intValue()))
            .jsonPath("$.isLeftOpen")
            .value(is(DEFAULT_IS_LEFT_OPEN.booleanValue()))
            .jsonPath("$.isRightOpen")
            .value(is(DEFAULT_IS_RIGHT_OPEN.booleanValue()))
            .jsonPath("$.lower")
            .value(is(DEFAULT_LOWER.doubleValue()))
            .jsonPath("$.upper")
            .value(is(DEFAULT_UPPER.doubleValue()));
    }

    @Test
    void getNonExistingFloatParameterRange() {
        // Get the floatParameterRange
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingFloatParameterRange() throws Exception {
        // Initialize the database
        floatParameterRangeRepository.save(floatParameterRange).block();

        int databaseSizeBeforeUpdate = floatParameterRangeRepository.findAll().collectList().block().size();

        // Update the floatParameterRange
        FloatParameterRange updatedFloatParameterRange = floatParameterRangeRepository.findById(floatParameterRange.getId()).block();
        updatedFloatParameterRange
            .isLeftOpen(UPDATED_IS_LEFT_OPEN)
            .isRightOpen(UPDATED_IS_RIGHT_OPEN)
            .lower(UPDATED_LOWER)
            .upper(UPDATED_UPPER);
        FloatParameterRangeDTO floatParameterRangeDTO = floatParameterRangeMapper.toDto(updatedFloatParameterRange);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, floatParameterRangeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(floatParameterRangeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FloatParameterRange in the database
        List<FloatParameterRange> floatParameterRangeList = floatParameterRangeRepository.findAll().collectList().block();
        assertThat(floatParameterRangeList).hasSize(databaseSizeBeforeUpdate);
        FloatParameterRange testFloatParameterRange = floatParameterRangeList.get(floatParameterRangeList.size() - 1);
        assertThat(testFloatParameterRange.getIsLeftOpen()).isEqualTo(UPDATED_IS_LEFT_OPEN);
        assertThat(testFloatParameterRange.getIsRightOpen()).isEqualTo(UPDATED_IS_RIGHT_OPEN);
        assertThat(testFloatParameterRange.getLower()).isEqualTo(UPDATED_LOWER);
        assertThat(testFloatParameterRange.getUpper()).isEqualTo(UPDATED_UPPER);
    }

    @Test
    void putNonExistingFloatParameterRange() throws Exception {
        int databaseSizeBeforeUpdate = floatParameterRangeRepository.findAll().collectList().block().size();
        floatParameterRange.setId(count.incrementAndGet());

        // Create the FloatParameterRange
        FloatParameterRangeDTO floatParameterRangeDTO = floatParameterRangeMapper.toDto(floatParameterRange);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, floatParameterRangeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(floatParameterRangeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FloatParameterRange in the database
        List<FloatParameterRange> floatParameterRangeList = floatParameterRangeRepository.findAll().collectList().block();
        assertThat(floatParameterRangeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFloatParameterRange() throws Exception {
        int databaseSizeBeforeUpdate = floatParameterRangeRepository.findAll().collectList().block().size();
        floatParameterRange.setId(count.incrementAndGet());

        // Create the FloatParameterRange
        FloatParameterRangeDTO floatParameterRangeDTO = floatParameterRangeMapper.toDto(floatParameterRange);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(floatParameterRangeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FloatParameterRange in the database
        List<FloatParameterRange> floatParameterRangeList = floatParameterRangeRepository.findAll().collectList().block();
        assertThat(floatParameterRangeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFloatParameterRange() throws Exception {
        int databaseSizeBeforeUpdate = floatParameterRangeRepository.findAll().collectList().block().size();
        floatParameterRange.setId(count.incrementAndGet());

        // Create the FloatParameterRange
        FloatParameterRangeDTO floatParameterRangeDTO = floatParameterRangeMapper.toDto(floatParameterRange);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(floatParameterRangeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FloatParameterRange in the database
        List<FloatParameterRange> floatParameterRangeList = floatParameterRangeRepository.findAll().collectList().block();
        assertThat(floatParameterRangeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFloatParameterRangeWithPatch() throws Exception {
        // Initialize the database
        floatParameterRangeRepository.save(floatParameterRange).block();

        int databaseSizeBeforeUpdate = floatParameterRangeRepository.findAll().collectList().block().size();

        // Update the floatParameterRange using partial update
        FloatParameterRange partialUpdatedFloatParameterRange = new FloatParameterRange();
        partialUpdatedFloatParameterRange.setId(floatParameterRange.getId());

        partialUpdatedFloatParameterRange.lower(UPDATED_LOWER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFloatParameterRange.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFloatParameterRange))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FloatParameterRange in the database
        List<FloatParameterRange> floatParameterRangeList = floatParameterRangeRepository.findAll().collectList().block();
        assertThat(floatParameterRangeList).hasSize(databaseSizeBeforeUpdate);
        FloatParameterRange testFloatParameterRange = floatParameterRangeList.get(floatParameterRangeList.size() - 1);
        assertThat(testFloatParameterRange.getIsLeftOpen()).isEqualTo(DEFAULT_IS_LEFT_OPEN);
        assertThat(testFloatParameterRange.getIsRightOpen()).isEqualTo(DEFAULT_IS_RIGHT_OPEN);
        assertThat(testFloatParameterRange.getLower()).isEqualTo(UPDATED_LOWER);
        assertThat(testFloatParameterRange.getUpper()).isEqualTo(DEFAULT_UPPER);
    }

    @Test
    void fullUpdateFloatParameterRangeWithPatch() throws Exception {
        // Initialize the database
        floatParameterRangeRepository.save(floatParameterRange).block();

        int databaseSizeBeforeUpdate = floatParameterRangeRepository.findAll().collectList().block().size();

        // Update the floatParameterRange using partial update
        FloatParameterRange partialUpdatedFloatParameterRange = new FloatParameterRange();
        partialUpdatedFloatParameterRange.setId(floatParameterRange.getId());

        partialUpdatedFloatParameterRange
            .isLeftOpen(UPDATED_IS_LEFT_OPEN)
            .isRightOpen(UPDATED_IS_RIGHT_OPEN)
            .lower(UPDATED_LOWER)
            .upper(UPDATED_UPPER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFloatParameterRange.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFloatParameterRange))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FloatParameterRange in the database
        List<FloatParameterRange> floatParameterRangeList = floatParameterRangeRepository.findAll().collectList().block();
        assertThat(floatParameterRangeList).hasSize(databaseSizeBeforeUpdate);
        FloatParameterRange testFloatParameterRange = floatParameterRangeList.get(floatParameterRangeList.size() - 1);
        assertThat(testFloatParameterRange.getIsLeftOpen()).isEqualTo(UPDATED_IS_LEFT_OPEN);
        assertThat(testFloatParameterRange.getIsRightOpen()).isEqualTo(UPDATED_IS_RIGHT_OPEN);
        assertThat(testFloatParameterRange.getLower()).isEqualTo(UPDATED_LOWER);
        assertThat(testFloatParameterRange.getUpper()).isEqualTo(UPDATED_UPPER);
    }

    @Test
    void patchNonExistingFloatParameterRange() throws Exception {
        int databaseSizeBeforeUpdate = floatParameterRangeRepository.findAll().collectList().block().size();
        floatParameterRange.setId(count.incrementAndGet());

        // Create the FloatParameterRange
        FloatParameterRangeDTO floatParameterRangeDTO = floatParameterRangeMapper.toDto(floatParameterRange);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, floatParameterRangeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(floatParameterRangeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FloatParameterRange in the database
        List<FloatParameterRange> floatParameterRangeList = floatParameterRangeRepository.findAll().collectList().block();
        assertThat(floatParameterRangeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFloatParameterRange() throws Exception {
        int databaseSizeBeforeUpdate = floatParameterRangeRepository.findAll().collectList().block().size();
        floatParameterRange.setId(count.incrementAndGet());

        // Create the FloatParameterRange
        FloatParameterRangeDTO floatParameterRangeDTO = floatParameterRangeMapper.toDto(floatParameterRange);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(floatParameterRangeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FloatParameterRange in the database
        List<FloatParameterRange> floatParameterRangeList = floatParameterRangeRepository.findAll().collectList().block();
        assertThat(floatParameterRangeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFloatParameterRange() throws Exception {
        int databaseSizeBeforeUpdate = floatParameterRangeRepository.findAll().collectList().block().size();
        floatParameterRange.setId(count.incrementAndGet());

        // Create the FloatParameterRange
        FloatParameterRangeDTO floatParameterRangeDTO = floatParameterRangeMapper.toDto(floatParameterRange);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(floatParameterRangeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FloatParameterRange in the database
        List<FloatParameterRange> floatParameterRangeList = floatParameterRangeRepository.findAll().collectList().block();
        assertThat(floatParameterRangeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFloatParameterRange() {
        // Initialize the database
        floatParameterRangeRepository.save(floatParameterRange).block();

        int databaseSizeBeforeDelete = floatParameterRangeRepository.findAll().collectList().block().size();

        // Delete the floatParameterRange
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, floatParameterRange.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<FloatParameterRange> floatParameterRangeList = floatParameterRangeRepository.findAll().collectList().block();
        assertThat(floatParameterRangeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
