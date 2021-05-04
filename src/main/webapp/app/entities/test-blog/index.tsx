import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TestBlog from './test-blog';
import TestBlogDetail from './test-blog-detail';
import TestBlogUpdate from './test-blog-update';
import TestBlogDeleteDialog from './test-blog-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TestBlogUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TestBlogUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TestBlogDetail} />
      <ErrorBoundaryRoute path={match.url} component={TestBlog} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TestBlogDeleteDialog} />
  </>
);

export default Routes;
