import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/photo">
        <Translate contentKey="global.menu.entities.photo" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/album">
        <Translate contentKey="global.menu.entities.album" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/journaling">
        <Translate contentKey="global.menu.entities.journaling" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/family-tree">
        <Translate contentKey="global.menu.entities.familyTree" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu as React.ComponentType<any>;
