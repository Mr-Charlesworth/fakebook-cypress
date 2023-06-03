import { createContext } from 'react';

const AppContext = createContext({
  client: {},
  token: "",
  setToken: () => {},
  isLoggedIn: false,
  setIsLoggedIn: () => {},
  user: {
    admin: false
  },
  setUser: () => {},
  posts: [],
  setPosts: () => {},
  logout: () => {},
});

export default AppContext;
