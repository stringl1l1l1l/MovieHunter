<template>
  <div class="app-container">
    <div class="filter-container">
      <el-row>
        <el-col :span="15">
          <el-button class="filter-item" style="margin-left: 10px; margin-bottom: 20px" type="primary" icon="el-icon-edit"
            @click="handleCreate">
            Add
          </el-button>
        </el-col>

        <el-col :span="6" style="float:right">
          <el-input placeholder="请输入搜索内容" prefix-icon="el-icon-search" v-model="this.search"/>
        </el-col>
      </el-row>
    </div>

    <el-row>
      <el-table v-loading="listLoading" :data="list" element-loading-text="加载中……" border style="width: 100%;" fit
        highlight-current-row :ke="itemKey">

        <el-table-column align="center" label="序号" width="100px">
          <template slot-scope="scope">
            {{ scope.$index }}
          </template>
        </el-table-column>

        <el-table-column align="center" label="角色ID" width="100px">
          <template slot-scope="scope">
            {{ scope.row.menuId }}
          </template>
        </el-table-column>

        <el-table-column label="权限名" mid-width="150px" align="center">
          <template slot-scope="scope">
            {{ scope.row.menuName }}
          </template>
        </el-table-column>

        <el-table-column label="指令" min-width="200px" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.permission }}</span>
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
            <el-button v-if="scope.row.delFlag != 1" size="mini" type="primary"
              @click="handleUpdate(scope.$index, scope.row)">编辑</el-button>
            <el-button v-if="scope.row.delFlag != 1" size="mini" type="danger"
              @click="handleDelete(scope.$index, scope.row)">删除</el-button>
            <el-button v-if="scope.row.delFlag != 0" size="mini" type="success"
              @click="handleRestore(scope.$index, scope.row)">还原</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
        <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="70px"
          style="width: 400px; margin-left:50px;">

          <el-form-item label="权限名" prop="menuName">
            <el-input v-model="temp.menuName" placeholder="请输入,编辑时不输入即不做修改" maxlength="30" style="width:350px;" />
          </el-form-item>

          <el-form-item label="指令" prop="permission">
            <el-input v-model="temp.permission" placeholder="请输入,编辑时不输入即不做修改" style="width:350px;" />
          </el-form-item>

        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">
            取消
          </el-button>
          <el-button type="primary" @click="dialogStatus === 'create' ? createData() : updateData()">
            确认
          </el-button>
        </div>
      </el-dialog>
    </el-row>
  </div>
</template>

<script>
import {
  deleteMenuById,
  insertMenu,
  restoreMenuById,
  restoreRoleById,
  showPermissionMenu, updateMenuById,
  updateRoleById
} from "@/api/permission";
import { deleteUserById, insertUser, restoreUserById } from "@/api/table";

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
        '发布': 'success',
        '删除': 'danger'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      itemKey: 0,
      list: [{
        menuId: null,
        menuName: null,
        permission: null,
        delFlag: null
      }],
      temp: {
        menuId: null,
        menuName: null,
        permission: null,
        delFlag: null
      },
      listLoading: true,
      dialogStatus: "",
      dialogFormVisible: false,
      rules: {
      },
      textMap: {
        update: '编辑',
        create: '创建'
      },
      search: "",
      
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      showPermissionMenu().then(response => {
        this.list = response.data
        this.listLoading = false
      })
    },
    resetTemp() {
      this.temp = {
        menuId: null,
        menuName: null,
        permission: null,
        delFlag: null
      }
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
          updateMenuById(this.temp).then((response) => {
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
    handleDelete(index, row) {
      this.$confirm('确定要删除该权限吗', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteMenuById(row.menuId).then(response => {
          if (response.data["影响行数"] !== 0) {
            this.$message({
              type: 'success',
              message: '删除成功!'
            });
          }
          else {
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
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          insertMenu(this.temp).then((response) => {
            if (response.data["影响行数"] !== 0) {
              this.dialogFormVisible = false
              this.$message({
                message: '添加成功',
                type: 'success'
              });
            }
            else {
              this.$message.error('添加失败');
            }
            this.$nextTick(this.fetchData())
          })
        }
      })
    },
    handleRestore(index, row) {
      restoreMenuById(row.menuId).then(response => {
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
    }
  }
}
</script>

<style scoped>
.right-aligned-content {
  display: flex;
  justify-content: flex-end;
}
</style>
