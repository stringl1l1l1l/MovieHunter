import http from '@/utils/request'

// user

export async function showAllUsers() {
  return http.get("/user/showUsersByPages")
}

export async function showUsersByPages(pageNum) {
  return http.getRestApi("/user/showUsersByPages", pageNum)
}

export async function showUserById(id) {
  return http.getRestApi("/user/showUserById", id)
}

export async function deleteUserById(id) {
  return http.delete("/user/deleteUserById", id)
}

export async function restoreUserById(id) {
  return http.getRestApi("/user/restoreUserById", id)
}

export async function insertUser(user) {
  return http.post("/user/insertUser", user)
}

export async function updateUserById(user) {
  return http.put("/user/updateUserById", user)
}

export async function setUserById(user) {
  return http.put("/user/setUserById", user)
}

export async function findUserByEmail(email) {
  return http.getRestApi("/user/findUserByEmail", email)
}

// movie
export async function findAllMovies(pageNum) {
  return http.get("/movie/findAllMovies", { pageNum: pageNum })
}

export async function deleteMovieById(id) {
  return http.delete("/movie/deleteMovieById", id)
}

export async function updateMovieById(movie) {
  return http.post("/movie/updateMovieById", movie)
}

export async function setMovieById(movie) {
  return http.post("/movie/setMovieById", movie)
}

export async function insertMovie(movie) {
  return http.put("/movie/insertMovie", movie)
}

export async function findMoviesByName(name, pageNum) {
  return http.get("/movie/findMoviesByName", { name: name, pageNum: pageNum })
}

// comment
export async function deleteAnyCommentById(commentId) {
  return http.delete("/comment/deleteAnyCommentById", commentId)
}

export async function findAllComments(pageNum) {
  return http.get("/comment/findAllComments", { pageNum: pageNum })
}

export async function findCommentById(commentId) {
  return http.getRestApi("/comment/findCommentById", commentId)
}

// likes
export async function findAllLikes(pageNum) {
  return http.get("/likes/findAllLikes", { pageNum: pageNum })
}

export async function findLikesById(obj) {
  return http.get("/likes/findLikesById", obj)
}
