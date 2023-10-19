package ai.turintech.catalog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class MlTaskTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MlTaskType.class);
        MlTaskType mlTaskType1 = new MlTaskType();
        mlTaskType1.setId(UUID.randomUUID());
        MlTaskType mlTaskType2 = new MlTaskType();
        mlTaskType2.setId(mlTaskType1.getId());
        assertThat(mlTaskType1).isEqualTo(mlTaskType2);
        mlTaskType2.setId(UUID.randomUUID());
        assertThat(mlTaskType1).isNotEqualTo(mlTaskType2);
        mlTaskType1.setId(null);
        assertThat(mlTaskType1).isNotEqualTo(mlTaskType2);
    }
}
