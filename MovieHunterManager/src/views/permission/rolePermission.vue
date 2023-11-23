<template>
  <div class="app-container">
    <el-button class="filter-item" style="margin-left: 10px; margin-bottom: 20px" type="primary" icon="el-icon-edit" @click="handleCreate">
      Add
    </el-button>
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="加载中……"
      border
      style="width: 100%;"
      fit
      highlight-current-row
      :ke="itemKey"
    >

      <el-table-column align="center" label="序号" width="100px">
        <template slot-scope="scope">
          {{ scope.$index }}
        </template>
      </el-table-column>

      <el-table-column label="角色ID" width="100px" align="center">
        <template slot-scope="scope">
          {{ scope.row.roleId }}
        </template>
      </el-table-column>

      <el-table-column label="角色名" min-width="150px" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.roleName }}</span>
        </template>
      </el-table-column>

      <el-table-column label="状态" class-name="status-col" width="200px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="row.delFlag | numMap | statusFilter">
            {{ row.delFlag | numMap }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" width="300px" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.delFlag != 1"
            size="mini"
            type="primary"
            @click="handleUpdate(scope.$index, scope.row)">编辑</el-button>
          <el-button
            v-if="scope.row.delFlag != 1"
            size="mini"
            type="warning"
            @click="handleAuthenticate(scope.$index, scope.row)">授权</el-button>
          <el-button
            v-if="scope.row.delFlag != 1"
            size="mini"
            type="danger"
            @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          <el-button
            v-if="scope.row.delFlag != 0"
            size="mini"
            type="success"
            @click="handleRestore(scope.$index, scope.row)">还原</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="70px" style="width: 400px; margin-left:50px;">

        <el-form-item label="角色名" prop="roleName">
          <el-input v-model="temp.roleName" placeholder="请输入,编辑时不输入即不做修改" maxlength="30" style="width:350px;"/>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确认
        </el-button>
      </div>
    </el-dialog>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="authenticateDialogVisible" width="300px">
      <el-form>
        <el-form-item  style="padding-left: 30%;">
          <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
          <br>
          <el-checkbox-group style="display: grid" v-for="item in menuList" :key="item.menuId" v-model="checkMenu" >
            <el-checkbox :label="item.menuId">{{ item.permission }}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="authenticateDialogVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="authenticate">
          确认
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>

import {
  deleteRoleById, deleteRoleMenu, deleteUserRole, findAllPermissionOfOneRole, findPermissionMenu,
  insertRole, insertRoleMenu, insertUserRole, restoreRoleById,
  showAllRoles,
  updateRoleById
} from "@/api/permission";

export default {
  filters: {
    numMap(status) {
      const map = [
        '发布',
        '删除'
      ]
      return map[status]
    },
    statusFilter(status) {
      const statusMap = {
        发布: 'success',
        删除: 'danger'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      itemKey: 0,
      dialogFormVisible: false,
      authenticateDialogVisible: false,
      dialogStatus: null,
      checkMenu: [],
      permissionOptions: [],
      menuList: [{
        menuId: null,
        menuName: null,
        permission: null,
        delFlag: null
      }],
      roleMenu: [{
        menuId: null,
        menuName: null,
        permission: null,
        delFlag: null
      }],
      tempRow: null, // 当前行
      rules: {},
      temp: {
        "roleId": null,
        "roleName": null,
        "delFlag": null,
      },
      list: [
        {
          "roleId": 0,
          "roleName": null,
          "delFlag": 0,
        }],
      listLoading: true,
      textMap: {
        update: '编辑',
        create: '创建',
        authenticate: '授权',
      },
      isIndeterminate: true,
      checkAll: false,
    }
  },
  created() {
    this.fetchData()
    this.fetchPermissionMenu()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      showAllRoles().then(response => {
        this.list = response.data
        this.listLoading = false
      })
    },
    fetchPermissionMenu() {
      findPermissionMenu().then(response => {
        this.menuList = response.data
        for (const elem of this.menuList) {
          this.permissionOptions.push(elem.menuId)
        }
      })
    },
    resetTemp() {
      this.temp = {
        "roleId": null,
        "roleName": null,
        "delFlag": null,
      }
    },
    formMenuRolePairs( roleId, menu) {
      let menuRolePair = {}, array = []
      for (const elem of menu) {
        menuRolePair['roleId'] = roleId
        menuRolePair['menuId'] = elem.menuId
        array.push(menuRolePair)
        menuRolePair = {}
      }
      return array
    },
    handleCheckAllChange(val) {
      this.checkMenu = val ? this.permissionOptions : [];
      this.isIndeterminate = false;
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          insertRole(this.temp).then((response) => {
            if (response.data["影响行数"] !== 0) {
              this.dialogFormVisible = false
              this.$message({
                message: '添加成功',
                type: 'success'
              });
            } else {
              this.$message.error('添加失败');
            }
            this.$nextTick(this.fetchData())
          })
        }
      })
    },
    handleUpdate(index, row) {
      this.temp = Object.assign({}, this.list[index])
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          updateRoleById(this.temp).then((response) => {
            if (response.data["影响行数"] !== 0) {
              this.dialogFormVisible = false
              this.$message({
                message: '更新成功',
                type: 'success'
              });
            } else {
              this.$message.error('更新失败');
            }
            this.$nextTick(this.fetchData())
          })
        }
      })
    },
    handleRestore(index, row) {
      restoreRoleById(row.roleId).then(response => {
        if (response.data["影响行数"] !== 0) {
          this.$message({
            message: '还原成功',
            type: 'success'
          });
        }
        //失败
        else {
          this.$message.error('还原失败');
        }
        this.$nextTick(this.fetchData())
      })
    },
    handleDelete(index, row) {
      this.$confirm('确定要删除该角色吗', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteRoleById(row.roleId).then(response => {
          if (response.data["影响行数"] !== 0) {
            this.$message({
              type: 'success',
              message: '删除成功!'
            });
          } else {
            this.$message.error('删除失败')
          }
          this.$nextTick(this.fetchData())
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消'
        });
      });
    },
    handleAuthenticate(index, row) {
      findAllPermissionOfOneRole(row.roleId).then(response => {
        this.roleMenu = response.data
        this.checkMenu = []
        for (const menu of this.roleMenu) {
          this.checkMenu.push(menu['menuId'])
        }
      })
      this.tempRow = row
      this.dialogStatus = 'authenticate'
      this.authenticateDialogVisible = true
    },
    authenticate() {
      let array = []
      let delElem = [], insertElem = []
      //从当前用户的角色中去除勾选的角色, 剩下的是待删除的角色
      if (this.roleMenu) {
        delElem = this.roleMenu.filter(menu => {
          return !this.checkMenu.includes(menu.menuId)
        })
        //所有勾选的且不是用户已有的角色即为待插入角色
        insertElem = this.menuList.filter(menu => {
          return this.checkMenu.includes(menu.menuId)
        }).filter(menu => {
          return !this.roleMenu.some(item => item.menuId === menu.menuId)
        })
        if (delElem) {
          const arr = this.formMenuRolePairs(this.tempRow.roleId, delElem)
          deleteRoleMenu(arr).then(response => {
              if (response.data["影响行数"] !== 0) {
                this.$message({
                  message: '删除成功',
                  type: 'success'
                });
              }
            }
          )
        }
      }
      //若当前用户没有角色,所有勾选的角色都需要插入
      else {
        insertElem = this.menuList.filter(menu => {
          return this.checkMenu.includes(menu.menuId)
        })
      }
      array = this.formMenuRolePairs(this.tempRow.roleId, insertElem)

      insertRoleMenu(array).then(response => {
        if(response.data["影响行数"] !== 0) {
          this.$message({
            message: '添加成功',
            type: 'success'
          });
        }
      });
      this.authenticateDialogVisible = false
    },
  }
}
</script>
