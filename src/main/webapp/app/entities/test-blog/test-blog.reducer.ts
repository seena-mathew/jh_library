import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ITestBlog, defaultValue } from 'app/shared/model/test-blog.model';

export const ACTION_TYPES = {
  SEARCH_TESTBLOGS: 'testBlog/SEARCH_TESTBLOGS',
  FETCH_TESTBLOG_LIST: 'testBlog/FETCH_TESTBLOG_LIST',
  FETCH_TESTBLOG: 'testBlog/FETCH_TESTBLOG',
  CREATE_TESTBLOG: 'testBlog/CREATE_TESTBLOG',
  UPDATE_TESTBLOG: 'testBlog/UPDATE_TESTBLOG',
  PARTIAL_UPDATE_TESTBLOG: 'testBlog/PARTIAL_UPDATE_TESTBLOG',
  DELETE_TESTBLOG: 'testBlog/DELETE_TESTBLOG',
  RESET: 'testBlog/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ITestBlog>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type TestBlogState = Readonly<typeof initialState>;

// Reducer

export default (state: TestBlogState = initialState, action): TestBlogState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_TESTBLOGS):
    case REQUEST(ACTION_TYPES.FETCH_TESTBLOG_LIST):
    case REQUEST(ACTION_TYPES.FETCH_TESTBLOG):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_TESTBLOG):
    case REQUEST(ACTION_TYPES.UPDATE_TESTBLOG):
    case REQUEST(ACTION_TYPES.DELETE_TESTBLOG):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_TESTBLOG):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_TESTBLOGS):
    case FAILURE(ACTION_TYPES.FETCH_TESTBLOG_LIST):
    case FAILURE(ACTION_TYPES.FETCH_TESTBLOG):
    case FAILURE(ACTION_TYPES.CREATE_TESTBLOG):
    case FAILURE(ACTION_TYPES.UPDATE_TESTBLOG):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_TESTBLOG):
    case FAILURE(ACTION_TYPES.DELETE_TESTBLOG):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_TESTBLOGS):
    case SUCCESS(ACTION_TYPES.FETCH_TESTBLOG_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_TESTBLOG):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_TESTBLOG):
    case SUCCESS(ACTION_TYPES.UPDATE_TESTBLOG):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_TESTBLOG):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_TESTBLOG):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/test-blogs';
const apiSearchUrl = 'api/_search/test-blogs';

// Actions

export const getSearchEntities: ICrudSearchAction<ITestBlog> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_TESTBLOGS,
  payload: axios.get<ITestBlog>(`${apiSearchUrl}?query=${query}`),
});

export const getEntities: ICrudGetAllAction<ITestBlog> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_TESTBLOG_LIST,
  payload: axios.get<ITestBlog>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<ITestBlog> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_TESTBLOG,
    payload: axios.get<ITestBlog>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ITestBlog> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_TESTBLOG,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ITestBlog> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_TESTBLOG,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<ITestBlog> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_TESTBLOG,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ITestBlog> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_TESTBLOG,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
