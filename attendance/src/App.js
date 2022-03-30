//import logo from './logo.svg';
import "antd/dist/antd.css";
import React from 'react';
// import Navbar from './components/Navbar';
// import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import LogInForm from './components/LogInForm';
import { Layout, Menu, Breadcrumb } from 'antd';

const { Header, Footer, Sider, Content } = Layout;


function App() {
  
  return (

    
    <Layout>
    <Header style={{ width: '100%' }}>
      <Menu theme="dark" mode="horizontal" defaultSelectedKeys={['2']}>
        <Menu.Item key="1">nav 1</Menu.Item>
        <Menu.Item key="2">nav 2</Menu.Item>
        <Menu.Item key="3">nav 3</Menu.Item>
      </Menu>
    </Header>
      <Content>
        <LogInForm />
      </Content>
      <Footer>Footer</Footer>
    </Layout>


    

  );
}



export default App;