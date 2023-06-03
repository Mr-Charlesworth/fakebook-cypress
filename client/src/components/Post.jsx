import { Card, Col, Row } from "react-bootstrap";
import moment from "moment/moment";
import { useContext } from "react";
import AppContext from "../context";
import Button from "../toolkit/Button";
import { IconButton } from "@mui/material";
import { ThumbUp, ThumbUpAlt, ThumbUpOffAlt } from "@mui/icons-material";

const Post = ({ post }) => {

  const { user, client } = useContext(AppContext);

  const isUsersPost = user.username === post.username;

  const userLikesPost = post.likingUsers.includes(user.id);

  const deleteHandler = () => {
    client.deletePost(post.id)
      .then(() => {
        console.log("post deleted")
      })
      .catch((error) => {
        console.log(error)
      })
  }

  const likeHandler = () => {
    client.likePost(post.id)
      .then(() => {
        console.log("post liked/disliked")
      })
      .catch((error) => {
        console.log(error)
      })
  }

  return (
    <Card className="mb-3 ">
      <Card.Header>
        <big>{post.title}</big>
      </Card.Header>
      <Card.Body>
        <Card.Subtitle>{`By ${post.username}`}</Card.Subtitle>
        <Card.Text>{post.content}</Card.Text>
        {isUsersPost && (
          <Button small onClick={deleteHandler}>Delete</Button>
        )}
      </Card.Body>
      <Card.Footer>
        <Row>
          <Col>
            <small>{moment(post.timeCreated).format('MMMM, Do, YYYY, h:mm a')}</small>
          </Col>
          <Col className="text-end">
            <IconButton onClick={likeHandler}>
              {userLikesPost ? (
                <>
                  <ThumbUpAlt/>
                </>
              ) : (
                <ThumbUpOffAlt/>
              )}
              <small>{post.likingUsers.length}</small>
            </IconButton>
          </Col>
        </Row>

      </Card.Footer>
    </Card>
  );
};

export default Post;