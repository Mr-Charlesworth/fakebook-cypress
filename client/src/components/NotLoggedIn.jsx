import { Link, useNavigate } from "react-router-dom";
import Button from "../toolkit/Button";


const NotLoggedIn = () => {

  const navigate = useNavigate();

  return (
    <div className="mt-2">
        <Button data-cy="login-button" onClick={() => navigate('/login')}>
          Login
        </Button>
        <Button data-cy="signup-button" onClick={() => navigate('/signup')}>
          Sign Up
        </Button>
    </div>
  )
};

export default NotLoggedIn;