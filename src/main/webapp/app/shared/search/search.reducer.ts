import axios from 'axios';
import { ICrudSearchAction, ICrudGetAllAction } from 'react-jhipster';

import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { ISearch } from 'app/shared/search/search.model';

export const ACTION_TYPES = {
  SEARCH_MASTER: 'testBlog/SEARCH_MASTER',
};

const initialState = {
  loading: false,
  errorMessage: null,
  updating: false,
  updateSuccess: false,
};

export type HomeState = Readonly<typeof initialState>;

// Reducer

export default (state: HomeState = initialState, action): HomeState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_MASTER):
    case FAILURE(ACTION_TYPES.SEARCH_MASTER):
    case SUCCESS(ACTION_TYPES.SEARCH_MASTER):
    default:
      return state;
  }
};

const apiUrl = 'api/test-blogs';
const apiSearchUrl = 'api/_search/test-blogs';

// Actions

export const getMasterSearch: ICrudSearchAction<ISearch> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_MASTER,
  payload: axios.get<ISearch>(`${apiSearchUrl}?query=${query}`),
});

export const getEntities: ICrudGetAllAction<ISearch> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_TESTBLOG_LIST,
  payload: axios.get<ISearch>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
