<template>
    <div class="app-container">
        <div class="filter-container">
            <el-row>
                <el-col :span="6" style="float:right; margin-left: 10px; margin-bottom: 20px">
                    <el-input clearable placeholder="请输入评论ID" prefix-icon="el-icon-search"
                        onkeyup="value=value.replace(/[^\d]/g,'')" v-model="keyword" @change="fetchData(1)" />
                </el-col>
            </el-row>
        </div>

        <el-table v-loading="listLoading" :data="list" element-loading-text="加载中……" border style="width: 100%;" fit
            highlight-current-row :ke="itemKey">
            <el-table-column align="center" label="序号" width="50px">
                <template slot-scope="scope">
                    {{ scope.$index }}
                </template>
            </el-table-column>

            <el-table-column label="评论ID" min-width="50" align="center">
                <template slot-scope="scope">
                    {{ scope.row.commentId }}
                </template>
            </el-table-column>

            <el-table-column label="电影ID" min-width="50" align="center">
                <template slot-scope="scope">
                    <span>{{ scope.row.movieId }}</span>
                </template>
            </el-table-column>

            <el-table-column label="用户ID" min-width="150px" align="center">
                <template slot-scope="scope">
                    <span>{{ scope.row.userId }}</span>
                </template>
            </el-table-column>

            <el-table-column label="获赞数量" min-width="50px" align="center">
                <template slot-scope="scope">
                    {{ scope.row.votes }}
                </template>
            </el-table-column>

            <el-table-column label="评分" min-width="50" align="center">
                <template slot-scope="scope">
                    {{ scope.row.rating }}
                </template>
            </el-table-column>

            <el-table-column label="评论内容" min-width="200px" align="center">
                <template slot-scope="scope">
                    {{ scope.row.content }}
                </template>
            </el-table-column>

            <el-table-column label="评论时间" min-width="100px" align="center">
                <template slot-scope="scope">
                    {{ scope.row.commentTime }}
                </template>
            </el-table-column>

            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">

                    <el-button v-if="scope.row.delFlag != 1" size="mini" type="danger"
                        @click="handleDelete(scope.$index, scope.row)">删除</el-button>

                </template>
            </el-table-column>
        </el-table>

        <el-row>
            <el-col :span="10">
                <el-pagination background layout="prev, pager, next, jumper, ->" @current-change="handleCurrentPageChange"
                    :page-count="this.pages" />
            </el-col>
        </el-row>
    </div>
</template>
<script>
import { deleteAnyCommentById, findAllComments, findCommentById } from '@/api/table'

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
            list: [
                {
                }
            ],
            //多选框勾选的选项
            checkRoles: [],
            userRoles: [{
                roleId: 0,
                roleName: "",
                delFlag: 0
            }],
            temp: {
            },
            textMap: {
                update: '编辑',
                create: '创建',
                authenticate: '角色授权',
            },
            dialogStatus: "",
            listLoading: true,
            tempRow: null, // 当前行
            rules: {
            },
            pages: null,
            curPage: 1,
            keyword: "",
            pageType: 0
        }
    },
    created() {
        this.fetchData(1)
    },
    methods: {
        fetchData(pageNum = 1) {
            this.listLoading = true
            if (this.keyword == "" || this.keyword == null) {
                findAllComments(pageNum).then(response => {
                    this.list = response.data.records
                    this.listLoading = false
                    this.pages = response.data.pages
                    this.curPage = response.data.current
                })
            }
            else {
                findCommentById(this.keyword).then(response => {
                    this.list = []
                    this.list.push(response.data)
                    this.listLoading = false
                })
            }
        },
        resetTemp() {
            this.temp = {
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

                    setUserById(this.temp).then((response) => {
                        if (response.data["影响行数"] !== 0) {
                            this.dialogFormVisible = false
                            this.$message({
                                message: '更新成功',
                                type: 'success'
                            });
                        } else {
                            this.$message.error('更新失败');
                        }
                        this.$nextTick(this.fetchData(this.curPage))
                    })
                }
            })
        },
        handleDelete(index, row) {
            this.$confirm('确定要删除该评论吗', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                deleteAnyCommentById(row.commentId).then(response => {
                    if (response.data["影响行数"] !== 0) {
                        this.$message({
                            type: 'success',
                            message: '删除成功!'
                        });
                    }
                    else {
                        this.$message.error('删除失败')
                    }
                    this.$nextTick(this.fetchData(this.curPage))
                })
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消'
                });
            });
        },
        handleCurrentPageChange(newPage) {
            this.fetchData(newPage)
        },
    }
}
</script>
  