<template>
    <div class="app-container">
        <div class="filter-container">
            <el-row>
                <el-col :span="6" style="float:right; margin-left: 10px; margin-bottom: 20px">
                    <el-input clearable placeholder="请输入用户ID或评论ID" prefix-icon="el-icon-search" v-model="keyword"
                        @change="fetchData(1)" />
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

            <el-table-column label="用户ID" min-width="150" align="center">
                <template slot-scope="scope">
                    <span>{{ scope.row.userId }}</span>
                </template>
            </el-table-column>

            <el-table-column label="评论ID" min-width="100" align="center">
                <template slot-scope="scope">
                    <span>{{ scope.row.commentId }}</span>
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
import { deleteAnyCommentById, findAllComments, findAllLikes, findLikesById } from '@/api/table'
import { } from "@/api/permission";

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
                findAllLikes(pageNum).then(response => {
                    this.list = response.data.records
                    this.listLoading = false
                    this.pages = response.data.pages
                    this.curPage = response.data.current
                })
            }
            else {
                let obj = { userId: null, commentId: null, pageNum: pageNum }
                if (!this.isNumber(this.keyword))
                    obj.userId = this.keyword
                else
                    obj.commentId = this.keyword

                findLikesById(obj).then(response => {
                    this.list = response.data.records
                    this.listLoading = false
                    this.pages = response.data.pages
                    this.curPage = response.data.current
                })
            }
        },
        resetTemp() {
            this.temp = {
            }
        },
        handleCurrentPageChange(newPage) {
            this.fetchData(newPage)
        },
        isNumber(str) {
            let flag = true;
            for (let i = 0; i < str.length; i++) {
                if (!(str[i] >= '0' && str[i] <= '9')) {
                    flag = false;
                    break;
                }
            }
            return flag;
        }
    }
}
</script>
  