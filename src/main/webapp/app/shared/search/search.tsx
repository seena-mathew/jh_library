import './home.scss';

import React, { useEffect, useState } from 'react';
import { Link, Redirect, RouteComponentProps } from 'react-router-dom';

import { connect } from 'react-redux';
import { Row, Col, InputGroup, Button } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
import { faSearch } from '@fortawesome/free-solid-svg-icons/faSearch';
import { getEntities, getMasterSearch } from './search.reducer';
import { IHomeProp } from 'app/modules/home/home';

export interface ISearchProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Search = (props: ISearchProps) => {
  const [search, setSearch] = useState('');

  useEffect(() => {
    props.getEntities();
  }, []);

  const startSearching = () => {
    if (search) {
      props.getMasterSearch(search);
    }
  };

  const clear = () => {
    setSearch('');
    props.getEntities();
  };

  const handleSearch = event => setSearch(event.target.value);
  //
  // const handleSyncList = () => {
  //   props.getEntities();
  // };
  //
  // const { testBlogList, match, loading } = props;
  return (
    <div className="search">
      <AvForm onSubmit={startSearching}>
        <AvGroup>
          <InputGroup>
            <AvInput type="text" name="search" value={search} onChange={handleSearch} placeholder="Search" />
            <Button className="input-group-addon">
              <FontAwesomeIcon icon="search" />
            </Button>
            <Button type="reset" className="input-group-addon" onClick={clear}>
              <FontAwesomeIcon icon="trash" />
            </Button>
          </InputGroup>
        </AvGroup>
      </AvForm>
    </div>
  );
};

const mapStateToProps = storeState => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated,
});

const mapDispatchToProps = {
  getMasterSearch,
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Search);
