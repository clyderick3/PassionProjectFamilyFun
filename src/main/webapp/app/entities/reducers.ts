import photo from 'app/entities/photo/photo.reducer';
import album from 'app/entities/album/album.reducer';
import journaling from 'app/entities/journaling/journaling.reducer';
import familyTree from 'app/entities/family-tree/family-tree.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  photo,
  album,
  journaling,
  familyTree,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
