import { useContext, useState } from "react";

import { Formik, Form } from "formik";
import * as Yup from 'yup';

import AppContext from "../context";
import { Alert, Button } from "react-bootstrap";
import FormTextField from "./FormTextField";
import { useNavigate } from "react-router-dom";
import { isEmpty } from "lodash";

const PostForm = () => {
  const { client, setToken, setUser, setIsLoggedIn } = useContext(AppContext)
  const [error, setError] = useState(undefined);
  const navigate = useNavigate();

  const handleSubmit = (values, setSubmitting) => {
    setSubmitting(true);
    setError(undefined)
    client.createPost(values)
      .then(() => {
        setSubmitting(false);
      })
      .catch((error) => {
        const reason = error?.response?.data?.message;
        if (!isEmpty(reason)) {
          setError(reason);
        }
        setSubmitting(false);
      })
  }

  return (
    <Formik
      enableReinitialize
      initialValues={{
        title: "",
        content: "",
      }}
      validationSchema={Yup.object().shape({
        title: Yup.string()
          .required("title is required"),
        content: Yup.string()
          .required("content is required"),
      })}
      onSubmit={(values, { setSubmitting }) => handleSubmit(values, setSubmitting)}>
      {({ values, isValid, handleSubmit, isSubmitting, errors, }) => (
        <Form>
          {error && (
            <Alert variant="danger">{error}</Alert>
          )}
          <h3>Create New Post</h3>
          <FormTextField
            label="Post Title"
            type="text"
            name="title"
            isInvalid={!!errors.title}
            feedback={errors.title}
          />
          <FormTextField
            label="Post Message"
            as="textarea"
            name="content"
            isInvalid={!!errors.content}
            feedback={errors.content}
          />
          <Button
            className="smallTopMargin"
            disabled={!isValid || isSubmitting}
            variant="success"
            as="input"
            size="lg"
            type="submit"
            value="Create Post"
          />
        </Form>
      )}
    </Formik>
  );
};

export default PostForm;