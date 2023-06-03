import { useContext } from "react";

import { Button, Container } from "react-bootstrap";
import { isEmpty } from "lodash";

import "bootstrap/dist/css/bootstrap.min.css";
import './App.css';
import AppContext from "./context";
import LoginForm from "./components/LoginForm";
import Dashboard from "./components/Dashboard";
import { Route, Routes } from "react-router-dom";
import NotLoggedIn from "./components/NotLoggedIn";
import SignupForm from "./components/SignupForm";

const App = () => {

  const { isLoggedIn, user, client, logout } = useContext(AppContext);

  return (
      <Container>
        <h1>Welcome to Fakebook</h1>
        {(isLoggedIn && !isEmpty(user)) ? (
            <Dashboard/>
        ) : (
            <Routes>
              <Route element={<NotLoggedIn/>} path="/" />
              <Route element={<LoginForm/>} path="/login" />
              <Route element={<SignupForm/>} path="/signup" />
            </Routes>
        )}
      </Container>
  );
}


export default App;
