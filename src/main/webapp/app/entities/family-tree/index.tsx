import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import FamilyTree from './family-tree';
import FamilyTreeDetail from './family-tree-detail';
import FamilyTreeUpdate from './family-tree-update';
import FamilyTreeDeleteDialog from './family-tree-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FamilyTreeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FamilyTreeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FamilyTreeDetail} />
      <ErrorBoundaryRoute path={match.url} component={FamilyTree} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={FamilyTreeDeleteDialog} />
  </>
);

export default Routes;
