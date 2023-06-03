import { useState } from "react";

import axios from "axios";

import App from "./App";
import AppContext from "./context";
import {isEmpty} from "lodash";
import { useNavigate } from "react-router-dom";

const AxiosClient = () => {
  const baseUrl =
      !process.env.NODE_ENV || process.env.NODE_ENV === "development"
          ? "http://localhost:8080/api"
          : "";

  const [token, setToken] = useState(undefined);
  const [user, setUser] = useState(undefined);
  const [posts, setPosts] = useState([]);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const navigate = useNavigate();

  const logout = () => {
    setToken(undefined);
    setIsLoggedIn(false);
    navigate("/")
  }

  const apiCall = (method, url, data, rest = {}) => {
    return axios({
      method,
      url: `${baseUrl}${url}`,
      data,
      headers: {
        authorization: !isEmpty(token) ? `Bearer ${token}` : null,
      },
      ...rest,
    }).catch((error) => {
      throw error;
    });
  };

  const login = ({ username, password }) => {
    return apiCall("post", "/auth/login", {}, { auth: { username, password } });
  };

  const signup = (signupFormValues) => {
    return apiCall("post", "/auth/signup", signupFormValues)
  };

  const getPosts = () => {
    return apiCall("get", "/posts")
      .then(({ data }) => new Promise((resolve) => {
        setPosts(data);
        resolve();
      }));
  }

  const createPost = (postFormValues) => {
    return apiCall("post", "/posts/create", postFormValues)
      .then(() => getPosts());
  }

  const deletePost = (postId) => {
    return apiCall("delete", `/posts/delete/${postId}`)
      .then(() => getPosts());
  }

  const likePost = (postId) => {
    return apiCall("post", `/posts/like/${postId}`)
      .then(() => getPosts());
  }

  const client = {
    apiCall,
    login,
    signup,
    createPost,
    getPosts,
    deletePost,
    likePost,
  };

  return (
      <AppContext.Provider
        value={{
          client,
          token,
          setToken,
          isLoggedIn,
          setIsLoggedIn,
          user,
          setUser,
          posts,
          setPosts,
          logout,
        }}
      >
        <App/>
      </AppContext.Provider>
  );
};

export default AxiosClient;
