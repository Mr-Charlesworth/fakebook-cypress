import { useState } from "react";
import { useNavigate } from "react-router-dom";

import axios from "axios";
import { isEmpty } from "lodash";

import App from "./App";
import AppContext from "./context";

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

  const apiCall = ({ method, url, data, ...rest }) => axios({
    method,
    url: `${baseUrl}${url}`,
    data,
    headers: {
      authorization: !isEmpty(token) ? `Bearer ${token}` : null,
    },
    ...rest,
  });

  const login = ({ username, password }) => apiCall({
    method: 'post',
    url: "/auth/login",
    auth: { username, password },
  });

  const signup = (signupFormValues) => apiCall({
    method: 'post',
    url: '/auth/signup',
    data: signupFormValues,
  });

  const getPosts = () => apiCall({
    method: 'get',
    url: '/posts',
  }).then(({ data }) => new Promise((resolve) => {
    setPosts(data);
    resolve();
  }));

  const createPost = (postFormValues) => apiCall({
    method: 'post',
    url: '/posts/create',
    data: postFormValues,
  }).then(() => getPosts());


  const deletePost = (postId) => apiCall({
    method: 'delete',
    url: `/posts/${postId}`
  }).then(() => getPosts());


  const likePost = (postId) => apiCall({
    method: 'post',
    url: `/posts/${postId}/like`,
  }).then(() => getPosts());


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
