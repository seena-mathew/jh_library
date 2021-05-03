import './sidebar.scss';

import React, { useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHome, faBriefcase, faPaperPlane, faQuestion, faImage, faCopy } from '@fortawesome/free-solid-svg-icons';
import { NavItem, NavLink, Nav, Navbar } from 'reactstrap';
import { Brand, Home } from '../header/header-components';
import { Link } from 'react-router-dom';

import SubMenu from './submenu';
import { EntitiesMenu } from 'app/shared/layout/menus';

export interface ISidebarProps {
  isAuthenticated: boolean;
  isAdmin: boolean;
  isInProduction: boolean;
  isOpenAPIEnabled: boolean;
}
const Sidebar = (props: ISidebarProps) => {
  /* jhipster-needle-add-element-to-menu - JHipster will add new menu items here */
  const submenus = [
    [
      {
        title: 'Blog 1',
        target: 'Page-1',
      },
      {
        title: 'Blog 2',
        target: 'Page-2',
      },
    ],
  ];
  return (
    <div className="bg-primary side-bar">
      <Nav vertical>
        <Home />
        {props.isAuthenticated && <SubMenu title="Blogs" icon={faCopy} items={submenus[0]} />}
        {props.isAuthenticated && <SubMenu title="Categories" icon={faCopy} items={submenus[0]} />}
        {/*<NavItem>*/}
        {/*  <NavLink tag={Link} to={"/about"}>*/}
        {/*    <FontAwesomeIcon icon={faBriefcase} className="mr-2" />*/}
        {/*    JHipster Official site*/}
        {/*  </NavLink>*/}
        {/*</NavItem>*/}
      </Nav>
    </div>
  );
};

export default Sidebar;
