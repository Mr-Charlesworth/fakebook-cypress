import { useContext, useState } from "react";

import { Formik, Form } from "formik";
import * as Yup from 'yup';

import AppContext from "../context";
import { Alert, Button } from "react-bootstrap";
import FormTextField from "./FormTextField";
import { useNavigate } from "react-router-dom";

const SignupForm = () => {
  const { client, setToken, setUser, setIsLoggedIn } = useContext(AppContext)
  const [error, setError] = useState(undefined);
  const navigate = useNavigate();

  const handleSubmit = (values, setSubmitting) => {
    setSubmitting(true);
    setError(undefined)
    client.signup(values)
      .then(({ data }) => {
        setToken(data.token);
        setUser(data.user);
        setIsLoggedIn(true);
        setSubmitting(false);
        navigate("/");
      })
      .catch((error) => {
        console.log(error)
        const reason = error.response.data.message;
        setError(reason);
        setSubmitting(false);
      })
  }

  return (
    <Formik
      initialValues={{
        username: "",
        password: "",
      }}
      validationSchema={Yup.object().shape({
        username: Yup.string()
          .required("Username is required"),
        password: Yup.string()
          .required("Password is required")
          .min(8, "Must be at least 8 characters"),
      })}
      onSubmit={(values, { setSubmitting }) => handleSubmit(values, setSubmitting)}>
      {({ values, isValid, handleSubmit, isSubmitting, errors, }) => (
        <Form>
          {error && (
            <Alert data-cy="signup-error" variant="danger">{error}</Alert>
          )}
          <h3>Sign up for a new account</h3>
          <FormTextField
            data-cy="input-username"
            label="Username"
            type="text"
            name="username"
            isInvalid={!!errors.name}
            feedback={errors.name}
          />
          <FormTextField
            data-cy="input-password"
            label="Password"
            type="password"
            name="password"
            isInvalid={!!errors.password}
            feedback={errors.password}
          />
          <Button
            className="smallTopMargin"
            data-cy="submit-button"
            disabled={!isValid || isSubmitting}
            variant="success"
            as="input"
            size="lg"
            type="submit"
            value="Sign Up"
          />
        </Form>
      )}
    </Formik>
  );
};

export default SignupForm;