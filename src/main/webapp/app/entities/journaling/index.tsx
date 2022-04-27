import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Journaling from './journaling';
import JournalingDetail from './journaling-detail';
import JournalingUpdate from './journaling-update';
import JournalingDeleteDialog from './journaling-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={JournalingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={JournalingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={JournalingDetail} />
      <ErrorBoundaryRoute path={match.url} component={Journaling} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={JournalingDeleteDialog} />
  </>
);

export default Routes;
