import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Search from './search';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute path={match.url} component={Search} />
    </Switch>
  </>
);

export default Routes;
