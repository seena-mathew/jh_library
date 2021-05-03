import './home.scss';

import React, { useEffect } from 'react';
import { Link, Redirect } from 'react-router-dom';

import { connect } from 'react-redux';
import { Row, Col } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSearch } from '@fortawesome/free-solid-svg-icons/faSearch';

export type IHomeProp = StateProps;

export const Home = (props: IHomeProp) => {
  const { account } = props;
  const { from } = { from: { pathname: '/login' } };
  const search = () => {
    if (!props.isAuthenticated) {
      return <Redirect to="/login" />;
    }
  };
  return (
    <Row>
      <Col md="3" className="pad">
        <span className="hipster rounded" />
      </Col>
      <Col md="9" className="center">
        <h2>JHipster Library</h2>
        <p className="lead">A library for JHipster documentation</p>
        <div className="search">
          <input className="search-box" type="text" placeholder="What do you want to search?" />
          &nbsp;&nbsp;
          <button onClick={search} type="submit" className="search-button">
            Search &nbsp;
            <FontAwesomeIcon icon={faSearch} />
          </button>
        </div>
        <div className="ext-link">
          <p>If you have any question on JHipster:</p>
          <ul>
            <li>
              <a href="https://www.jhipster.tech/" target="_blank" rel="noopener noreferrer">
                JHipster homepage
              </a>
            </li>
            <li>
              <a href="http://stackoverflow.com/tags/jhipster/info" target="_blank" rel="noopener noreferrer">
                JHipster on Stack Overflow
              </a>
            </li>
            <li>
              <a href="https://github.com/jhipster/generator-jhipster/issues?state=open" target="_blank" rel="noopener noreferrer">
                JHipster bug tracker
              </a>
            </li>
            <li>
              <a href="https://gitter.im/jhipster/generator-jhipster" target="_blank" rel="noopener noreferrer">
                JHipster public chat room
              </a>
            </li>
            <li>
              <a href="https://twitter.com/jhipster" target="_blank" rel="noopener noreferrer">
                follow @jhipster on Twitter
              </a>
            </li>
          </ul>

          <p>
            If you like JHipster, do not forget to give us a star on{' '}
            <a href="https://github.com/jhipster/generator-jhipster" target="_blank" rel="noopener noreferrer">
              GitHub
            </a>
            !
          </p>
        </div>
      </Col>
    </Row>
  );
};

const mapStateToProps = storeState => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated,
});

type StateProps = ReturnType<typeof mapStateToProps>;

export default connect(mapStateToProps)(Home);
