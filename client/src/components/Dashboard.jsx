import { useContext } from "react";
import AppContext from "../context";

import Posts from "./Posts";
import Button from "../toolkit/Button";

const Dashboard = () => {
  const { user, logout } = useContext(AppContext);
  return (
    <>
      <p>{`Welcome ${user.username}!`}</p>
      {user.admin && (
        <p>{"Ah! You're an admin!"}</p>
      )}
      <Button data-cy="logout-button" onClick={logout}>Logout</Button>
      <div className="posts">
        <Posts/>
      </div>
    </>
  );
};

export default Dashboard;