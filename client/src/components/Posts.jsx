import { useContext, useEffect } from "react";

import { IconButton } from "@mui/material";
import { Refresh } from "@mui/icons-material";

import AppContext from "../context";
import PostForm from "./PostForm";
import Post from "./Post";

const Posts = () => {
  const { posts, client } = useContext(AppContext);

  useEffect(() => {
    client.getPosts();
  }, [])

  return (
    <>
      <div className="mt-2 mb-3">
        <PostForm/>
      </div>
      <div className="text-end">
        <IconButton onClick={client.getPosts}>
          <Refresh/>
        </IconButton>
      </div>
      {posts.map((post) => (
        <Post key={post.id} post={post}/>
      ))}
    </>
  );
};

export default Posts