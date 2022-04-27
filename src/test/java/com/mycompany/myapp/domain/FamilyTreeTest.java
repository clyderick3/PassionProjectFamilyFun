package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FamilyTreeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FamilyTree.class);
        FamilyTree familyTree1 = new FamilyTree();
        familyTree1.setId(1L);
        FamilyTree familyTree2 = new FamilyTree();
        familyTree2.setId(familyTree1.getId());
        assertThat(familyTree1).isEqualTo(familyTree2);
        familyTree2.setId(2L);
        assertThat(familyTree1).isNotEqualTo(familyTree2);
        familyTree1.setId(null);
        assertThat(familyTree1).isNotEqualTo(familyTree2);
    }
}
