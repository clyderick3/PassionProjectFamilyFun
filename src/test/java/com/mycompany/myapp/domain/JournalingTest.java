package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JournalingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Journaling.class);
        Journaling journaling1 = new Journaling();
        journaling1.setId(1L);
        Journaling journaling2 = new Journaling();
        journaling2.setId(journaling1.getId());
        assertThat(journaling1).isEqualTo(journaling2);
        journaling2.setId(2L);
        assertThat(journaling1).isNotEqualTo(journaling2);
        journaling1.setId(null);
        assertThat(journaling1).isNotEqualTo(journaling2);
    }
}
