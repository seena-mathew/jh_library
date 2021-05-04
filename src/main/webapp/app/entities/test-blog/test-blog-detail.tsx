import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './test-blog.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITestBlogDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const TestBlogDetail = (props: ITestBlogDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { testBlogEntity } = props;
  return (
    <Row>
      <Col md="8">
        <div dangerouslySetInnerHTML={{ __html: testBlogEntity.content }} />
        {/*<h2 data-cy="testBlogDetailsHeading">TestBlog</h2>*/}
        {/*<dl className="jh-entity-details">*/}
        {/*  <dt>*/}
        {/*    <span id="id">ID</span>*/}
        {/*  </dt>*/}
        {/*  <dd>{testBlogEntity.id}</dd>*/}
        {/*  <dt>*/}
        {/*    <span id="title">Title</span>*/}
        {/*  </dt>*/}
        {/*  <dd>{testBlogEntity.title}</dd>*/}
        {/*  <dt>*/}
        {/*    <span id="content">Content</span>*/}
        {/*  </dt>*/}
        {/*  <dd>{testBlogEntity.content}</dd>*/}
        {/*  <dt>*/}
        {/*    <span id="createdOn">Created On</span>*/}
        {/*  </dt>*/}
        {/*  <dd>{testBlogEntity.createdOn ? <TextFormat value={testBlogEntity.createdOn} type="date" format={APP_DATE_FORMAT} /> : null}</dd>*/}
        {/*  <dt>*/}
        {/*    <span id="updatedOn">Updated On</span>*/}
        {/*  </dt>*/}
        {/*  <dd>{testBlogEntity.updatedOn ? <TextFormat value={testBlogEntity.updatedOn} type="date" format={APP_DATE_FORMAT} /> : null}</dd>*/}
        {/*  <dt>*/}
        {/*    <span id="username">Username</span>*/}
        {/*  </dt>*/}
        {/*  <dd>{testBlogEntity.username}</dd>*/}
        {/*</dl>*/}
        {/*<Button tag={Link} to="/test-blog" replace color="info" data-cy="entityDetailsBackButton">*/}
        {/*  <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>*/}
        {/*</Button>*/}
        {/*&nbsp;*/}
        {/*<Button tag={Link} to={`/test-blog/${testBlogEntity.id}/edit`} replace color="primary">*/}
        {/*  <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>*/}
        {/*</Button>*/}
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ testBlog }: IRootState) => ({
  testBlogEntity: testBlog.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(TestBlogDetail);
