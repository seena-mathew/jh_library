import React, { useState, useEffect, useRef } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './test-blog.reducer';
import { ITestBlog } from 'app/shared/model/test-blog.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { Editor } from '@tinymce/tinymce-react';

export interface ITestBlogUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const TestBlogUpdate = (props: ITestBlogUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { testBlogEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/test-blog');
  };

  const editorRef = useRef(null);

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.createdOn = convertDateTimeToServer(values.createdOn);
    values.updatedOn = convertDateTimeToServer(values.updatedOn);
    values.content = editorRef.current.getContent();
    if (errors.length === 0) {
      const entity = {
        ...testBlogEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhLibraryApp.testBlog.home.createOrEditLabel" data-cy="TestBlogCreateUpdateHeading">
            Create or edit a TestBlog
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : testBlogEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="test-blog-id">ID</Label>
                  <AvInput id="test-blog-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="titleLabel" for="test-blog-title">
                  Title
                </Label>
                <AvField
                  id="test-blog-title"
                  data-cy="title"
                  type="text"
                  name="title"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="contentLabel" for="test-blog-content">
                  Content
                </Label>
                <Editor
                  onInit={(evt, editor) => (editorRef.current = editor)}
                  // editorRef.current.setContent(testBlogEntity.content)
                  init={{
                    height: 500,
                    menubar: true,
                    image_uploadtab: true,
                    plugins: [
                      'advlist autolink lists link image charmap print preview anchor',
                      'searchreplace visualblocks code fullscreen',
                      'insertdatetime media table paste code help wordcount',
                    ],
                    toolbar:
                      'undo redo | formatselect | ' +
                      'bold italic backcolor | alignleft aligncenter ' +
                      'alignright alignjustify | bullist numlist outdent indent | ' +
                      'removeformat | help',
                    content_style: 'body { font-family:Helvetica,Arial,sans-serif; font-size:14px }',
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="createdOnLabel" for="test-blog-createdOn">
                  Created On
                </Label>
                <AvInput
                  id="test-blog-createdOn"
                  data-cy="createdOn"
                  type="datetime-local"
                  className="form-control"
                  name="createdOn"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.testBlogEntity.createdOn)}
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedOnLabel" for="test-blog-updatedOn">
                  Updated On
                </Label>
                <AvInput
                  id="test-blog-updatedOn"
                  data-cy="updatedOn"
                  type="datetime-local"
                  className="form-control"
                  name="updatedOn"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.testBlogEntity.updatedOn)}
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="usernameLabel" for="test-blog-username">
                  Username
                </Label>
                <AvField
                  id="test-blog-username"
                  data-cy="username"
                  type="text"
                  name="username"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/test-blog" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  testBlogEntity: storeState.testBlog.entity,
  loading: storeState.testBlog.loading,
  updating: storeState.testBlog.updating,
  updateSuccess: storeState.testBlog.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(TestBlogUpdate);
