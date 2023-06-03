import { Link } from "react-router-dom";
import Button from "../toolkit/Button";


const NotLoggedIn = () => {

  return (
    <div className="mt-2">
      <Link to={"/login"}>
        <Button>
          Login
        </Button>
      </Link>
      <Link to={"/signup"}>
        <Button>
          Sign Up
        </Button>
      </Link>
    </div>
  )
};

export default NotLoggedIn;