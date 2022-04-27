import React from 'react';
import { Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Photo from './photo';
import Album from './album';
import Journaling from './journaling';
import FamilyTree from './family-tree';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default ({ match }) => {
  return (
    <div>
      <Switch>
        {/* prettier-ignore */}
        <ErrorBoundaryRoute path={`${match.url}photo`} component={Photo} />
        <ErrorBoundaryRoute path={`${match.url}album`} component={Album} />
        <ErrorBoundaryRoute path={`${match.url}journaling`} component={Journaling} />
        <ErrorBoundaryRoute path={`${match.url}family-tree`} component={FamilyTree} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </Switch>
    </div>
  );
};
