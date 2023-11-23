import http from "@/utils/request";

export async function showAllRoles() {
  return http.get("/permission/showAllRoles")
}

export async function findAllRoles() {
  return http.get("/permission/findAllRoles")
}

export async function showPermissionMenu() {
  return http.get("/permission/showPermissionMenu")
}

export async function findPermissionMenu() {
  return http.get("/permission/findPermissionMenu")
}

export async function findAllRolesOfOneUser(id) {
  return http.getRestApi("/permission/findAllRolesOfOneUser", id)
}

export async function insertUserRole(userRoles) {
  return http.post("/permission/insertUserRole", userRoles)
}

export async function deleteUserRole(userRoles) {
  return http.post("/permission/deleteUserRole", userRoles)
}

export async function deleteRoleById(id) {
  return http.delete("/permission/deleteRoleById", id)
}

export async function updateRoleById(role) {
  return http.put("/permission/updateRoleById", role)
}

export async function insertRole(role) {
  return http.post("/permission/insertRole", role)
}

export async function restoreRoleById(id) {
  return http.getRestApi("/permission/restoreRoleById", id)
}

export async function findAllPermissionOfOneRole(id) {
  return http.getRestApi("/permission/findAllPermissionOfOneRole", id)
}

export async function insertRoleMenu(roleMenu) {
  return http.post("/permission/insertRoleMenu", roleMenu)
}

export async function deleteRoleMenu(roleMenu) {
  return http.post("/permission/deleteRoleMenu", roleMenu)
}

export async function deleteMenuById(id) {
  return http.delete("/permission/deleteMenuById", id)
}

export async function updateMenuById(role) {
  return http.put("/permission/updateMenuById", role)
}

export async function insertMenu(menu) {
  return http.post("/permission/insertMenu", menu)
}

export async function restoreMenuById(id) {
  return http.getRestApi("/permission/restoreMenuById", id)
}
